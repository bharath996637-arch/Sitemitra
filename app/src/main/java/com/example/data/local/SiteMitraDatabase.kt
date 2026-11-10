package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.local.dao.SiteMitraDao
import com.example.data.local.entity.ApplicationEntity
import com.example.data.local.entity.AttendanceEntity
import com.example.data.local.entity.ConstructionSiteEntity
import com.example.data.local.entity.JobEntity
import com.example.data.local.entity.PlatformReportEntity
import com.example.data.local.entity.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        ConstructionSiteEntity::class,
        JobEntity::class,
        ApplicationEntity::class,
        AttendanceEntity::class,
        PlatformReportEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SiteMitraDatabase : RoomDatabase() {

    abstract fun dao(): SiteMitraDao

    companion object {
        @Volatile
        private var INSTANCE: SiteMitraDatabase? = null

        fun getInstance(context: Context): SiteMitraDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SiteMitraDatabase::class.java,
                    "sitemitra_database.db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                getInstance(context).dao().apply {
                                    insertUsers(InitialData.users)
                                    insertSites(InitialData.sites)
                                    insertJobs(InitialData.jobs)
                                    insertApplications(InitialData.applications)
                                    insertAttendanceList(InitialData.attendanceList)
                                    insertReports(InitialData.reports)
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

object InitialData {
    val users = listOf(
        UserEntity(
            id = 1,
            name = "Ramesh Kumar (ಕಾರ್ಮಿಕ)",
            phone = "+91 98450 78210",
            role = "WORKER",
            skills = "Mason, Tile Laying, Brickwork",
            experienceYears = 6,
            expectedWage = 950,
            isAvailable = true,
            rating = 4.9f,
            completedJobsCount = 28,
            city = "Bengaluru"
        ),
        UserEntity(
            id = 2,
            name = "Mestri Govindappa (ಮೇಸ್ತ್ರಿ)",
            phone = "+91 98451 22340",
            role = "SUPERVISOR",
            skills = "Site Supervision, Labour Management",
            experienceYears = 14,
            expectedWage = 1600,
            isAvailable = true,
            rating = 4.8f,
            completedJobsCount = 74,
            city = "Bengaluru"
        ),
        UserEntity(
            id = 3,
            name = "Suresh Reddy (Reddy Infra)",
            phone = "+91 99002 44551",
            role = "CONTRACTOR",
            skills = "General Contracting, Structural Works",
            experienceYears = 22,
            expectedWage = 0,
            isAvailable = true,
            rating = 4.9f,
            completedJobsCount = 120,
            city = "Bengaluru"
        ),
        UserEntity(
            id = 4,
            name = "SiteMitra Admin",
            phone = "+91 80 4012 8899",
            role = "ADMIN",
            skills = "Pilot Operations, Moderation & Safety",
            experienceYears = 10,
            expectedWage = 0,
            isAvailable = true,
            rating = 5.0f,
            completedJobsCount = 500,
            city = "Bengaluru"
        )
    )

    val sites = listOf(
        ConstructionSiteEntity(
            id = 1,
            contractorId = 3,
            supervisorId = 2,
            name = "Nalini Towers Enclave",
            developer = "Reddy Infra & Properties",
            address = "Near ITPL, Whitefield, Bengaluru",
            area = "Whitefield",
            latitude = 12.9850,
            longitude = 77.7314,
            status = "ACTIVE",
            totalWorkersNeeded = 50,
            activeWorkersHired = 42,
            estimatedCompletion = "Dec 2026"
        ),
        ConstructionSiteEntity(
            id = 2,
            contractorId = 3,
            supervisorId = 2,
            name = "Brigade Tech Park Extension",
            developer = "Brigade Enterprises",
            address = "Velankani Drive, Electronic City Phase 1",
            area = "Electronic City",
            latitude = 12.8452,
            longitude = 77.6602,
            status = "ACTIVE",
            totalWorkersNeeded = 65,
            activeWorkersHired = 54,
            estimatedCompletion = "August 2027"
        ),
        ConstructionSiteEntity(
            id = 3,
            contractorId = 3,
            supervisorId = 2,
            name = "Prestige Green Glen Apartments",
            developer = "Prestige Estates",
            address = "Kaveriappa Compound, Bellandur ORR",
            area = "Bellandur",
            latitude = 12.9279,
            longitude = 77.6835,
            status = "ACTIVE",
            totalWorkersNeeded = 40,
            activeWorkersHired = 35,
            estimatedCompletion = "April 2027"
        ),
        ConstructionSiteEntity(
            id = 4,
            contractorId = 3,
            supervisorId = 2,
            name = "Sobha Dream Acres Block 7",
            developer = "Sobha Limited",
            address = "Panathur Balagere Main Road, Varthur",
            area = "Varthur",
            latitude = 12.9340,
            longitude = 77.7120,
            status = "ACTIVE",
            totalWorkersNeeded = 55,
            activeWorkersHired = 48,
            estimatedCompletion = "Nov 2027"
        ),
        ConstructionSiteEntity(
            id = 5,
            contractorId = 3,
            supervisorId = 2,
            name = "Godrej Splendour Towers",
            developer = "Godrej Properties",
            address = "Belathur Road, Kadugodi, Bengaluru",
            area = "Kadugodi",
            latitude = 12.9985,
            longitude = 77.7612,
            status = "ACTIVE",
            totalWorkersNeeded = 35,
            activeWorkersHired = 26,
            estimatedCompletion = "Feb 2027"
        ),
        ConstructionSiteEntity(
            id = 6,
            contractorId = 3,
            supervisorId = 2,
            name = "Purva Marina Palms",
            developer = "Puravankara Projects",
            address = "Bellary Road, Hebbal Flyover Junction",
            area = "Hebbal",
            latitude = 13.0358,
            longitude = 77.5970,
            status = "ACTIVE",
            totalWorkersNeeded = 45,
            activeWorkersHired = 39,
            estimatedCompletion = "June 2027"
        )
    )

    val jobs = listOf(
        JobEntity(
            id = 1,
            siteId = 1,
            supervisorId = 2,
            title = "Urgent: 8 Master Masons for Solid Brick Laying",
            category = "Mason",
            wagePerDay = 950,
            vacancies = 8,
            hiredCount = 5,
            startDate = "Tomorrow, 8:00 AM",
            workDurationDays = 25,
            foodProvided = true,
            stayProvided = true,
            isUrgent = true,
            siteName = "Nalini Towers Enclave",
            siteAddress = "Near ITPL, Whitefield, Bengaluru",
            area = "Whitefield",
            supervisorName = "Mestri Govindappa",
            supervisorPhone = "+91 98451 22340",
            distanceKm = 2.4f,
            status = "OPEN",
            postedAgo = "1 hour ago",
            isVerified = true
        ),
        JobEntity(
            id = 2,
            siteId = 2,
            supervisorId = 2,
            title = "Electricians for 3-Phase Conduit Wiring & DB Panels",
            category = "Electrician",
            wagePerDay = 1100,
            vacancies = 6,
            hiredCount = 4,
            startDate = "Immediate Joining",
            workDurationDays = 40,
            foodProvided = true,
            stayProvided = false,
            isUrgent = true,
            siteName = "Brigade Tech Park Extension",
            siteAddress = "Velankani Drive, Electronic City Phase 1",
            area = "Electronic City",
            supervisorName = "Mestri Manjunath",
            supervisorPhone = "+91 94480 33419",
            distanceKm = 5.8f,
            status = "OPEN",
            postedAgo = "3 hours ago",
            isVerified = true
        ),
        JobEntity(
            id = 3,
            siteId = 3,
            supervisorId = 2,
            title = "CPVC Plumbing Technicians & Helper Assistants",
            category = "Plumber",
            wagePerDay = 900,
            vacancies = 5,
            hiredCount = 2,
            startDate = "12 Nov 2026",
            workDurationDays = 30,
            foodProvided = true,
            stayProvided = false,
            isUrgent = false,
            siteName = "Prestige Green Glen Apartments",
            siteAddress = "Kaveriappa Compound, Bellandur ORR",
            area = "Bellandur",
            supervisorName = "Mestri Govindappa",
            supervisorPhone = "+91 98451 22340",
            distanceKm = 3.9f,
            status = "OPEN",
            postedAgo = "Yesterday",
            isVerified = true
        ),
        JobEntity(
            id = 4,
            siteId = 4,
            supervisorId = 2,
            title = "Bar Benders for 6th Floor Heavy Slab Casting",
            category = "Bar Bender",
            wagePerDay = 1050,
            vacancies = 10,
            hiredCount = 6,
            startDate = "Tomorrow Morning",
            workDurationDays = 15,
            foodProvided = true,
            stayProvided = true,
            isUrgent = true,
            siteName = "Sobha Dream Acres Block 7",
            siteAddress = "Panathur Balagere Main Road, Varthur",
            area = "Varthur",
            supervisorName = "Mestri Venkatesh",
            supervisorPhone = "+91 97312 88201",
            distanceKm = 4.2f,
            status = "OPEN",
            postedAgo = "Just now",
            isVerified = true
        ),
        JobEntity(
            id = 5,
            siteId = 5,
            supervisorId = 2,
            title = "Exterior Weatherproof Paint & Wall Putty Finishers",
            category = "Painter",
            wagePerDay = 850,
            vacancies = 6,
            hiredCount = 3,
            startDate = "14 Nov 2026",
            workDurationDays = 45,
            foodProvided = true,
            stayProvided = false,
            isUrgent = false,
            siteName = "Godrej Splendour Towers",
            siteAddress = "Belathur Road, Kadugodi, Bengaluru",
            area = "Kadugodi",
            supervisorName = "Mestri Govindappa",
            supervisorPhone = "+91 98451 22340",
            distanceKm = 6.1f,
            status = "OPEN",
            postedAgo = "4 hours ago",
            isVerified = true
        ),
        JobEntity(
            id = 6,
            siteId = 6,
            supervisorId = 2,
            title = "Aluminium Formwork & Shuttering Carpenters",
            category = "Carpenter",
            wagePerDay = 1150,
            vacancies = 7,
            hiredCount = 5,
            startDate = "Immediate",
            workDurationDays = 60,
            foodProvided = true,
            stayProvided = true,
            isUrgent = true,
            siteName = "Purva Marina Palms",
            siteAddress = "Bellary Road, Hebbal Flyover Junction",
            area = "Hebbal",
            supervisorName = "Mestri Shiva",
            supervisorPhone = "+91 96112 00934",
            distanceKm = 8.5f,
            status = "OPEN",
            postedAgo = "Today",
            isVerified = true
        ),
        JobEntity(
            id = 7,
            siteId = 1,
            supervisorId = 2,
            title = "General Construction Helpers (Material Handling & Curing)",
            category = "Helper",
            wagePerDay = 700,
            vacancies = 12,
            hiredCount = 8,
            startDate = "Immediate",
            workDurationDays = 90,
            foodProvided = true,
            stayProvided = true,
            isUrgent = false,
            siteName = "Nalini Towers Enclave",
            siteAddress = "Near ITPL, Whitefield, Bengaluru",
            area = "Whitefield",
            supervisorName = "Mestri Govindappa",
            supervisorPhone = "+91 98451 22340",
            distanceKm = 2.4f,
            status = "OPEN",
            postedAgo = "5 hours ago",
            isVerified = true
        ),
        JobEntity(
            id = 8,
            siteId = 3,
            supervisorId = 2,
            title = "MS Railing & Safety Balcony Welders",
            category = "Welder",
            wagePerDay = 1000,
            vacancies = 4,
            hiredCount = 2,
            startDate = "15 Nov 2026",
            workDurationDays = 20,
            foodProvided = true,
            stayProvided = false,
            isUrgent = false,
            siteName = "Prestige Green Glen Apartments",
            siteAddress = "Kaveriappa Compound, Bellandur ORR",
            area = "Bellandur",
            supervisorName = "Mestri Govindappa",
            supervisorPhone = "+91 98451 22340",
            distanceKm = 3.9f,
            status = "OPEN",
            postedAgo = "Yesterday",
            isVerified = true
        )
    )

    val applications = listOf(
        ApplicationEntity(
            id = 1,
            jobId = 1,
            jobTitle = "Urgent: 8 Master Masons for Solid Brick Laying",
            siteName = "Nalini Towers Enclave",
            wagePerDay = 950,
            workerId = 1,
            workerName = "Ramesh Kumar",
            workerPhone = "+91 98450 78210",
            workerSkill = "Mason",
            workerExperience = 6,
            status = "HIRED",
            appliedDate = "Yesterday, 10:30 AM",
            supervisorPhone = "+91 98451 22340"
        ),
        ApplicationEntity(
            id = 2,
            jobId = 2,
            jobTitle = "Electricians for 3-Phase Conduit Wiring & DB Panels",
            siteName = "Brigade Tech Park Extension",
            wagePerDay = 1100,
            workerId = 102,
            workerName = "Anand Swamy (ಆನಂದ್)",
            workerPhone = "+91 94481 99012",
            workerSkill = "Electrician",
            workerExperience = 5,
            status = "SHORTLISTED",
            appliedDate = "Today, 9:15 AM",
            supervisorPhone = "+91 94480 33419"
        ),
        ApplicationEntity(
            id = 3,
            jobId = 4,
            jobTitle = "Bar Benders for 6th Floor Heavy Slab Casting",
            siteName = "Sobha Dream Acres Block 7",
            wagePerDay = 1050,
            workerId = 103,
            workerName = "Mohan Lal (मोहन)",
            workerPhone = "+91 98230 45671",
            workerSkill = "Bar Bender",
            workerExperience = 7,
            status = "APPLIED",
            appliedDate = "Today, 11:00 AM",
            supervisorPhone = "+91 97312 88201"
        ),
        ApplicationEntity(
            id = 4,
            jobId = 6,
            jobTitle = "Aluminium Formwork & Shuttering Carpenters",
            siteName = "Purva Marina Palms",
            wagePerDay = 1150,
            workerId = 104,
            workerName = "Basavaraju (ಬಸವರಾಜು)",
            workerPhone = "+91 97410 33812",
            workerSkill = "Carpenter",
            workerExperience = 8,
            status = "HIRED",
            appliedDate = "08 Nov 2026",
            supervisorPhone = "+91 96112 00934"
        ),
        ApplicationEntity(
            id = 5,
            jobId = 1,
            jobTitle = "Urgent: 8 Master Masons for Solid Brick Laying",
            siteName = "Nalini Towers Enclave",
            wagePerDay = 950,
            workerId = 105,
            workerName = "Chandrappa (ಚಂದ್ರಪ್ಪ)",
            workerPhone = "+91 98455 12890",
            workerSkill = "Mason",
            workerExperience = 4,
            status = "APPLIED",
            appliedDate = "Today, 1:20 PM",
            supervisorPhone = "+91 98451 22340"
        )
    )

    val attendanceList = listOf(
        AttendanceEntity(
            id = 1,
            siteId = 1,
            siteName = "Nalini Towers Enclave",
            workerId = 1,
            workerName = "Ramesh Kumar (Mason)",
            skill = "Mason",
            date = "09 Nov 2026",
            status = "PRESENT",
            dailyWage = 950,
            advancePaid = 0,
            notes = "Completed block A north wall"
        ),
        AttendanceEntity(
            id = 2,
            siteId = 1,
            siteName = "Nalini Towers Enclave",
            workerId = 105,
            workerName = "Chandrappa (Mason)",
            skill = "Mason",
            date = "09 Nov 2026",
            status = "PRESENT",
            dailyWage = 950,
            advancePaid = 200,
            notes = "Advance given for bus fare"
        ),
        AttendanceEntity(
            id = 3,
            siteId = 1,
            siteName = "Nalini Towers Enclave",
            workerId = 108,
            workerName = "Siddaramaiah (Helper)",
            skill = "Helper",
            date = "09 Nov 2026",
            status = "PRESENT",
            dailyWage = 700,
            advancePaid = 0,
            notes = "Mortar mix duty"
        ),
        AttendanceEntity(
            id = 4,
            siteId = 1,
            siteName = "Nalini Towers Enclave",
            workerId = 109,
            workerName = "Raju Naik (Helper)",
            skill = "Helper",
            date = "09 Nov 2026",
            status = "HALF_DAY",
            dailyWage = 700,
            advancePaid = 0,
            notes = "Left at 1:30 PM due to fever"
        ),
        AttendanceEntity(
            id = 5,
            siteId = 2,
            siteName = "Brigade Tech Park Extension",
            workerId = 110,
            workerName = "Kiran Gowda (Electrician)",
            skill = "Electrician",
            date = "09 Nov 2026",
            status = "PRESENT",
            dailyWage = 1100,
            advancePaid = 0,
            notes = "Conduit floor 3 passed inspection"
        )
    )

    val reports = listOf(
        PlatformReportEntity(
            id = 1,
            category = "Safety Equipment Missing",
            reportedBy = "Worker Union Rep (Bengaluru)",
            againstEntity = "Sub-contractor at Panathur Site",
            description = "Shortage of safety harnesses on 7th floor scaffolding. Notified supervisor.",
            status = "RESOLVED",
            date = "07 Nov 2026"
        ),
        PlatformReportEntity(
            id = 2,
            category = "Wage Verification",
            reportedBy = "Siddaraju (Mason)",
            againstEntity = "Private Villa Project - Sarjapur",
            description = "Daily wage offered was ₹800 vs advertised ₹950. Listing was flagged for moderation.",
            status = "RESOLVED",
            date = "08 Nov 2026"
        ),
        PlatformReportEntity(
            id = 3,
            category = "Drinking Water & First Aid",
            reportedBy = "Site Safety Inspector",
            againstEntity = "Electronic City Site B",
            description = "Clean RO drinking water tank refilled and first aid kit restocked.",
            status = "RESOLVED",
            date = "09 Nov 2026"
        )
    )
}
