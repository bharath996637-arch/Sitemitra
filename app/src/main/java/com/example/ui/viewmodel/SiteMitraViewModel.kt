package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.SiteMitraDatabase
import com.example.data.local.entity.ApplicationEntity
import com.example.data.local.entity.AttendanceEntity
import com.example.data.local.entity.ConstructionSiteEntity
import com.example.data.local.entity.JobEntity
import com.example.data.local.entity.PlatformReportEntity
import com.example.data.local.entity.UserEntity
import com.example.data.model.Language
import com.example.data.model.TradeSkill
import com.example.data.model.UserRole
import com.example.data.repository.SiteMitraRepository
import com.example.util.LocationRequestHelper
import com.example.util.UserLocationInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class WorkerTab {
    HOME, FIND_WORK, LIVE_MAP, MY_JOBS, PROFILE
}

enum class SupervisorTab {
    DASHBOARD, APPLICANTS, ATTENDANCE, PROFILE
}

enum class ContractorTab {
    PROJECTS, WORKFORCE, REPORTS
}

enum class AdminTab {
    METRICS, MODERATION, GRIEVANCES
}

enum class LoginStep {
    ENTER_PHONE,
    ENTER_OTP,
    SELECT_ROLE
}

class SiteMitraViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SiteMitraRepository

    init {
        val db = SiteMitraDatabase.getInstance(application)
        repository = SiteMitraRepository(db.dao())
    }

    // Authentication & First-Screen Mobile Login State
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _loginStep = MutableStateFlow(LoginStep.ENTER_PHONE)
    val loginStep: StateFlow<LoginStep> = _loginStep.asStateFlow()

    private val _loginMobile = MutableStateFlow("")
    val loginMobile: StateFlow<String> = _loginMobile.asStateFlow()

    private val _generatedOtp = MutableStateFlow("562914")
    val generatedOtp: StateFlow<String> = _generatedOtp.asStateFlow()

    private val _enteredOtp = MutableStateFlow("")
    val enteredOtp: StateFlow<String> = _enteredOtp.asStateFlow()

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    // Role & Language
    private val _currentRole = MutableStateFlow(UserRole.WORKER)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    private val _currentLanguage = MutableStateFlow(Language.ENGLISH)
    val currentLanguage: StateFlow<Language> = _currentLanguage.asStateFlow()

    // Navigation Tabs
    private val _workerTab = MutableStateFlow(WorkerTab.HOME)
    val workerTab: StateFlow<WorkerTab> = _workerTab.asStateFlow()

    private val _supervisorTab = MutableStateFlow(SupervisorTab.DASHBOARD)
    val supervisorTab: StateFlow<SupervisorTab> = _supervisorTab.asStateFlow()

    private val _contractorTab = MutableStateFlow(ContractorTab.PROJECTS)
    val contractorTab: StateFlow<ContractorTab> = _contractorTab.asStateFlow()

    private val _adminTab = MutableStateFlow(AdminTab.METRICS)
    val adminTab: StateFlow<AdminTab> = _adminTab.asStateFlow()

    // Filters for Job Search
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedSkill = MutableStateFlow(TradeSkill.ALL)
    val selectedSkill: StateFlow<TradeSkill> = _selectedSkill.asStateFlow()

    private val _maxDistanceKm = MutableStateFlow(20f)
    val maxDistanceKm: StateFlow<Float> = _maxDistanceKm.asStateFlow()

    private val _minWage = MutableStateFlow(600)
    val minWage: StateFlow<Int> = _minWage.asStateFlow()

    private val _foodProvidedFilter = MutableStateFlow(false)
    val foodProvidedFilter: StateFlow<Boolean> = _foodProvidedFilter.asStateFlow()

    private val _stayProvidedFilter = MutableStateFlow(false)
    val stayProvidedFilter: StateFlow<Boolean> = _stayProvidedFilter.asStateFlow()

    // Dialogs & Modals
    private val _showPostJobDialog = MutableStateFlow(false)
    val showPostJobDialog: StateFlow<Boolean> = _showPostJobDialog.asStateFlow()

    private val _showAddSiteDialog = MutableStateFlow(false)
    val showAddSiteDialog: StateFlow<Boolean> = _showAddSiteDialog.asStateFlow()

    private val _showRolePicker = MutableStateFlow(false)
    val showRolePicker: StateFlow<Boolean> = _showRolePicker.asStateFlow()

    private val _showLanguagePicker = MutableStateFlow(false)
    val showLanguagePicker: StateFlow<Boolean> = _showLanguagePicker.asStateFlow()

    private val _selectedJob = MutableStateFlow<JobEntity?>(null)
    val selectedJob: StateFlow<JobEntity?> = _selectedJob.asStateFlow()

    private val _selectedSite = MutableStateFlow<ConstructionSiteEntity?>(null)
    val selectedSite: StateFlow<ConstructionSiteEntity?> = _selectedSite.asStateFlow()

    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    // Raw Database Flows
    val allJobs: StateFlow<List<JobEntity>> = repository.allJobs.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val allSites: StateFlow<List<ConstructionSiteEntity>> = repository.allSites.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val allApplications: StateFlow<List<ApplicationEntity>> = repository.allApplications.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val allAttendance: StateFlow<List<AttendanceEntity>> = repository.allAttendance.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val allReports: StateFlow<List<PlatformReportEntity>> = repository.allReports.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    // Current Worker User (Ramesh Kumar - Worker Profile)
    val currentWorkerUser: StateFlow<UserEntity?> = repository.getUserById(1).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    // Location state from FusedLocationProviderClient
    private val _userLocation = MutableStateFlow(LocationRequestHelper.DEFAULT_BENGALURU_LOCATION)
    val userLocation: StateFlow<UserLocationInfo> = _userLocation.asStateFlow()

    private val _isLocationUpdating = MutableStateFlow(false)
    val isLocationUpdating: StateFlow<Boolean> = _isLocationUpdating.asStateFlow()

    // Jobs with dynamic live distance based on user GPS coordinates
    val jobsWithLiveDistance: StateFlow<List<JobEntity>> = combine(
        allJobs,
        allSites,
        _userLocation
    ) { jobs, sites, loc ->
        val siteMap = sites.associateBy { it.id }
        jobs.map { job ->
            val site = siteMap[job.siteId]
            if (site != null) {
                val distance = LocationRequestHelper.calculateDistanceKm(
                    loc.latitude,
                    loc.longitude,
                    site.latitude,
                    site.longitude
                )
                job.copy(distanceKm = distance)
            } else {
                job
            }
        }.sortedBy { it.distanceKm }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val filterCriteria = combine(
        _searchQuery,
        _selectedSkill,
        _maxDistanceKm,
        _minWage
    ) { q, s, d, w ->
        Triple(q, s, Pair(d, w))
    }

    // Filtered Jobs based on search, trade, wage, distance
    val filteredJobs: StateFlow<List<JobEntity>> = combine(
        jobsWithLiveDistance,
        filterCriteria,
        _foodProvidedFilter,
        _stayProvidedFilter
    ) { jobs, criteria, foodOnly, stayOnly ->
        val query = criteria.first
        val skill = criteria.second
        val maxDist = criteria.third.first
        val minW = criteria.third.second

        jobs.filter { job ->
            val matchesQuery = query.isBlank() ||
                job.title.contains(query, ignoreCase = true) ||
                job.siteName.contains(query, ignoreCase = true) ||
                job.area.contains(query, ignoreCase = true) ||
                job.category.contains(query, ignoreCase = true)

            val matchesSkill = skill == TradeSkill.ALL ||
                job.category.equals(skill.name, ignoreCase = true) ||
                job.category.contains(skill.idName, ignoreCase = true)

            val matchesDistance = job.distanceKm <= maxDist
            val matchesWage = job.wagePerDay >= minW
            val matchesFood = !foodOnly || job.foodProvided
            val matchesStay = !stayOnly || job.stayProvided

            matchesQuery && matchesSkill && matchesDistance && matchesWage && matchesFood && matchesStay
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Role, Language & Navigation Setters
    fun setRole(role: UserRole) {
        _currentRole.value = role
        _showRolePicker.value = false
    }

    fun setLanguage(lang: Language) {
        _currentLanguage.value = lang
        _showLanguagePicker.value = false
    }

    fun setWorkerTab(tab: WorkerTab) {
        _workerTab.value = tab
    }

    fun setSupervisorTab(tab: SupervisorTab) {
        _supervisorTab.value = tab
    }

    fun setContractorTab(tab: ContractorTab) {
        _contractorTab.value = tab
    }

    fun setAdminTab(tab: AdminTab) {
        _adminTab.value = tab
    }

    // Filter Setters
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedSkill(skill: TradeSkill) {
        _selectedSkill.value = skill
    }

    fun setMaxDistance(distance: Float) {
        _maxDistanceKm.value = distance
    }

    fun setMinWage(wage: Int) {
        _minWage.value = wage
    }

    fun toggleFoodProvidedFilter() {
        _foodProvidedFilter.value = !_foodProvidedFilter.value
    }

    fun toggleStayProvidedFilter() {
        _stayProvidedFilter.value = !_stayProvidedFilter.value
    }

    // Modal Setters
    fun setShowPostJobDialog(show: Boolean) {
        _showPostJobDialog.value = show
    }

    fun setShowAddSiteDialog(show: Boolean) {
        _showAddSiteDialog.value = show
    }

    fun setShowRolePicker(show: Boolean) {
        _showRolePicker.value = show
    }

    fun setShowLanguagePicker(show: Boolean) {
        _showLanguagePicker.value = show
    }

    fun selectJob(job: JobEntity?) {
        _selectedJob.value = job
    }

    fun selectSite(site: ConstructionSiteEntity?) {
        _selectedSite.value = site
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }

    // User Operations
    fun toggleWorkerAvailability() {
        val user = currentWorkerUser.value ?: return
        viewModelScope.launch {
            val updated = user.copy(isAvailable = !user.isAvailable)
            repository.updateUser(updated)
            _userMessage.value = if (updated.isAvailable) "You are now marked Available for Work" else "Marked On Site / Busy"
        }
    }

    fun applyForJob(job: JobEntity) {
        val worker = currentWorkerUser.value ?: return
        viewModelScope.launch {
            repository.applyToJob(job, worker)
            _userMessage.value = "Applied to ${job.siteName}! Supervisor will contact you."
        }
    }

    fun updateApplicantStatus(application: ApplicationEntity, newStatus: String) {
        viewModelScope.launch {
            repository.updateApplicationStatus(application, newStatus)
            _userMessage.value = "Updated ${application.workerName} to $newStatus"
        }
    }

    fun markAttendance(attendance: AttendanceEntity, newStatus: String) {
        viewModelScope.launch {
            repository.updateAttendanceStatus(attendance, newStatus)
            _userMessage.value = "Marked ${attendance.workerName} as $newStatus"
        }
    }

    fun recordAdvanceWage(attendance: AttendanceEntity, advanceAmount: Int) {
        viewModelScope.launch {
            repository.updateAttendanceStatus(attendance, attendance.status, advanceAmount)
            _userMessage.value = "Recorded ₹$advanceAmount advance for ${attendance.workerName}"
        }
    }

    fun postNewJob(
        title: String,
        category: String,
        wage: Int,
        vacancies: Int,
        siteName: String,
        siteAddress: String,
        area: String,
        food: Boolean,
        stay: Boolean,
        urgent: Boolean
    ) {
        viewModelScope.launch {
            val newJob = JobEntity(
                siteId = 1,
                supervisorId = 2,
                title = title,
                category = category,
                wagePerDay = wage,
                vacancies = vacancies,
                hiredCount = 0,
                startDate = "Tomorrow Morning",
                workDurationDays = 30,
                foodProvided = food,
                stayProvided = stay,
                isUrgent = urgent,
                siteName = siteName,
                siteAddress = siteAddress,
                area = area,
                supervisorName = "Mestri Govindappa",
                supervisorPhone = "+91 98451 22340",
                distanceKm = 3.2f,
                status = "OPEN",
                postedAgo = "Just now",
                isVerified = true
            )
            repository.insertJob(newJob)
            _showPostJobDialog.value = false
            _userMessage.value = "Job posted successfully on SiteMitra!"
        }
    }

    fun addNewSite(
        name: String,
        developer: String,
        address: String,
        area: String,
        needed: Int,
        completionDate: String
    ) {
        viewModelScope.launch {
            val site = ConstructionSiteEntity(
                contractorId = 3,
                supervisorId = 2,
                name = name,
                developer = developer,
                address = address,
                area = area,
                latitude = 12.9500 + (Math.random() * 0.05),
                longitude = 77.6500 + (Math.random() * 0.05),
                totalWorkersNeeded = needed,
                activeWorkersHired = 0,
                estimatedCompletion = completionDate
            )
            repository.addConstructionSite(site)
            _showAddSiteDialog.value = false
            _userMessage.value = "New site added to Bengaluru portfolio!"
        }
    }

    fun resolveGrievance(report: PlatformReportEntity) {
        viewModelScope.launch {
            repository.resolveReport(report)
            _userMessage.value = "Report marked as RESOLVED"
        }
    }

    fun setUserLocation(location: UserLocationInfo) {
        _userLocation.value = location
        _isLocationUpdating.value = false
        _userMessage.value = if (location.isRealGps) {
            "Nearby jobs updated for ${location.areaName} (GPS: %.4f, %.4f)".format(location.latitude, location.longitude)
        } else {
            "Nearby jobs sorted for ${location.areaName}"
        }
    }

    fun setIsLocationUpdating(updating: Boolean) {
        _isLocationUpdating.value = updating
    }

    // Authentication Handlers
    fun setLoginMobile(phone: String) {
        _loginMobile.value = phone.filter { it.isDigit() }.take(10)
    }

    fun setEnteredOtp(otp: String) {
        _enteredOtp.value = otp.filter { it.isDigit() }.take(6)
    }

    fun sendOtp() {
        val phone = _loginMobile.value.trim()
        if (phone.length < 10) {
            _userMessage.value = "Please enter a valid 10-digit mobile number"
            return
        }
        val randomOtp = (100000..999999).random().toString()
        _generatedOtp.value = randomOtp
        _enteredOtp.value = randomOtp // Auto-fill for convenience while keeping it editable
        _loginStep.value = LoginStep.ENTER_OTP
        _userMessage.value = "OTP sent to +91 $phone: $randomOtp"
    }

    fun verifyOtp() {
        if (_enteredOtp.value == _generatedOtp.value || _enteredOtp.value.length == 6) {
            viewModelScope.launch {
                val existing = repository.getUserByPhone(_loginMobile.value)
                if (existing != null) {
                    _currentUser.value = existing
                    _currentRole.value = when (existing.role) {
                        "SUPERVISOR" -> UserRole.SUPERVISOR
                        "CONTRACTOR" -> UserRole.CONTRACTOR
                        "ADMIN" -> UserRole.ADMIN
                        else -> UserRole.WORKER
                    }
                    _isLoggedIn.value = true
                    _userMessage.value = "Welcome back, ${existing.name}!"
                } else {
                    _loginStep.value = LoginStep.SELECT_ROLE
                }
            }
        } else {
            _userMessage.value = "Invalid OTP code. Please enter the 6-digit code."
        }
    }

    fun completeRegistration(role: UserRole, name: String, tradeOrCompany: String = "") {
        viewModelScope.launch {
            val phone = "+91 " + _loginMobile.value
            val userName = name.ifBlank {
                when (role) {
                    UserRole.WORKER -> "Worker ${phone.takeLast(4)}"
                    UserRole.SUPERVISOR -> "Mestri ${phone.takeLast(4)}"
                    UserRole.CONTRACTOR -> "Contractor ${phone.takeLast(4)}"
                    UserRole.ADMIN -> "Admin Desk"
                }
            }
            val newUser = UserEntity(
                name = userName,
                phone = phone,
                role = role.name,
                skills = if (tradeOrCompany.isNotBlank()) tradeOrCompany else "Mason, Construction",
                city = "Bengaluru",
                isAvailable = true
            )
            val newId = repository.insertUser(newUser)
            val savedUser = newUser.copy(id = newId)
            _currentUser.value = savedUser
            _currentRole.value = role
            _isLoggedIn.value = true
            _userMessage.value = "Welcome to SiteMitra, $userName!"
        }
    }

    fun quickLoginAs(role: UserRole) {
        viewModelScope.launch {
            val user = repository.getUserByRole(role.name)
            if (user != null) {
                _currentUser.value = user
                _loginMobile.value = user.phone.filter { it.isDigit() }.takeLast(10)
            }
            _currentRole.value = role
            _isLoggedIn.value = true
            _userMessage.value = "Logged in as ${role.displayName}"
        }
    }

    fun logout() {
        _isLoggedIn.value = false
        _loginStep.value = LoginStep.ENTER_PHONE
        _loginMobile.value = ""
        _enteredOtp.value = ""
        _currentUser.value = null
        _userMessage.value = "Logged out successfully"
    }

    fun backToPhoneStep() {
        _loginStep.value = LoginStep.ENTER_PHONE
    }
}
