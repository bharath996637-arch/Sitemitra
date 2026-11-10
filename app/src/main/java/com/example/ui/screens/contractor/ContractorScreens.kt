package com.example.ui.screens.contractor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.ui.text.font.FontFamily
import com.example.data.remote.SiteMitraServerConfig
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.ConstructionSiteEntity
import com.example.ui.components.ExternalActionHelper
import com.example.ui.theme.AmberSafety
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.viewmodel.SiteMitraViewModel

@Composable
fun ContractorDashboardScreen(
    viewModel: SiteMitraViewModel,
    modifier: Modifier = Modifier
) {
    val sites by viewModel.allSites.collectAsState()
    val jobs by viewModel.allJobs.collectAsState()
    val attendance by viewModel.allAttendance.collectAsState()
    val showAddDialog by viewModel.showAddSiteDialog.collectAsState()
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) }

    val totalSitesNeeded = sites.sumOf { it.totalWorkersNeeded }
    val totalSitesHired = sites.sumOf { it.activeWorkersHired }
    val fulfillmentPct = if (totalSitesNeeded > 0) ((totalSitesHired.toFloat() / totalSitesNeeded) * 100).toInt() else 0

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("contractor_dashboard_screen")
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Projects (${sites.size})", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Supervisors", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("Wage Outflow", fontWeight = FontWeight.Bold) }
            )
        }

        if (selectedTab == 0) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Contractor Company Card
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = NavyDark),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Reddy Infra & Construction Pvt Ltd",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "Suresh Reddy • Bengaluru Metro Region",
                                        fontSize = 11.sp,
                                        color = Color(0xFF94A3B8)
                                    )
                                }

                                Button(
                                    onClick = { viewModel.setShowAddSiteDialog(true) },
                                    colors = ButtonDefaults.buttonColors(containerColor = AmberSafety),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Add Site", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(text = "Total Sites", fontSize = 11.sp, color = Color(0xFF94A3B8))
                                    Text(text = "${sites.size} Active", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                                Column {
                                    Text(text = "Total Workforce", fontSize = 11.sp, color = Color(0xFF94A3B8))
                                    Text(text = "$totalSitesHired / $totalSitesNeeded", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AmberSafety)
                                }
                                Column {
                                    Text(text = "Fulfillment", fontSize = 11.sp, color = Color(0xFF94A3B8))
                                    Text(text = "$fulfillmentPct%", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GreenSuccess)
                                }
                            }
                        }
                    }
                }

                // Google Cloud Server Enterprise Integration Card
                item {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.CloudDone,
                                        contentDescription = null,
                                        tint = GreenSuccess,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Google Cloud Server Connected",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = NavyPrimary
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = GreenSuccess.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "ONLINE",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GreenSuccess,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = SiteMitraServerConfig.GOOGLE_SERVER_WEB_URL,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { viewModel.syncWithGoogleServer() },
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Sync", fontSize = 11.sp)
                                }

                                Button(
                                    onClick = {
                                        SiteMitraServerConfig.openWebPortal(context)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1.5f)
                                ) {
                                    Icon(Icons.Default.OpenInBrowser, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Web Portal", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Active Construction Sites",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                items(sites) { site ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = site.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    Text(text = site.address, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(AmberSafety)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(text = site.area, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Site Progress
                            val sitePct = (site.activeWorkersHired.toFloat() / site.totalWorkersNeeded.coerceAtLeast(1))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Workforce Deployed: ${site.activeWorkersHired}/${site.totalWorkersNeeded}", fontSize = 11.sp)
                                Text(text = "${(sitePct * 100).toInt()}%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GreenSuccess)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { sitePct },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = NavyPrimary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Estimated: ${site.estimatedCompletion}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                OutlinedButton(
                                    onClick = {
                                        ExternalActionHelper.openMapDirections(
                                            context,
                                            site.latitude,
                                            site.longitude,
                                            site.name
                                        )
                                    },
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("View Map", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        } else if (selectedTab == 1) {
            // Supervisors Roster
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val mestris = listOf(
                    Triple("Mestri Govindappa", "+91 98451 22340", "Nalini Towers & Prestige Enclave"),
                    Triple("Mestri Manjunath", "+91 94480 33419", "Brigade Tech Park Extension"),
                    Triple("Mestri Venkatesh", "+91 97312 88201", "Sobha Dream Acres Block 7"),
                    Triple("Mestri Shiva", "+91 96112 00934", "Purva Marina Palms Hebbal")
                )

                item {
                    Text(text = "Appointed Site Mestris & Supervisors", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                items(mestris) { (name, phone, siteManaged) ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(text = phone, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                                Text(text = "Site: $siteManaged", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                OutlinedButton(
                                    onClick = { ExternalActionHelper.callPhone(context, phone) },
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Call", fontSize = 11.sp)
                                }
                                OutlinedButton(
                                    onClick = { ExternalActionHelper.openWhatsApp(context, phone, "Namaskara Mestri, update needed on today's site labour.") },
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("WhatsApp", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Wage Outflow Reports
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = NavyDark),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "WEEKLY WAGE EXPENDITURE", fontSize = 11.sp, color = AmberSafety, fontWeight = FontWeight.Bold)
                            Text(text = "₹1,42,800", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Disbursed across 6 active Bengaluru construction sites", fontSize = 11.sp, color = Color(0xFF94A3B8))
                        }
                    }
                }

                item {
                    Text(text = "Project-wise Daily Outflow", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                val projectWages = listOf(
                    Pair("Nalini Towers Enclave", "₹38,200 / day (42 workers)"),
                    Pair("Brigade Tech Park Phase 2", "₹48,600 / day (54 workers)"),
                    Pair("Sobha Dream Acres Block 7", "₹41,100 / day (48 workers)"),
                    Pair("Prestige Green Glen", "₹29,800 / day (35 workers)")
                )

                items(projectWages) { (project, outflow) ->
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = project, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Text(text = outflow, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = GreenSuccess)
                        }
                    }
                }
            }
        }

        // Add Site Dialog
        if (showAddDialog) {
            AddSiteDialog(
                viewModel = viewModel,
                onDismiss = { viewModel.setShowAddSiteDialog(false) }
            )
        }
    }
}

@Composable
fun AddSiteDialog(
    viewModel: SiteMitraViewModel,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var developer by remember { mutableStateOf("Reddy Infra & Properties") }
    var address by remember { mutableStateOf("") }
    var area by remember { mutableStateOf("Sarjapur Road") }
    var neededText by remember { mutableStateOf("40") }
    var completionDate by remember { mutableStateOf("Dec 2027") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Register New Construction Site", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Project Name (e.g. Sobha Royal Pavilion)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = developer,
                    onValueChange = { developer = it },
                    label = { Text("Developer / Builder") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = area,
                    onValueChange = { area = it },
                    label = { Text("Bengaluru Area (e.g. Sarjapur)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Site Address / Landmark") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = neededText,
                    onValueChange = { neededText = it },
                    label = { Text("Total Workers Needed") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = completionDate,
                    onValueChange = { completionDate = it },
                    label = { Text("Estimated Completion") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val finalName = name.ifBlank { "New Construction Project - $area" }
                    val needed = neededText.toIntOrNull() ?: 35
                    viewModel.addNewSite(
                        name = finalName,
                        developer = developer,
                        address = if (address.isNotBlank()) address else "$area Main Road, Bengaluru",
                        area = area,
                        needed = needed,
                        completionDate = completionDate
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
            ) {
                Text("Add Project")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
