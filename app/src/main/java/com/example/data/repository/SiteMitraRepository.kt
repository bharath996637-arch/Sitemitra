package com.example.data.repository

import com.example.data.local.dao.SiteMitraDao
import com.example.data.local.entity.ApplicationEntity
import com.example.data.local.entity.AttendanceEntity
import com.example.data.local.entity.ConstructionSiteEntity
import com.example.data.local.entity.JobEntity
import com.example.data.local.entity.PlatformReportEntity
import com.example.data.local.entity.UserEntity
import com.example.data.remote.ApplicationStatusPatchDto
import com.example.data.remote.AttendancePatchDto
import com.example.data.remote.ReportPatchDto
import com.example.data.remote.SiteMitraServerConfig
import com.example.data.remote.SyncSnapshotDto
import com.example.data.remote.toDto
import com.example.data.remote.toEntity
import kotlinx.coroutines.flow.Flow

data class SyncSummary(
    val usersCount: Int,
    val sitesCount: Int,
    val jobsCount: Int,
    val applicationsCount: Int,
    val attendanceCount: Int,
    val reportsCount: Int,
    val endpointUrl: String,
    val latencyMs: Long
)

class SiteMitraRepository(private val dao: SiteMitraDao) {

    val allJobs: Flow<List<JobEntity>> = dao.getAllJobs()
    val allSites: Flow<List<ConstructionSiteEntity>> = dao.getAllSites()
    val allApplications: Flow<List<ApplicationEntity>> = dao.getAllApplications()
    val allAttendance: Flow<List<AttendanceEntity>> = dao.getAllAttendance()
    val allReports: Flow<List<PlatformReportEntity>> = dao.getAllReports()

    fun getWorkerApplications(workerId: Long): Flow<List<ApplicationEntity>> =
        dao.getApplicationsByWorker(workerId)

    fun getUserById(userId: Long): Flow<UserEntity?> = dao.getUserById(userId)

    /**
     * Performs bidirectional synchronization between the local Room database and the live
     * SiteMitra REST Data Server (/api/sync).
     */
    suspend fun syncWithServer(): Result<SyncSummary> {
        val start = System.currentTimeMillis()
        return try {
            val api = SiteMitraServerConfig.resolveReachableApi()
                ?: return Result.failure(IllegalStateException("SiteMitra Data Server unreachable"))

            val localSnapshot = SyncSnapshotDto(
                users = dao.getAllUsersOnce().map { it.toDto() },
                sites = dao.getAllSitesOnce().map { it.toDto() },
                jobs = dao.getAllJobsOnce().map { it.toDto() },
                applications = dao.getAllApplicationsOnce().map { it.toDto() },
                attendance = dao.getAllAttendanceOnce().map { it.toDto() },
                reports = dao.getAllReportsOnce().map { it.toDto() }
            )

            val remoteSnapshot = api.postSyncSnapshot(localSnapshot)

            if (remoteSnapshot.users.isNotEmpty()) {
                dao.insertUsers(remoteSnapshot.users.map { it.toEntity() })
            }
            if (remoteSnapshot.sites.isNotEmpty()) {
                dao.insertSites(remoteSnapshot.sites.map { it.toEntity() })
            }
            if (remoteSnapshot.jobs.isNotEmpty()) {
                dao.insertJobs(remoteSnapshot.jobs.map { it.toEntity() })
            }
            if (remoteSnapshot.applications.isNotEmpty()) {
                dao.insertApplications(remoteSnapshot.applications.map { it.toEntity() })
            }
            if (remoteSnapshot.attendance.isNotEmpty()) {
                dao.insertAttendanceList(remoteSnapshot.attendance.map { it.toEntity() })
            }
            if (remoteSnapshot.reports.isNotEmpty()) {
                dao.insertReports(remoteSnapshot.reports.map { it.toEntity() })
            }

            val latency = System.currentTimeMillis() - start
            Result.success(
                SyncSummary(
                    usersCount = remoteSnapshot.users.size,
                    sitesCount = remoteSnapshot.sites.size,
                    jobsCount = remoteSnapshot.jobs.size,
                    applicationsCount = remoteSnapshot.applications.size,
                    attendanceCount = remoteSnapshot.attendance.size,
                    reportsCount = remoteSnapshot.reports.size,
                    endpointUrl = SiteMitraServerConfig.activeBaseUrl,
                    latencyMs = latency
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun insertJob(job: JobEntity): Long {
        val syncedEntity = runCatching {
            SiteMitraServerConfig.resolveReachableApi()?.createOrUpdateJob(job.toDto())?.toEntity()
        }.getOrNull()
        return dao.insertJob(syncedEntity ?: job)
    }

    suspend fun applyToJob(
        job: JobEntity,
        worker: UserEntity
    ): Long {
        val application = ApplicationEntity(
            jobId = job.id,
            jobTitle = job.title,
            siteName = job.siteName,
            wagePerDay = job.wagePerDay,
            workerId = worker.id,
            workerName = worker.name,
            workerPhone = worker.phone,
            workerSkill = worker.skills,
            workerExperience = worker.experienceYears,
            status = "APPLIED",
            appliedDate = "Today, just now",
            supervisorPhone = job.supervisorPhone
        )
        val syncedEntity = runCatching {
            SiteMitraServerConfig.resolveReachableApi()?.createApplication(application.toDto())?.toEntity()
        }.getOrNull()
        return dao.insertApplication(syncedEntity ?: application)
    }

    suspend fun updateApplicationStatus(application: ApplicationEntity, newStatus: String) {
        val updated = application.copy(status = newStatus)
        dao.updateApplication(updated)

        // If hired, update job hiredCount locally
        if (newStatus == "HIRED") {
            val job = dao.getJobById(application.jobId)
            if (job != null) {
                dao.updateJob(job.copy(hiredCount = job.hiredCount + 1))
            }
        }

        // Push status mutation to Data Server
        runCatching {
            SiteMitraServerConfig.resolveReachableApi()?.updateApplicationStatus(
                id = application.id,
                patch = ApplicationStatusPatchDto(status = newStatus)
            )
        }
    }

    suspend fun updateAttendanceStatus(
        attendance: AttendanceEntity,
        newStatus: String,
        advance: Int? = null
    ) {
        val updated = attendance.copy(
            status = newStatus,
            advancePaid = advance ?: attendance.advancePaid
        )
        dao.updateAttendance(updated)

        runCatching {
            SiteMitraServerConfig.resolveReachableApi()?.updateAttendance(
                id = attendance.id,
                patch = AttendancePatchDto(
                    status = newStatus,
                    advancePaid = updated.advancePaid
                )
            )
        }
    }

    suspend fun addAttendanceRecord(attendance: AttendanceEntity): Long {
        val syncedEntity = runCatching {
            SiteMitraServerConfig.resolveReachableApi()?.createAttendance(attendance.toDto())?.toEntity()
        }.getOrNull()
        return dao.insertAttendance(syncedEntity ?: attendance)
    }

    suspend fun addConstructionSite(site: ConstructionSiteEntity): Long {
        val syncedEntity = runCatching {
            SiteMitraServerConfig.resolveReachableApi()?.createOrUpdateSite(site.toDto())?.toEntity()
        }.getOrNull()
        return dao.insertSite(syncedEntity ?: site)
    }

    suspend fun updateUser(user: UserEntity) {
        dao.updateUser(user)
        runCatching {
            SiteMitraServerConfig.resolveReachableApi()?.upsertUser(user.toDto())
        }
    }

    suspend fun getUserByPhone(phone: String): UserEntity? = dao.getUserByPhone(phone)

    suspend fun getUserByRole(role: String): UserEntity? = dao.getUserByRole(role)

    suspend fun insertUser(user: UserEntity): Long {
        val syncedEntity = runCatching {
            SiteMitraServerConfig.resolveReachableApi()?.upsertUser(user.toDto())?.toEntity()
        }.getOrNull()
        return dao.insertUser(syncedEntity ?: user)
    }

    suspend fun resolveReport(report: PlatformReportEntity) {
        dao.updateReport(report.copy(status = "RESOLVED"))
        runCatching {
            SiteMitraServerConfig.resolveReachableApi()?.updateReport(
                id = report.id,
                patch = ReportPatchDto(status = "RESOLVED")
            )
        }
    }

    suspend fun addReport(report: PlatformReportEntity): Long {
        val syncedEntity = runCatching {
            SiteMitraServerConfig.resolveReachableApi()?.createReport(report.toDto())?.toEntity()
        }.getOrNull()
        return dao.insertReport(syncedEntity ?: report)
    }
}
