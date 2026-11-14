package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.ApplicationEntity
import com.example.data.local.entity.AttendanceEntity
import com.example.data.local.entity.ConstructionSiteEntity
import com.example.data.local.entity.JobEntity
import com.example.data.local.entity.PlatformReportEntity
import com.example.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SiteMitraDao {

    // Jobs
    @Query("SELECT * FROM jobs ORDER BY isUrgent DESC, id DESC")
    fun getAllJobs(): Flow<List<JobEntity>>

    @Query("SELECT * FROM jobs WHERE category = :category ORDER BY id DESC")
    fun getJobsByCategory(category: String): Flow<List<JobEntity>>

    @Query("SELECT * FROM jobs WHERE id = :jobId LIMIT 1")
    suspend fun getJobById(jobId: Long): JobEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJob(job: JobEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJobs(jobs: List<JobEntity>)

    @Update
    suspend fun updateJob(job: JobEntity)

    // Construction Sites
    @Query("SELECT * FROM construction_sites ORDER BY id ASC")
    fun getAllSites(): Flow<List<ConstructionSiteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSites(sites: List<ConstructionSiteEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSite(site: ConstructionSiteEntity): Long

    // Applications
    @Query("SELECT * FROM applications ORDER BY id DESC")
    fun getAllApplications(): Flow<List<ApplicationEntity>>

    @Query("SELECT * FROM applications WHERE workerId = :workerId ORDER BY id DESC")
    fun getApplicationsByWorker(workerId: Long): Flow<List<ApplicationEntity>>

    @Query("SELECT * FROM applications WHERE jobId = :jobId ORDER BY id DESC")
    fun getApplicationsByJob(jobId: Long): Flow<List<ApplicationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplication(application: ApplicationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplications(applications: List<ApplicationEntity>)

    @Update
    suspend fun updateApplication(application: ApplicationEntity)

    // Attendance
    @Query("SELECT * FROM attendance ORDER BY date DESC, id DESC")
    fun getAllAttendance(): Flow<List<AttendanceEntity>>

    @Query("SELECT * FROM attendance WHERE siteId = :siteId ORDER BY date DESC")
    fun getAttendanceBySite(siteId: Long): Flow<List<AttendanceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(attendance: AttendanceEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendanceList(list: List<AttendanceEntity>)

    @Update
    suspend fun updateAttendance(attendance: AttendanceEntity)

    // Platform Reports
    @Query("SELECT * FROM platform_reports ORDER BY id DESC")
    fun getAllReports(): Flow<List<PlatformReportEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: PlatformReportEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReports(reports: List<PlatformReportEntity>)

    @Update
    suspend fun updateReport(report: PlatformReportEntity)

    // Users
    @Query("SELECT * FROM users WHERE role = :role LIMIT 1")
    suspend fun getUserByRole(role: String): UserEntity?

    @Query("SELECT * FROM users WHERE phone LIKE '%' || :phone || '%' LIMIT 1")
    suspend fun getUserByPhone(phone: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    fun getUserById(userId: Long): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Update
    suspend fun updateUser(user: UserEntity)

    // One-shot snapshots for Data Server synchronization
    @Query("SELECT * FROM users ORDER BY id ASC")
    suspend fun getAllUsersOnce(): List<UserEntity>

    @Query("SELECT * FROM construction_sites ORDER BY id ASC")
    suspend fun getAllSitesOnce(): List<ConstructionSiteEntity>

    @Query("SELECT * FROM jobs ORDER BY isUrgent DESC, id DESC")
    suspend fun getAllJobsOnce(): List<JobEntity>

    @Query("SELECT * FROM applications ORDER BY id DESC")
    suspend fun getAllApplicationsOnce(): List<ApplicationEntity>

    @Query("SELECT * FROM attendance ORDER BY id DESC")
    suspend fun getAllAttendanceOnce(): List<AttendanceEntity>

    @Query("SELECT * FROM platform_reports ORDER BY id DESC")
    suspend fun getAllReportsOnce(): List<PlatformReportEntity>
}
