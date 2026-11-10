package com.example.ui.screens.supervisor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.data.local.entity.ApplicationEntity
import com.example.data.local.entity.AttendanceEntity
import com.example.data.local.entity.JobEntity
import com.example.data.model.Language
import com.example.data.model.LocaleStrings
import com.example.data.model.TradeSkill
import com.example.ui.components.ExternalActionHelper
import com.example.ui.theme.AmberSafety
import com.example.ui.theme.AmberSafetyContainer
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.GreenSuccessContainer
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.RedUrgent
import com.example.ui.theme.RedUrgentContainer
import com.example.ui.viewmodel.SiteMitraViewModel

@Composable
fun SupervisorDashboardScreen(
    viewModel: SiteMitraViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.currentLanguage.collectAsState()
    val jobs by viewModel.allJobs.collectAsState()
    val applications by viewModel.allApplications.collectAsState()
    val attendance by viewModel.allAttendance.collectAsState()

    val totalNeeded = jobs.sumOf { it.vacancies }
    val totalHired = jobs.sumOf { it.hiredCount }
    val pendingApplications = applications.filter { it.status == "APPLIED" }.size

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("supervisor_dashboard_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Mestri Header Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NavyDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Mestri Govindappa (ಸೈಟ್ ಮೇಸ್ತ್ರಿ)",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            )
                            Text(
                                text = "Site: Nalini Towers & Brigade Extension, Bengaluru",
                                color = Color(0xFF94A3B8),
                                fontSize = 11.sp
                            )
                        }

                        Button(
                            onClick = { viewModel.setShowPostJobDialog(true) },
                            colors = ButtonDefaults.buttonColors(containerColor = AmberSafety),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("post_job_btn")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Post Job", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Hiring Progress Bar
                    val progress = if (totalNeeded > 0) totalHired.toFloat() / totalNeeded.toFloat() else 0f
                    Text(
                        text = "Workforce Fulfillment: $totalHired / $totalNeeded Workers (${(progress * 100).toInt()}%)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = GreenSuccess,
                        trackColor = Color(0xFF334155),
                    )
                }
            }
        }

        // Summary Metric Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = "Active Jobs",
                    value = "${jobs.size}",
                    icon = Icons.Default.Work,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "New Applicants",
                    value = "$pendingApplications",
                    icon = Icons.Default.Groups,
                    containerColor = AmberSafetyContainer,
                    contentColor = AmberSafety,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Today Roll Call",
                    value = "${attendance.size}",
                    icon = Icons.Default.Payments,
                    containerColor = GreenSuccessContainer,
                    contentColor = GreenSuccess,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Active Job Postings List for this Mestri
        item {
            Text(
                text = "Your Active Site Vacancies",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        items(jobs) { job ->
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
                            Text(text = job.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(
                                text = "${job.siteName} • ${job.area}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "₹${job.wagePerDay}/day",
                            fontWeight = FontWeight.ExtraBold,
                            color = GreenSuccess,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Status: ${job.hiredCount} of ${job.vacancies} Hired",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (job.hiredCount >= job.vacancies) GreenSuccess else AmberSafety
                        )

                        Text(
                            text = "Posted: ${job.postedAgo}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SupervisorApplicantsScreen(
    viewModel: SiteMitraViewModel,
    modifier: Modifier = Modifier
) {
    val applications by viewModel.allApplications.collectAsState()
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("supervisor_applicants_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "Worker Applications (${applications.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = "Review, shortlist, and hire genuine Bengaluru workers directly",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

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
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = app.workerName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text(
                                text = "Skill: ${app.workerSkill} • ${app.workerExperience} yrs experience",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Applied for: ${app.jobTitle}",
                                fontSize = 11.sp,
                                color = NavyPrimary,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        val (statusColor, containerColor) = when (app.status) {
                            "HIRED" -> GreenSuccess to GreenSuccessContainer
                            "SHORTLISTED" -> AmberSafety to AmberSafetyContainer
                            "REJECTED" -> RedUrgent to RedUrgentContainer
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
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = statusColor
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Contact and Decision Actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Call Worker
                        OutlinedButton(
                            onClick = { ExternalActionHelper.callPhone(context, app.workerPhone) },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Call Worker", fontSize = 11.sp)
                        }

                        // WhatsApp Worker
                        OutlinedButton(
                            onClick = {
                                ExternalActionHelper.openWhatsApp(
                                    context,
                                    app.workerPhone,
                                    "Namaskara ${app.workerName}, regarding your application for ${app.jobTitle} on SiteMitra."
                                )
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("💬 WhatsApp", fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (app.status != "HIRED") {
                            Button(
                                onClick = { viewModel.updateApplicantStatus(app, "HIRED") },
                                colors = ButtonDefaults.buttonColors(containerColor = GreenSuccess),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Hire Worker", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }

                        if (app.status == "APPLIED") {
                            OutlinedButton(
                                onClick = { viewModel.updateApplicantStatus(app, "SHORTLISTED") },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Shortlist", fontSize = 11.sp)
                            }
                        }

                        if (app.status != "REJECTED") {
                            OutlinedButton(
                                onClick = { viewModel.updateApplicantStatus(app, "REJECTED") },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = RedUrgent)
                            ) {
                                Text("Reject", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SupervisorAttendanceScreen(
    viewModel: SiteMitraViewModel,
    modifier: Modifier = Modifier
) {
    val attendanceList by viewModel.allAttendance.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("supervisor_attendance_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = NavyDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Daily Muster Roll & Wage Log",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "Mark Present / Absent / Half-day to log wages for hired site crew",
                        color = Color(0xFF94A3B8),
                        fontSize = 11.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }

        items(attendanceList) { att ->
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
                        Column {
                            Text(text = att.workerName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(
                                text = "Trade: ${att.skill} • Site: ${att.siteName}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Text(
                            text = "₹${att.dailyWage}/day",
                            fontWeight = FontWeight.Bold,
                            color = GreenSuccess,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Roll call buttons: Present, Half-Day, Absent
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.markAttendance(att, "PRESENT") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (att.status == "PRESENT") GreenSuccess else MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = if (att.status == "PRESENT") Color.White else MaterialTheme.colorScheme.onSurface
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Present", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { viewModel.markAttendance(att, "HALF_DAY") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (att.status == "HALF_DAY") AmberSafety else MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = if (att.status == "HALF_DAY") Color.White else MaterialTheme.colorScheme.onSurface
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Half Day", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { viewModel.markAttendance(att, "ABSENT") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (att.status == "ABSENT") RedUrgent else MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = if (att.status == "ABSENT") Color.White else MaterialTheme.colorScheme.onSurface
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Absent", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PostJobDialog(
    viewModel: SiteMitraViewModel,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedTrade by remember { mutableStateOf(TradeSkill.MASON) }
    var wageText by remember { mutableStateOf("950") }
    var vacanciesText by remember { mutableStateOf("5") }
    var siteName by remember { mutableStateOf("Nalini Towers Enclave") }
    var area by remember { mutableStateOf("Whitefield") }
    var foodProvided by remember { mutableStateOf(true) }
    var stayProvided by remember { mutableStateOf(false) }
    var isUrgent by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Post Construction Job Vacancy", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Job Title (e.g. 5 Masons Needed for Slab)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Trade Category
                Text(text = "Trade Category", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                LazyColumn(
                    modifier = Modifier.height(110.dp)
                ) {
                    items(TradeSkill.values().filter { it != TradeSkill.ALL }) { trade ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedTrade = trade }
                                .padding(vertical = 4.dp)
                        ) {
                            Text(text = trade.icon, fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = trade.idName,
                                fontSize = 12.sp,
                                fontWeight = if (trade == selectedTrade) FontWeight.Bold else FontWeight.Normal,
                                color = if (trade == selectedTrade) NavyPrimary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = wageText,
                        onValueChange = { wageText = it },
                        label = { Text("Daily Wage (₹)") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = vacanciesText,
                        onValueChange = { vacanciesText = it },
                        label = { Text("Workers Needed") },
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = siteName,
                    onValueChange = { siteName = it },
                    label = { Text("Site / Project Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = area,
                    onValueChange = { area = it },
                    label = { Text("Bengaluru Area (e.g. Whitefield)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Checkboxes
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = foodProvided, onCheckedChange = { foodProvided = it })
                    Text("Food Provided (Canteen / Allowance)", fontSize = 12.sp)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = stayProvided, onCheckedChange = { stayProvided = it })
                    Text("Stay / Labour Sheds Provided", fontSize = 12.sp)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isUrgent, onCheckedChange = { isUrgent = it })
                    Text("Urgent Hiring (Shows on Home Radar)", fontSize = 12.sp, color = RedUrgent, fontWeight = FontWeight.Bold)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val wage = wageText.toIntOrNull() ?: 900
                    val vacancies = vacanciesText.toIntOrNull() ?: 4
                    val finalTitle = title.ifBlank { "Urgent: $vacancies ${selectedTrade.idName}s Needed" }
                    viewModel.postNewJob(
                        title = finalTitle,
                        category = selectedTrade.idName,
                        wage = wage,
                        vacancies = vacancies,
                        siteName = siteName,
                        siteAddress = "$area Main Road, Bengaluru",
                        area = area,
                        food = foodProvided,
                        stay = stayProvided,
                        urgent = isUrgent
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
            ) {
                Text("Publish Job")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = contentColor)
            Text(text = title, fontSize = 10.sp, color = contentColor.copy(alpha = 0.8f))
        }
    }
}
