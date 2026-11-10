package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.remote.SiteMitraServerConfig
import com.example.ui.theme.AmberSafety
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.GreenSuccessContainer
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary

@Composable
fun ServerStatusChip(
    isConnected: Boolean,
    latencyMs: Long?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isConnected) GreenSuccessContainer else MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
            .testTag("server_status_chip")
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(if (isConnected) GreenSuccess else AmberSafety)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = if (latencyMs != null) "${latencyMs}ms" else "Cloud",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isConnected) GreenSuccess else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ServerStatusDialog(
    isConnected: Boolean,
    latencyMs: Long?,
    statusMessage: String,
    lastSyncTime: String,
    isPinging: Boolean,
    onPingClick: () -> Unit,
    onSyncClick: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.CloudDone,
                    contentDescription = null,
                    tint = if (isConnected) GreenSuccess else AmberSafety,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Google Cloud Server Connection", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Server Endpoint Card
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = NavyDark)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "SERVER ENDPOINT",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = AmberSafety
                        )
                        Text(
                            text = SiteMitraServerConfig.GOOGLE_SERVER_WEB_URL,
                            color = Color.White,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = SiteMitraServerConfig.SERVER_REGION,
                            color = Color(0xFF94A3B8),
                            fontSize = 10.sp
                        )
                    }
                }

                // Connection metrics
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Status", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (isConnected) GreenSuccess else AmberSafety)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isConnected) "ONLINE" else "CONNECTING",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = if (isConnected) GreenSuccess else AmberSafety
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "Ping Latency", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(
                            text = if (latencyMs != null) "${latencyMs} ms" else "Checking...",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = NavyPrimary
                        )
                    }
                }

                Text(
                    text = statusMessage,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "Last Synced: $lastSyncTime",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Actions inside dialog
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onPingClick,
                        enabled = !isPinging,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        if (isPinging) {
                            CircularProgressIndicator(modifier = Modifier.size(14.dp), strokeWidth = 2.dp)
                        } else {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Ping Server", fontSize = 11.sp)
                        }
                    }

                    Button(
                        onClick = onSyncClick,
                        colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Sync Data", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Open in Web Browser Button
                Button(
                    onClick = {
                        SiteMitraServerConfig.openWebPortal(context)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AmberSafety),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("open_web_portal_button")
                ) {
                    Icon(Icons.Default.OpenInBrowser, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Open Google Server Web Portal", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                // Share Testing Link with Friends Button
                OutlinedButton(
                    onClick = {
                        ExternalActionHelper.shareAppLink(context, SiteMitraServerConfig.GOOGLE_SERVER_WEB_URL)
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("share_testing_link_button")
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Share Testing Link with Friends", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}
