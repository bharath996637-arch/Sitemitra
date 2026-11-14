package com.example.data.remote

import com.example.data.local.entity.ApplicationEntity
import com.example.data.local.entity.AttendanceEntity
import com.example.data.local.entity.ConstructionSiteEntity
import com.example.data.local.entity.JobEntity
import com.example.data.local.entity.PlatformReportEntity
import com.example.data.local.entity.UserEntity
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

data class HealthCountsDto(
    val users: Int = 0,
    val sites: Int = 0,
    val jobs: Int = 0,
    val applications: Int = 0,
    val attendance: Int = 0,
    val reports: Int = 0
)

data class HealthResponseDto(
    val status: String = "ok",
    val service: String = "SiteMitra Data Server",
    val region: String = "Google Cloud Run (asia-northeast1)",
    val version: String = "1.0.0",
    val counts: HealthCountsDto = HealthCountsDto(),
    val timestamp: String = ""
)

data class UserDto(
    val id: Long = 0,
    val name: String,
    val phone: String,
    val role: String,
    val language: String = "en",
    val status: String = "VERIFIED",
    val skills: String = "Mason, Brickwork",
    val experienceYears: Int = 5,
    val expectedWage: Int = 900,
    val isAvailable: Boolean = true,
    val rating: Float = 4.8f,
    val completedJobsCount: Int = 18,
    val city: String = "Bengaluru"
)

data class SiteDto(
    val id: Long = 0,
    val contractorId: Long = 1,
    val supervisorId: Long = 2,
    val name: String,
    val developer: String = "SiteMitra Partner",
    val address: String,
    val area: String,
    val latitude: Double = 12.9716,
    val longitude: Double = 77.5946,
    val status: String = "ACTIVE",
    val totalWorkersNeeded: Int = 45,
    val activeWorkersHired: Int = 38,
    val estimatedCompletion: String = "March 2027"
)

data class JobDto(
    val id: Long = 0,
    val siteId: Long = 1,
    val supervisorId: Long = 2,
    val title: String,
    val category: String,
    val wagePerDay: Int,
    val vacancies: Int,
    val hiredCount: Int = 0,
    val startDate: String = "Immediate",
    val workDurationDays: Int = 30,
    val foodProvided: Boolean = true,
    val stayProvided: Boolean = false,
    val isUrgent: Boolean = false,
    val siteName: String,
    val siteAddress: String,
    val area: String,
    val supervisorName: String = "Mestri Govindappa",
    val supervisorPhone: String = "+91 98451 22340",
    val distanceKm: Float = 3.5f,
    val status: String = "OPEN",
    val postedAgo: String = "Today",
    val isVerified: Boolean = true
)

data class ApplicationDto(
    val id: Long = 0,
    val jobId: Long,
    val jobTitle: String,
    val siteName: String,
    val wagePerDay: Int,
    val workerId: Long,
    val workerName: String,
    val workerPhone: String,
    val workerSkill: String,
    val workerExperience: Int,
    val status: String = "APPLIED",
    val appliedDate: String = "Today",
    val supervisorPhone: String = "+91 98451 22340"
)

data class AttendanceDto(
    val id: Long = 0,
    val siteId: Long,
    val siteName: String,
    val workerId: Long,
    val workerName: String,
    val skill: String,
    val date: String,
    val status: String = "PRESENT",
    val dailyWage: Int = 900,
    val advancePaid: Int = 0,
    val notes: String = ""
)

data class ReportDto(
    val id: Long = 0,
    val category: String,
    val reportedBy: String,
    val againstEntity: String,
    val description: String,
    val status: String = "INVESTIGATING",
    val date: String = "Nov 2026"
)

data class SyncSnapshotDto(
    val users: List<UserDto> = emptyList(),
    val sites: List<SiteDto> = emptyList(),
    val jobs: List<JobDto> = emptyList(),
    val applications: List<ApplicationDto> = emptyList(),
    val attendance: List<AttendanceDto> = emptyList(),
    val reports: List<ReportDto> = emptyList(),
    val syncedAt: String? = null
)

data class ApplicationStatusPatchDto(
    val status: String
)

data class AttendancePatchDto(
    val status: String,
    val advancePaid: Int? = null
)

data class ReportPatchDto(
    val status: String
)

interface SiteMitraApi {
    @GET("api/health")
    suspend fun getHealth(): HealthResponseDto

    @GET("api/sync")
    suspend fun getSyncSnapshot(): SyncSnapshotDto

    @POST("api/sync")
    suspend fun postSyncSnapshot(@Body snapshot: SyncSnapshotDto): SyncSnapshotDto

    @POST("api/users")
    suspend fun upsertUser(@Body user: UserDto): UserDto

    @POST("api/sites")
    suspend fun createOrUpdateSite(@Body site: SiteDto): SiteDto

    @POST("api/jobs")
    suspend fun createOrUpdateJob(@Body job: JobDto): JobDto

    @POST("api/applications")
    suspend fun createApplication(@Body application: ApplicationDto): ApplicationDto

    @PATCH("api/applications/{id}")
    suspend fun updateApplicationStatus(
        @Path("id") id: Long,
        @Body patch: ApplicationStatusPatchDto
    ): ApplicationDto

    @POST("api/attendance")
    suspend fun createAttendance(@Body attendance: AttendanceDto): AttendanceDto

    @PATCH("api/attendance/{id}")
    suspend fun updateAttendance(
        @Path("id") id: Long,
        @Body patch: AttendancePatchDto
    ): AttendanceDto

    @POST("api/reports")
    suspend fun createReport(@Body report: ReportDto): ReportDto

    @PATCH("api/reports/{id}")
    suspend fun updateReport(
        @Path("id") id: Long,
        @Body patch: ReportPatchDto
    ): ReportDto
}

// Entity <-> DTO Mappers

fun UserEntity.toDto() = UserDto(
    id = id,
    name = name,
    phone = phone,
    role = role,
    language = language,
    status = status,
    skills = skills,
    experienceYears = experienceYears,
    expectedWage = expectedWage,
    isAvailable = isAvailable,
    rating = rating,
    completedJobsCount = completedJobsCount,
    city = city
)

fun UserDto.toEntity() = UserEntity(
    id = id,
    name = name,
    phone = phone,
    role = role,
    language = language,
    status = status,
    skills = skills,
    experienceYears = experienceYears,
    expectedWage = expectedWage,
    isAvailable = isAvailable,
    rating = rating,
    completedJobsCount = completedJobsCount,
    city = city
)

fun ConstructionSiteEntity.toDto() = SiteDto(
    id = id,
    contractorId = contractorId,
    supervisorId = supervisorId,
    name = name,
    developer = developer,
    address = address,
    area = area,
    latitude = latitude,
    longitude = longitude,
    status = status,
    totalWorkersNeeded = totalWorkersNeeded,
    activeWorkersHired = activeWorkersHired,
    estimatedCompletion = estimatedCompletion
)

fun SiteDto.toEntity() = ConstructionSiteEntity(
    id = id,
    contractorId = contractorId,
    supervisorId = supervisorId,
    name = name,
    developer = developer,
    address = address,
    area = area,
    latitude = latitude,
    longitude = longitude,
    status = status,
    totalWorkersNeeded = totalWorkersNeeded,
    activeWorkersHired = activeWorkersHired,
    estimatedCompletion = estimatedCompletion
)

fun JobEntity.toDto() = JobDto(
    id = id,
    siteId = siteId,
    supervisorId = supervisorId,
    title = title,
    category = category,
    wagePerDay = wagePerDay,
    vacancies = vacancies,
    hiredCount = hiredCount,
    startDate = startDate,
    workDurationDays = workDurationDays,
    foodProvided = foodProvided,
    stayProvided = stayProvided,
    isUrgent = isUrgent,
    siteName = siteName,
    siteAddress = siteAddress,
    area = area,
    supervisorName = supervisorName,
    supervisorPhone = supervisorPhone,
    distanceKm = distanceKm,
    status = status,
    postedAgo = postedAgo,
    isVerified = isVerified
)

fun JobDto.toEntity() = JobEntity(
    id = id,
    siteId = siteId,
    supervisorId = supervisorId,
    title = title,
    category = category,
    wagePerDay = wagePerDay,
    vacancies = vacancies,
    hiredCount = hiredCount,
    startDate = startDate,
    workDurationDays = workDurationDays,
    foodProvided = foodProvided,
    stayProvided = stayProvided,
    isUrgent = isUrgent,
    siteName = siteName,
    siteAddress = siteAddress,
    area = area,
    supervisorName = supervisorName,
    supervisorPhone = supervisorPhone,
    distanceKm = distanceKm,
    status = status,
    postedAgo = postedAgo,
    isVerified = isVerified
)

fun ApplicationEntity.toDto() = ApplicationDto(
    id = id,
    jobId = jobId,
    jobTitle = jobTitle,
    siteName = siteName,
    wagePerDay = wagePerDay,
    workerId = workerId,
    workerName = workerName,
    workerPhone = workerPhone,
    workerSkill = workerSkill,
    workerExperience = workerExperience,
    status = status,
    appliedDate = appliedDate,
    supervisorPhone = supervisorPhone
)

fun ApplicationDto.toEntity() = ApplicationEntity(
    id = id,
    jobId = jobId,
    jobTitle = jobTitle,
    siteName = siteName,
    wagePerDay = wagePerDay,
    workerId = workerId,
    workerName = workerName,
    workerPhone = workerPhone,
    workerSkill = workerSkill,
    workerExperience = workerExperience,
    status = status,
    appliedDate = appliedDate,
    supervisorPhone = supervisorPhone
)

fun AttendanceEntity.toDto() = AttendanceDto(
    id = id,
    siteId = siteId,
    siteName = siteName,
    workerId = workerId,
    workerName = workerName,
    skill = skill,
    date = date,
    status = status,
    dailyWage = dailyWage,
    advancePaid = advancePaid,
    notes = notes
)

fun AttendanceDto.toEntity() = AttendanceEntity(
    id = id,
    siteId = siteId,
    siteName = siteName,
    workerId = workerId,
    workerName = workerName,
    skill = skill,
    date = date,
    status = status,
    dailyWage = dailyWage,
    advancePaid = advancePaid,
    notes = notes
)

fun PlatformReportEntity.toDto() = ReportDto(
    id = id,
    category = category,
    reportedBy = reportedBy,
    againstEntity = againstEntity,
    description = description,
    status = status,
    date = date
)

fun ReportDto.toEntity() = PlatformReportEntity(
    id = id,
    category = category,
    reportedBy = reportedBy,
    againstEntity = againstEntity,
    description = description,
    status = status,
    date = date
)
