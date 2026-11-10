package com.example.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Language
import com.example.data.model.LocaleStrings
import com.example.data.model.UserRole
import com.example.data.remote.SiteMitraServerConfig
import com.example.ui.components.ServerStatusChip
import com.example.ui.theme.AmberSafety
import com.example.ui.theme.AmberSafetyContainer
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.GreenSuccessContainer
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.viewmodel.LoginStep
import com.example.ui.viewmodel.SiteMitraViewModel

@Composable
fun MobileLoginScreen(
    viewModel: SiteMitraViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.currentLanguage.collectAsState()
    val loginStep by viewModel.loginStep.collectAsState()
    val mobileNumber by viewModel.loginMobile.collectAsState()
    val enteredOtp by viewModel.enteredOtp.collectAsState()
    val generatedOtp by viewModel.generatedOtp.collectAsState()
    val serverIsConnected by viewModel.serverIsConnected.collectAsState()
    val serverLatencyMs by viewModel.serverLatencyMs.collectAsState()

    var newUserName by remember { mutableStateOf("") }
    var newUserSkillOrCompany by remember { mutableStateOf("") }
    var selectedRoleForNewUser by remember { mutableStateOf(UserRole.WORKER) }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag("mobile_login_screen"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar: Language Selector & Bengaluru Pilot Badge & Google Cloud Server Chip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (loginStep != LoginStep.ENTER_PHONE) {
                    IconButton(
                        onClick = { viewModel.backToPhoneStep() },
                        modifier = Modifier.testTag("login_back_button")
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(AmberSafetyContainer)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "🇮🇳 BENGALURU PILOT",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AmberSafety
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ServerStatusChip(
                        isConnected = serverIsConnected,
                        latencyMs = serverLatencyMs,
                        onClick = { viewModel.setShowServerDialog(true) }
                    )

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.clickable { viewModel.setShowLanguagePicker(true) }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Language,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = NavyPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = language.nativeLabel,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = NavyPrimary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // App Brand Header
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(NavyPrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🏗️", fontSize = 36.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "SiteMitra",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NavyPrimary
            )
            Text(
                text = LocaleStrings.get("tagline", language),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Step Content
            when (loginStep) {
                LoginStep.ENTER_PHONE -> {
                    EnterPhoneStep(
                        mobileNumber = mobileNumber,
                        language = language,
                        onMobileChanged = { viewModel.setLoginMobile(it) },
                        onSendOtp = { viewModel.sendOtp() },
                        onQuickLogin = { role -> viewModel.quickLoginAs(role) },
                        onServerDialogClick = { viewModel.setShowServerDialog(true) }
                    )
                }

                LoginStep.ENTER_OTP -> {
                    EnterOtpStep(
                        mobileNumber = mobileNumber,
                        enteredOtp = enteredOtp,
                        generatedOtp = generatedOtp,
                        language = language,
                        onOtpChanged = { viewModel.setEnteredOtp(it) },
                        onVerify = { viewModel.verifyOtp() },
                        onResend = { viewModel.sendOtp() }
                    )
                }

                LoginStep.SELECT_ROLE -> {
                    SelectRoleStep(
                        selectedRole = selectedRoleForNewUser,
                        userName = newUserName,
                        userSkillOrCompany = newUserSkillOrCompany,
                        language = language,
                        onRoleSelected = { selectedRoleForNewUser = it },
                        onNameChanged = { newUserName = it },
                        onSkillOrCompanyChanged = { newUserSkillOrCompany = it },
                        onComplete = {
                            viewModel.completeRegistration(
                                role = selectedRoleForNewUser,
                                name = newUserName,
                                tradeOrCompany = newUserSkillOrCompany
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Trust & Free Launch Banner
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Icon(
                    Icons.Default.Shield,
                    contentDescription = null,
                    tint = GreenSuccess,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "100% Free Pilot Launch • Direct Contact with Verified Sites",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun EnterPhoneStep(
    mobileNumber: String,
    language: Language,
    onMobileChanged: (String) -> Unit,
    onSendOtp: () -> Unit,
    onQuickLogin: (UserRole) -> Unit,
    onServerDialogClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Mobile Login / ನೋಂದಣಿ",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Enter your 10-digit mobile number to log in or register",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 18.dp)
            )

            // Phone Input Field with Indian Flag & +91 Prefix
            OutlinedTextField(
                value = mobileNumber,
                onValueChange = onMobileChanged,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                leadingIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 12.dp, end = 6.dp)
                    ) {
                        Text(text = "🇮🇳", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "+91",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(20.dp)
                                .background(MaterialTheme.colorScheme.outline)
                        )
                    }
                },
                placeholder = { Text("98450 78210") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("mobile_number_input")
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Get OTP Button
            Button(
                onClick = onSendOtp,
                enabled = mobileNumber.length >= 10,
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("send_otp_button")
            ) {
                Text(
                    text = "Get OTP (ಮುಂದುವರಿಯಿರಿ)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Quick Persona Presets for Immediate Marketplace Demonstration
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Demo Accounts (One-Tap Instant Login)",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(10.dp))

        val presets = listOf(
            Triple(UserRole.WORKER, "👷 Worker (Ramesh - Mason)", "+91 98450 78210"),
            Triple(UserRole.SUPERVISOR, "👷‍♂️ Supervisor (Govindappa - Mestri)", "+91 98451 22340"),
            Triple(UserRole.CONTRACTOR, "🏢 Contractor (Suresh Reddy - Builder)", "+91 99002 44551"),
            Triple(UserRole.ADMIN, "🛡️ Admin (SiteMitra Desk)", "+91 80 4012 8899")
        )

        presets.forEach { (role, label, phone) ->
            OutlinedButton(
                onClick = { onQuickLogin(role) },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .testTag("quick_login_${role.name.lowercase()}")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = label,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = phone,
                        fontSize = 11.sp,
                        color = NavyPrimary,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Google Cloud Server Connection Badge Card
        Card(
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onServerDialogClick() }
                .testTag("login_server_card")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.CloudDone,
                    contentDescription = null,
                    tint = GreenSuccess,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Connected Google Server",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyPrimary
                    )
                    Text(
                        text = SiteMitraServerConfig.GOOGLE_SERVER_SHORT_HOST,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
                Text(
                    text = "Status >",
                    fontSize = 10.sp,
                    color = NavyPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun EnterOtpStep(
    mobileNumber: String,
    enteredOtp: String,
    generatedOtp: String,
    language: Language,
    onOtpChanged: (String) -> Unit,
    onVerify: () -> Unit,
    onResend: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Enter 6-Digit OTP",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Verification code sent to +91 $mobileNumber",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 14.dp)
            )

            // OTP Hint Chip
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = AmberSafetyContainer,
                modifier = Modifier.clickable { onOtpChanged(generatedOtp) }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "⚡ Demo OTP: ", fontSize = 12.sp, color = AmberSafety, fontWeight = FontWeight.Bold)
                    Text(text = generatedOtp, fontSize = 13.sp, color = AmberSafety, fontWeight = FontWeight.ExtraBold, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "(Tap to fill)", fontSize = 10.sp, color = AmberSafety)
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // OTP Input
            OutlinedTextField(
                value = enteredOtp,
                onValueChange = onOtpChanged,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = { Text("000000", letterSpacing = 8.sp) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("otp_input")
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Verify Button
            Button(
                onClick = onVerify,
                enabled = enteredOtp.length == 6,
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("verify_otp_button")
            ) {
                Text(
                    text = "Verify & Continue",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(onClick = onResend) {
                Text("Didn't receive code? Resend OTP", fontSize = 12.sp, color = NavyPrimary)
            }
        }
    }
}

@Composable
private fun SelectRoleStep(
    selectedRole: UserRole,
    userName: String,
    userSkillOrCompany: String,
    language: Language,
    onRoleSelected: (UserRole) -> Unit,
    onNameChanged: (String) -> Unit,
    onSkillOrCompanyChanged: (String) -> Unit,
    onComplete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Select Account Type",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "How would you like to use SiteMitra?",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
            )

            val roleOptions = listOf(
                Pair(UserRole.WORKER, "Find daily wage construction jobs, track attendance & earnings"),
                Pair(UserRole.SUPERVISOR, "Post vacancies, hire skilled labor, and log daily muster roll"),
                Pair(UserRole.CONTRACTOR, "Manage multiple sites, supervisors & weekly wage outflow")
            )

            roleOptions.forEach { (role, desc) ->
                val isSelected = role == selectedRole
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onRoleSelected(role) }
                        .testTag("select_account_${role.name.lowercase()}"),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = role.badge, fontSize = 24.sp, modifier = Modifier.padding(end = 10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = role.displayName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = desc,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (isSelected) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Name Input
            OutlinedTextField(
                value = userName,
                onValueChange = onNameChanged,
                label = { Text("Your Full Name (e.g. Ramesh Kumar)") },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("user_name_input")
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Trade or Company Input
            val labelText = when (selectedRole) {
                UserRole.WORKER -> "Your Primary Trade (e.g. Mason, Electrician)"
                UserRole.SUPERVISOR -> "Site Specialization (e.g. Residential, RCC)"
                UserRole.CONTRACTOR -> "Construction Company (e.g. Reddy Infra)"
                UserRole.ADMIN -> "Department"
            }
            OutlinedTextField(
                value = userSkillOrCompany,
                onValueChange = onSkillOrCompanyChanged,
                label = { Text(labelText) },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = onComplete,
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("complete_registration_button")
            ) {
                Text(
                    text = "Get Started",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}
