package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val phone: String,
    val role: String, // WORKER, SUPERVISOR, CONTRACTOR, ADMIN
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

@Entity(tableName = "construction_sites")
data class ConstructionSiteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val contractorId: Long = 1,
    val supervisorId: Long = 2,
    val name: String,
    val developer: String,
    val address: String,
    val area: String,
    val latitude: Double,
    val longitude: Double,
    val status: String = "ACTIVE",
    val totalWorkersNeeded: Int = 45,
    val activeWorkersHired: Int = 38,
    val estimatedCompletion: String = "March 2027"
)

@Entity(tableName = "jobs")
data class JobEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val siteId: Long,
    val supervisorId: Long,
    val title: String,
    val category: String, // Mason, Electrician, Plumber, Painter, Bar Bender, Carpenter, Helper, Welder
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
    val supervisorName: String,
    val supervisorPhone: String,
    val distanceKm: Float = 3.5f,
    val status: String = "OPEN", // OPEN, FILLED, EXPIRED
    val postedAgo: String = "Today",
    val isVerified: Boolean = true
)

@Entity(tableName = "applications")
data class ApplicationEntity(
    @PrimaryKey(autoGenerate = true)
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
    val status: String = "APPLIED", // APPLIED, SHORTLISTED, HIRED, REJECTED
    val appliedDate: String = "Today",
    val supervisorPhone: String = "+91 98451 22340"
)

@Entity(tableName = "attendance")
data class AttendanceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val siteId: Long,
    val siteName: String,
    val workerId: Long,
    val workerName: String,
    val skill: String,
    val date: String,
    val status: String = "PRESENT", // PRESENT, ABSENT, HALF_DAY
    val dailyWage: Int = 900,
    val advancePaid: Int = 0,
    val notes: String = ""
)

@Entity(tableName = "platform_reports")
data class PlatformReportEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: String,
    val reportedBy: String,
    val againstEntity: String,
    val description: String,
    val status: String = "INVESTIGATING", // INVESTIGATING, RESOLVED
    val date: String = "Nov 2026"
)
