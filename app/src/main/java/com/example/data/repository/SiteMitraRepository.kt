package com.example.data.repository

import com.example.data.local.dao.SiteMitraDao
import com.example.data.local.entity.ApplicationEntity
import com.example.data.local.entity.AttendanceEntity
import com.example.data.local.entity.ConstructionSiteEntity
import com.example.data.local.entity.JobEntity
import com.example.data.local.entity.PlatformReportEntity
import com.example.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

class SiteMitraRepository(private val dao: SiteMitraDao) {

    val allJobs: Flow<List<JobEntity>> = dao.getAllJobs()
    val allSites: Flow<List<ConstructionSiteEntity>> = dao.getAllSites()
    val allApplications: Flow<List<ApplicationEntity>> = dao.getAllApplications()
    val allAttendance: Flow<List<AttendanceEntity>> = dao.getAllAttendance()
    val allReports: Flow<List<PlatformReportEntity>> = dao.getAllReports()

    fun getWorkerApplications(workerId: Long): Flow<List<ApplicationEntity>> =
        dao.getApplicationsByWorker(workerId)

    fun getUserById(userId: Long): Flow<UserEntity?> = dao.getUserById(userId)

    suspend fun insertJob(job: JobEntity): Long = dao.insertJob(job)

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
        return dao.insertApplication(application)
    }

    suspend fun updateApplicationStatus(application: ApplicationEntity, newStatus: String) {
        val updated = application.copy(status = newStatus)
        dao.updateApplication(updated)

        // If hired, update job hiredCount
        if (newStatus == "HIRED") {
            val job = dao.getJobById(application.jobId)
            if (job != null) {
                dao.updateJob(job.copy(hiredCount = job.hiredCount + 1))
            }
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
    }

    suspend fun addAttendanceRecord(attendance: AttendanceEntity): Long =
        dao.insertAttendance(attendance)

    suspend fun addConstructionSite(site: ConstructionSiteEntity): Long =
        dao.insertSite(site)

    suspend fun updateUser(user: UserEntity) = dao.updateUser(user)

    suspend fun getUserByPhone(phone: String): UserEntity? = dao.getUserByPhone(phone)

    suspend fun getUserByRole(role: String): UserEntity? = dao.getUserByRole(role)

    suspend fun insertUser(user: UserEntity): Long = dao.insertUser(user)

    suspend fun resolveReport(report: PlatformReportEntity) {
        dao.updateReport(report.copy(status = "RESOLVED"))
    }

    suspend fun addReport(report: PlatformReportEntity): Long =
        dao.insertReport(report)
}
