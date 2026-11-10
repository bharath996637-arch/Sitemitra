package com.example.ui.screens.worker

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.platform.LocalContext
import com.example.util.LocationRequestHelper
import com.example.util.UserLocationInfo
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.entity.JobEntity
import com.example.data.model.Language
import com.example.data.model.LocaleStrings
import com.example.data.model.TradeSkill
import com.example.ui.components.JobCard
import com.example.ui.theme.AmberSafety
import com.example.ui.theme.AmberSafetyContainer
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.GreenSuccessContainer
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.RedUrgent
import com.example.ui.viewmodel.SiteMitraViewModel

@Composable
fun WorkerHomeScreen(
    viewModel: SiteMitraViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val language by viewModel.currentLanguage.collectAsState()
    val jobs by viewModel.jobsWithLiveDistance.collectAsState()
    val applications by viewModel.allApplications.collectAsState()
    val user by viewModel.currentWorkerUser.collectAsState()
    val userLocation by viewModel.userLocation.collectAsState()
    val isLocationUpdating by viewModel.isLocationUpdating.collectAsState()

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (fineGranted || coarseGranted) {
            viewModel.setIsLocationUpdating(true)
            LocationRequestHelper.requestCurrentLocation(
                context = context,
                onSuccess = { loc -> viewModel.setUserLocation(loc) },
                onError = { viewModel.setIsLocationUpdating(false) }
            )
        }
    }

    val appliedJobIds = applications.map { it.jobId }.toSet()
    val urgentJobs = jobs.filter { it.isUrgent }
    val highWageJobs = jobs.filter { it.wagePerDay >= 1000 }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("worker_home_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Banner with Bengaluru Illustration
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hero_banner"),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.site_banner_1794287711182),
                        contentDescription = "Bengaluru Construction Sites",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(175.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(175.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color(0xDD0F172A))
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(14.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = AmberSafety
                        ) {
                            Text(
                                text = "BENGALURU PILOT LAUNCH",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Find Genuine Construction Work",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                        Text(
                            text = "${jobs.size} Active Construction Openings Near Whitefield & E-City",
                            color = Color(0xFFE2E8F0),
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Availability Status Bar
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (user?.isAvailable == true) GreenSuccessContainer else MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(if (user?.isAvailable == true) GreenSuccess else Color.Gray)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = if (user?.isAvailable == true) LocaleStrings.get("available_status", language) else LocaleStrings.get("on_site_status", language),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (user?.isAvailable == true) GreenSuccess else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (user?.isAvailable == true) "Supervisors can contact you directly" else "Status hidden from active recruiters",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Switch(
                        checked = user?.isAvailable == true,
                        onCheckedChange = { viewModel.toggleWorkerAvailability() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = GreenSuccess,
                            checkedTrackColor = GreenSuccessContainer
                        )
                    )
                }
            }
        }

        // Nearby Jobs GPS Location Bar
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("nearby_jobs_location_card")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(if (userLocation.isRealGps) GreenSuccessContainer else AmberSafetyContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = if (userLocation.isRealGps) "🎯" else "📍", fontSize = 16.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Nearby Jobs in " + userLocation.areaName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                if (userLocation.isRealGps) {
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(GreenSuccessContainer)
                                            .padding(horizontal = 5.dp, vertical = 1.dp)
                                    ) {
                                        Text("LIVE GPS", fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = GreenSuccess)
                                    }
                                }
                            }
                            Text(
                                text = if (userLocation.isRealGps)
                                    "GPS: %.3f, %.3f • Auto-sorted by closest distance".format(userLocation.latitude, userLocation.longitude)
                                else
                                    "Tap to detect live GPS & calculate site distance",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    if (isLocationUpdating) {
                        androidx.compose.material3.CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.5.dp,
                            color = NavyPrimary
                        )
                    } else {
                        Button(
                            onClick = {
                                if (LocationRequestHelper.hasLocationPermission(context)) {
                                    viewModel.setIsLocationUpdating(true)
                                    LocationRequestHelper.requestCurrentLocation(
                                        context = context,
                                        onSuccess = { loc -> viewModel.setUserLocation(loc) },
                                        onError = { viewModel.setIsLocationUpdating(false) }
                                    )
                                } else {
                                    locationPermissionLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        )
                                    )
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (userLocation.isRealGps) MaterialTheme.colorScheme.surfaceVariant else NavyPrimary,
                                contentColor = if (userLocation.isRealGps) MaterialTheme.colorScheme.onSurfaceVariant else Color.White
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("detect_location_button")
                        ) {
                            Text(
                                text = if (userLocation.isRealGps) "Refresh GPS" else "Detect GPS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // Quick Trades Selector
        item {
            Column {
                Text(
                    text = LocaleStrings.get("filter_skill", language),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(TradeSkill.values()) { trade ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surface,
                            shadowElevation = 1.dp,
                            modifier = Modifier
                                .clickable {
                                    viewModel.setSelectedSkill(trade)
                                    viewModel.setWorkerTab(com.example.ui.viewmodel.WorkerTab.FIND_WORK)
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = trade.icon, fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = LocaleStrings.getTradeName(trade, language),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }

        // Urgent Hiring Section
        if (urgentJobs.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.ElectricBolt,
                            contentDescription = null,
                            tint = RedUrgent,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = LocaleStrings.get("urgent_hiring", language),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = RedUrgent
                        )
                    }
                    Text(
                        text = "${urgentJobs.size} sites hiring now",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            items(urgentJobs) { job ->
                JobCard(
                    job = job,
                    language = language,
                    hasApplied = appliedJobIds.contains(job.id),
                    onApply = { viewModel.applyForJob(job) }
                )
            }
        }

        // High Wage Openings (₹1,000+ / day)
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "High Daily Wage (₹1,000+ / day)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "All verified",
                    fontSize = 12.sp,
                    color = GreenSuccess,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        items(highWageJobs) { job ->
            JobCard(
                job = job,
                language = language,
                hasApplied = appliedJobIds.contains(job.id),
                onApply = { viewModel.applyForJob(job) }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FindWorkScreen(
    viewModel: SiteMitraViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.currentLanguage.collectAsState()
    val filteredJobs by viewModel.filteredJobs.collectAsState()
    val applications by viewModel.allApplications.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedSkill by viewModel.selectedSkill.collectAsState()
    val maxDistance by viewModel.maxDistanceKm.collectAsState()
    val minWage by viewModel.minWage.collectAsState()
    val foodFilter by viewModel.foodProvidedFilter.collectAsState()
    val stayFilter by viewModel.stayProvidedFilter.collectAsState()
    val userLocation by viewModel.userLocation.collectAsState()

    val appliedJobIds = applications.map { it.jobId }.toSet()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("find_work_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Search bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = { Text("Search by trade, site name or Bengaluru area...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("job_search_input")
            )
        }

        // Filter: Skills Horizontal Row
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(TradeSkill.values()) { trade ->
                    val isSelected = trade == selectedSkill
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.setSelectedSkill(trade) },
                        label = { Text(LocaleStrings.getTradeName(trade, language), fontSize = 12.sp) },
                        leadingIcon = { Text(trade.icon, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NavyPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Distance & Wage quick sliders
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = AmberSafety,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Origin: " + userLocation.areaName,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = NavyPrimary
                            )
                        }
                        if (userLocation.isRealGps) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(GreenSuccessContainer)
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text("LIVE GPS", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = GreenSuccess)
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${LocaleStrings.get("filter_distance", language)}: ${maxDistance.toInt()} km",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${LocaleStrings.get("filter_wage", language)}: ₹$minWage/day",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = GreenSuccess
                        )
                    }

                    Slider(
                        value = maxDistance,
                        onValueChange = { viewModel.setMaxDistance(it) },
                        valueRange = 2f..25f,
                        steps = 22,
                        colors = SliderDefaults.colors(
                            thumbColor = NavyPrimary,
                            activeTrackColor = NavyPrimary
                        )
                    )

                    // Food and Stay Toggles
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = foodFilter,
                            onClick = { viewModel.toggleFoodProvidedFilter() },
                            label = { Text("🍲 " + LocaleStrings.get("food_provided", language), fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AmberSafety,
                                selectedLabelColor = Color.White
                            )
                        )

                        FilterChip(
                            selected = stayFilter,
                            onClick = { viewModel.toggleStayProvidedFilter() },
                            label = { Text("🏠 " + LocaleStrings.get("stay_provided", language), fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AmberSafety,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }

        // Results count
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${filteredJobs.size} Jobs Found",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Sorted by proximity & urgency",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Job Cards
        items(filteredJobs) { job ->
            JobCard(
                job = job,
                language = language,
                hasApplied = appliedJobIds.contains(job.id),
                onApply = { viewModel.applyForJob(job) }
            )
        }

        if (filteredJobs.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🔍", fontSize = 36.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No jobs matching current filters",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "Try increasing the distance slider or resetting trade filter",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                viewModel.setSelectedSkill(TradeSkill.ALL)
                                viewModel.setMaxDistance(25f)
                                viewModel.setMinWage(600)
                                viewModel.setSearchQuery("")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
                        ) {
                            Text("Reset All Filters")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MyJobsScreen(
    viewModel: SiteMitraViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.currentLanguage.collectAsState()
    val applications by viewModel.allApplications.collectAsState()
    val attendance by viewModel.allAttendance.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("my_jobs_screen")
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Applied (${applications.size})", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Attendance & Earnings", fontWeight = FontWeight.Bold) }
            )
        }

        if (selectedTab == 0) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(applications) { app ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = app.jobTitle,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    modifier = Modifier.weight(1f)
                                )
                                val (statusColor, containerColor) = when (app.status) {
                                    "HIRED" -> GreenSuccess to GreenSuccessContainer
                                    "SHORTLISTED" -> AmberSafety to AmberSafetyContainer
                                    else -> NavyPrimary to MaterialTheme.colorScheme.primaryContainer
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(containerColor)
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = app.status,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = statusColor
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Site: ${app.siteName}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Daily Wage: ₹${app.wagePerDay}/day",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = GreenSuccess
                                )
                                Text(
                                    text = "Applied: ${app.appliedDate}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // Attendance and Wage Earnings Tab
            val totalEarnings = attendance.sumOf {
                when (it.status) {
                    "PRESENT" -> it.dailyWage
                    "HALF_DAY" -> it.dailyWage / 2
                    else -> 0
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = NavyDark),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "TOTAL WAGES LOGGED",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmberSafety
                            )
                            Text(
                                text = "₹$totalEarnings",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Based on verified site muster roll attendance in Bengaluru",
                                fontSize = 11.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = "Daily Roll Call History",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                items(attendance) { att ->
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = att.date, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(text = att.siteName, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                if (att.notes.isNotBlank()) {
                                    Text(text = "Note: ${att.notes}", fontSize = 10.sp, color = MaterialTheme.colorScheme.primary)
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                val statusText = when (att.status) {
                                    "PRESENT" -> "PRESENT (₹${att.dailyWage})"
                                    "HALF_DAY" -> "HALF DAY (₹${att.dailyWage / 2})"
                                    else -> "ABSENT (₹0)"
                                }
                                val statusColor = when (att.status) {
                                    "PRESENT" -> GreenSuccess
                                    "HALF_DAY" -> AmberSafety
                                    else -> RedUrgent
                                }
                                Text(text = statusText, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = statusColor)
                                if (att.advancePaid > 0) {
                                    Text(text = "Advance: -₹${att.advancePaid}", fontSize = 10.sp, color = AmberSafety)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WorkerProfileScreen(
    viewModel: SiteMitraViewModel,
    modifier: Modifier = Modifier
) {
    val user by viewModel.currentWorkerUser.collectAsState()
    val language by viewModel.currentLanguage.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("worker_profile_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Worker Identity Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = NavyDark),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(NavyPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "👷", fontSize = 28.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = user?.name ?: "Ramesh Kumar",
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                Icons.Default.Verified,
                                contentDescription = "Verified",
                                tint = GreenSuccess,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = user?.phone ?: "+91 98450 78210",
                            fontSize = 12.sp,
                            color = Color(0xFF94A3B8)
                        )
                        Text(
                            text = "Bengaluru City Pilot Registered Worker",
                            fontSize = 11.sp,
                            color = AmberSafety
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "Rating", fontSize = 11.sp, color = Color(0xFF94A3B8))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = AmberSafety, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(text = "${user?.rating ?: 4.9} / 5.0", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }

                    Column {
                        Text(text = "Experience", fontSize = 11.sp, color = Color(0xFF94A3B8))
                        Text(text = "${user?.experienceYears ?: 6} Years", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }

                    Column {
                        Text(text = "Expected Wage", fontSize = 11.sp, color = Color(0xFF94A3B8))
                        Text(text = "₹${user?.expectedWage ?: 950}/day", color = GreenSuccess, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }

                    Column {
                        Text(text = "Jobs Done", fontSize = 11.sp, color = Color(0xFF94A3B8))
                        Text(text = "${user?.completedJobsCount ?: 28} Sites", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }

        // Skills & Trades
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Trade & Specializations",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Brick Laying", "Vitrified Tiles", "Plastering", "Column Concrete").forEach { skill ->
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = skill,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // Mandatory Safety Equipment Checklist (India Construction Compliance)
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Safety Gear Checklist (PPE)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = "Required for entry on all Bengaluru RERA construction sites",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))

                val safetyItems = listOf(
                    "🪖 Hard Hat / Safety Helmet" to true,
                    "🥾 Steel Toe Safety Boots" to true,
                    "🦺 High-Visibility Reflective Vest" to true,
                    "🧤 Heavy Duty Grip Gloves" to true,
                    "🥽 Dust & Eye Protection Goggles" to true
                )

                safetyItems.forEach { (item, verified) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = item, fontSize = 12.sp)
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Verified",
                            tint = GreenSuccess,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        // Language & Switch Profile
        Button(
            onClick = { viewModel.setShowLanguagePicker(true) },
            colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Change Language / ಭಾಷೆ ಬದಲಿಸಿ (${language.nativeLabel})")
        }

        OutlinedButton(
            onClick = { viewModel.setShowRolePicker(true) },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Switch Role (Supervisor / Contractor / Admin)")
        }

        OutlinedButton(
            onClick = { viewModel.logout() },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = RedUrgent),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("logout_button")
        ) {
            Text("Log Out / Switch Mobile Number (ಲಾಗ್ ಔಟ್)", fontWeight = FontWeight.SemiBold)
        }
    }
}
