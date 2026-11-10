package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LocaleStrings
import com.example.data.model.UserRole
import com.example.ui.components.LanguagePickerSheet
import com.example.ui.components.LiveConstructionMap
import com.example.ui.components.RolePickerSheet
import com.example.ui.components.SiteMitraTopBar
import com.example.ui.screens.auth.MobileLoginScreen
import com.example.ui.screens.admin.AdminDashboardScreen
import com.example.ui.screens.contractor.ContractorDashboardScreen
import com.example.ui.screens.supervisor.PostJobDialog
import com.example.ui.screens.supervisor.SupervisorApplicantsScreen
import com.example.ui.screens.supervisor.SupervisorAttendanceScreen
import com.example.ui.screens.supervisor.SupervisorDashboardScreen
import com.example.ui.screens.worker.FindWorkScreen
import com.example.ui.screens.worker.MyJobsScreen
import com.example.ui.screens.worker.WorkerHomeScreen
import com.example.ui.screens.worker.WorkerProfileScreen
import com.example.ui.theme.AmberSafety
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.SiteMitraTheme
import com.example.ui.viewmodel.AdminTab
import com.example.ui.viewmodel.ContractorTab
import com.example.ui.viewmodel.SiteMitraViewModel
import com.example.ui.viewmodel.SupervisorTab
import com.example.ui.viewmodel.WorkerTab

class MainActivity : ComponentActivity() {

    private val viewModel: SiteMitraViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SiteMitraTheme {
                val isLoggedIn by viewModel.isLoggedIn.collectAsState()
                val showLanguagePicker by viewModel.showLanguagePicker.collectAsState()
                val currentLanguage by viewModel.currentLanguage.collectAsState()
                val userMessage by viewModel.userMessage.collectAsState()
                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(userMessage) {
                    userMessage?.let { msg ->
                        snackbarHostState.showSnackbar(msg)
                        viewModel.clearUserMessage()
                    }
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    if (!isLoggedIn) {
                        MobileLoginScreen(viewModel = viewModel)
                    } else {
                        SiteMitraApp(viewModel = viewModel)
                    }

                    SnackbarHost(
                        hostState = snackbarHostState,
                        modifier = Modifier.align(androidx.compose.ui.Alignment.BottomCenter)
                    )

                    if (showLanguagePicker) {
                        LanguagePickerSheet(
                            currentLanguage = currentLanguage,
                            onLanguageSelected = { viewModel.setLanguage(it) },
                            onDismiss = { viewModel.setShowLanguagePicker(false) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SiteMitraApp(viewModel: SiteMitraViewModel) {
    val currentRole by viewModel.currentRole.collectAsState()
    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val workerTab by viewModel.workerTab.collectAsState()
    val supervisorTab by viewModel.supervisorTab.collectAsState()
    val contractorTab by viewModel.contractorTab.collectAsState()
    val adminTab by viewModel.adminTab.collectAsState()

    val showRolePicker by viewModel.showRolePicker.collectAsState()
    val showLanguagePicker by viewModel.showLanguagePicker.collectAsState()
    val showPostJobDialog by viewModel.showPostJobDialog.collectAsState()
    val userMessage by viewModel.userMessage.collectAsState()

    val sites by viewModel.allSites.collectAsState()
    val selectedSite by viewModel.selectedSite.collectAsState()
    val userLocation by viewModel.userLocation.collectAsState()

    // Back handling: if in sub-tab, go back to main Home
    BackHandler(enabled = currentRole == UserRole.WORKER && workerTab != WorkerTab.HOME) {
        viewModel.setWorkerTab(WorkerTab.HOME)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SiteMitraTopBar(
                role = currentRole,
                language = currentLanguage,
                onRoleClick = { viewModel.setShowRolePicker(true) },
                onLanguageClick = { viewModel.setShowLanguagePicker(true) }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                modifier = Modifier.testTag("main_bottom_nav")
            ) {
                when (currentRole) {
                    UserRole.WORKER -> {
                        NavigationBarItem(
                            selected = workerTab == WorkerTab.HOME,
                            onClick = { viewModel.setWorkerTab(WorkerTab.HOME) },
                            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                            label = { Text(LocaleStrings.get("tab_home", currentLanguage), fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = NavyPrimary,
                                selectedTextColor = NavyPrimary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                        NavigationBarItem(
                            selected = workerTab == WorkerTab.FIND_WORK,
                            onClick = { viewModel.setWorkerTab(WorkerTab.FIND_WORK) },
                            icon = { Icon(Icons.Default.Search, contentDescription = "Find Work") },
                            label = { Text(LocaleStrings.get("tab_find_work", currentLanguage), fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = NavyPrimary,
                                selectedTextColor = NavyPrimary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                        NavigationBarItem(
                            selected = workerTab == WorkerTab.LIVE_MAP,
                            onClick = { viewModel.setWorkerTab(WorkerTab.LIVE_MAP) },
                            icon = { Icon(Icons.Default.Map, contentDescription = "Map") },
                            label = { Text(LocaleStrings.get("tab_live_map", currentLanguage), fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = NavyPrimary,
                                selectedTextColor = NavyPrimary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                        NavigationBarItem(
                            selected = workerTab == WorkerTab.MY_JOBS,
                            onClick = { viewModel.setWorkerTab(WorkerTab.MY_JOBS) },
                            icon = { Icon(Icons.Default.Work, contentDescription = "My Jobs") },
                            label = { Text(LocaleStrings.get("tab_my_jobs", currentLanguage), fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = NavyPrimary,
                                selectedTextColor = NavyPrimary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                        NavigationBarItem(
                            selected = workerTab == WorkerTab.PROFILE,
                            onClick = { viewModel.setWorkerTab(WorkerTab.PROFILE) },
                            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                            label = { Text(LocaleStrings.get("tab_profile", currentLanguage), fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = NavyPrimary,
                                selectedTextColor = NavyPrimary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }

                    UserRole.SUPERVISOR -> {
                        NavigationBarItem(
                            selected = supervisorTab == SupervisorTab.DASHBOARD,
                            onClick = { viewModel.setSupervisorTab(SupervisorTab.DASHBOARD) },
                            icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                            label = { Text("Dashboard", fontSize = 11.sp) }
                        )
                        NavigationBarItem(
                            selected = supervisorTab == SupervisorTab.APPLICANTS,
                            onClick = { viewModel.setSupervisorTab(SupervisorTab.APPLICANTS) },
                            icon = { Icon(Icons.Default.Groups, contentDescription = "Applicants") },
                            label = { Text("Applicants", fontSize = 11.sp) }
                        )
                        NavigationBarItem(
                            selected = supervisorTab == SupervisorTab.ATTENDANCE,
                            onClick = { viewModel.setSupervisorTab(SupervisorTab.ATTENDANCE) },
                            icon = { Icon(Icons.Default.Checklist, contentDescription = "Attendance") },
                            label = { Text("Muster Roll", fontSize = 11.sp) }
                        )
                    }

                    UserRole.CONTRACTOR -> {
                        NavigationBarItem(
                            selected = contractorTab == ContractorTab.PROJECTS,
                            onClick = { viewModel.setContractorTab(ContractorTab.PROJECTS) },
                            icon = { Icon(Icons.Default.Apartment, contentDescription = "Projects") },
                            label = { Text("Sites", fontSize = 11.sp) }
                        )
                        NavigationBarItem(
                            selected = contractorTab == ContractorTab.WORKFORCE,
                            onClick = { viewModel.setContractorTab(ContractorTab.WORKFORCE) },
                            icon = { Icon(Icons.Default.Group, contentDescription = "Workforce") },
                            label = { Text("Supervisors", fontSize = 11.sp) }
                        )
                        NavigationBarItem(
                            selected = contractorTab == ContractorTab.REPORTS,
                            onClick = { viewModel.setContractorTab(ContractorTab.REPORTS) },
                            icon = { Icon(Icons.Default.Assessment, contentDescription = "Reports") },
                            label = { Text("Wages", fontSize = 11.sp) }
                        )
                    }

                    UserRole.ADMIN -> {
                        NavigationBarItem(
                            selected = adminTab == AdminTab.METRICS,
                            onClick = { viewModel.setAdminTab(AdminTab.METRICS) },
                            icon = { Icon(Icons.Default.Dashboard, contentDescription = "Targets") },
                            label = { Text("Pilot KPI", fontSize = 11.sp) }
                        )
                        NavigationBarItem(
                            selected = adminTab == AdminTab.MODERATION,
                            onClick = { viewModel.setAdminTab(AdminTab.MODERATION) },
                            icon = { Icon(Icons.Default.Security, contentDescription = "Grievances") },
                            label = { Text("Moderation", fontSize = 11.sp) }
                        )
                        NavigationBarItem(
                            selected = adminTab == AdminTab.GRIEVANCES,
                            onClick = { viewModel.setAdminTab(AdminTab.GRIEVANCES) },
                            icon = { Icon(Icons.Default.Business, contentDescription = "Expansion") },
                            label = { Text("Roadmap", fontSize = 11.sp) }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (currentRole == UserRole.SUPERVISOR) {
                FloatingActionButton(
                    onClick = { viewModel.setShowPostJobDialog(true) },
                    containerColor = AmberSafety,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Post Job")
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentRole) {
                UserRole.WORKER -> {
                    when (workerTab) {
                        WorkerTab.HOME -> WorkerHomeScreen(viewModel = viewModel)
                        WorkerTab.FIND_WORK -> FindWorkScreen(viewModel = viewModel)
                        WorkerTab.LIVE_MAP -> LiveConstructionMap(
                            sites = sites,
                            selectedSite = selectedSite,
                            onSiteSelected = { viewModel.selectSite(it) },
                            userLocation = userLocation
                        )
                        WorkerTab.MY_JOBS -> MyJobsScreen(viewModel = viewModel)
                        WorkerTab.PROFILE -> WorkerProfileScreen(viewModel = viewModel)
                    }
                }

                UserRole.SUPERVISOR -> {
                    when (supervisorTab) {
                        SupervisorTab.DASHBOARD -> SupervisorDashboardScreen(viewModel = viewModel)
                        SupervisorTab.APPLICANTS -> SupervisorApplicantsScreen(viewModel = viewModel)
                        SupervisorTab.ATTENDANCE -> SupervisorAttendanceScreen(viewModel = viewModel)
                        SupervisorTab.PROFILE -> WorkerProfileScreen(viewModel = viewModel)
                    }
                }

                UserRole.CONTRACTOR -> {
                    ContractorDashboardScreen(viewModel = viewModel)
                }

                UserRole.ADMIN -> {
                    AdminDashboardScreen(viewModel = viewModel)
                }
            }
        }
    }

    // Role Picker Modal
    if (showRolePicker) {
        RolePickerSheet(
            currentRole = currentRole,
            onRoleSelected = { viewModel.setRole(it) },
            onDismiss = { viewModel.setShowRolePicker(false) }
        )
    }

    // Post Job Dialog
    if (showPostJobDialog) {
        PostJobDialog(
            viewModel = viewModel,
            onDismiss = { viewModel.setShowPostJobDialog(false) }
        )
    }
}
