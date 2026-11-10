package com.example.ui.screens.admin

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Verified
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import com.example.data.remote.SiteMitraServerConfig
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberSafety
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.GreenSuccessContainer
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.RedUrgent
import com.example.ui.theme.RedUrgentContainer
import com.example.ui.viewmodel.SiteMitraViewModel

@Composable
fun AdminDashboardScreen(
    viewModel: SiteMitraViewModel,
    modifier: Modifier = Modifier
) {
    val jobs by viewModel.allJobs.collectAsState()
    val sites by viewModel.allSites.collectAsState()
    val reports by viewModel.allReports.collectAsState()
    val serverIsConnected by viewModel.serverIsConnected.collectAsState()
    val serverLatencyMs by viewModel.serverLatencyMs.collectAsState()
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("admin_dashboard_screen")
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Pilot Targets", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Grievances (${reports.size})", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("Expansion Plan", fontWeight = FontWeight.Bold) }
            )
        }

        if (selectedTab == 0) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = NavyDark),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Security, contentDescription = null, tint = AmberSafety, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "SiteMitra Admin & Launch Oversight", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Pilot Phase: Bengaluru • 90-Day Marketplace Targets", fontSize = 12.sp, color = Color(0xFF94A3B8))
                        }
                    }
                }

                // Google Cloud Infrastructure Status Card
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
                                        tint = if (serverIsConnected) GreenSuccess else AmberSafety,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Google Cloud Server (Run)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = NavyPrimary
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (serverIsConnected) GreenSuccess.copy(alpha = 0.15f) else AmberSafety.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = if (serverLatencyMs != null) "${serverLatencyMs}ms ONLINE" else "ONLINE",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (serverIsConnected) GreenSuccess else AmberSafety,
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
                                    onClick = { viewModel.pingGoogleServer() },
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Ping Server", fontSize = 11.sp)
                                }

                                Button(
                                    onClick = {
                                        SiteMitraServerConfig.openWebPortal(context)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = AmberSafety),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1.5f)
                                ) {
                                    Icon(Icons.Default.OpenInBrowser, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Open Web Portal", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                // 4 Core 90-Day KPI Targets from Blueprint
                item {
                    Text(text = "First 90-Day Pilot Goals (Bengaluru)", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                val targets = listOf(
                    Triple("Registered Workers", "428 / 500", 0.856f),
                    Triple("Active Mestris & Supervisors", "44 / 50", 0.88f),
                    Triple("Registered Contractors", "18 / 20", 0.90f),
                    Triple("Genuine Job Listings", "86 / 100", 0.86f)
                )

                items(targets) { (name, progressText, ratio) ->
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = name, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                Text(text = progressText, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = GreenSuccess)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { ratio },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = NavyPrimary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                    }
                }

                // Moderation Status
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Site Verification & Safety Standard", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                item {
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Verified, contentDescription = null, tint = GreenSuccess, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "100% Zero-Commission Guarantee for Workers", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Workers are never charged to apply or accept construction jobs. All listed wages are verified with supervisors before publishing.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        } else if (selectedTab == 1) {
            // Grievances & Moderation
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(text = "Worker Grievances & Job Moderation Reports", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(text = "Safety equipment issues, wage delays, or site disputes", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                items(reports) { rep ->
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
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Flag, contentDescription = null, tint = RedUrgent, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = rep.category, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }

                                val isResolved = rep.status == "RESOLVED"
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isResolved) GreenSuccessContainer else RedUrgentContainer)
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = rep.status,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isResolved) GreenSuccess else RedUrgent
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = rep.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Reported by: ${rep.reportedBy} • Target: ${rep.againstEntity}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            if (rep.status != "RESOLVED") {
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = { viewModel.resolveGrievance(rep) },
                                    colors = ButtonDefaults.buttonColors(containerColor = GreenSuccess),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Mark as Resolved & Notify Worker", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Expansion Roadmap from Blueprint
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(text = "All-India Staged Expansion Strategy", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(text = "Proven pilot in Bengaluru first, followed by state-by-state rollouts:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                val stages = listOf(
                    Pair("Pilot Stage (Live)", "Bengaluru • 500 Workers, 50 Supervisors, 20 Contractors"),
                    Pair("Phase 2", "Hyderabad & Chennai • Expand across South India IT & Infra corridors"),
                    Pair("Phase 3", "Vijayawada, Visakhapatnam & Pune • Industrial & Residential clusters"),
                    Pair("Phase 4", "Mumbai, Delhi NCR & Ahmedabad • Tier 1 Metro Mega Projects"),
                    Pair("Phase 5", "All Major Indian Cities • Pan-India Construction Marketplace")
                )

                items(stages) { (stage, desc) ->
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "🇮🇳", fontSize = 24.sp, modifier = Modifier.padding(end = 12.dp))
                            Column {
                                Text(text = stage, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NavyPrimary)
                                Text(text = desc, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
    }
}
