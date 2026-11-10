package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.ConstructionSiteEntity
import com.example.ui.theme.AmberSafety
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.util.LocationRequestHelper
import com.example.util.UserLocationInfo

@Composable
fun LiveConstructionMap(
    sites: List<ConstructionSiteEntity>,
    selectedSite: ConstructionSiteEntity?,
    onSiteSelected: (ConstructionSiteEntity?) -> Unit,
    userLocation: UserLocationInfo = LocationRequestHelper.DEFAULT_BENGALURU_LOCATION,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Pulsing radar animation
    val infiniteTransition = rememberInfiniteTransition(label = "RadarPulse")
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "PulseRadius"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "PulseAlpha"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NavyDark)
    ) {
        // Interactive Canvas drawing Bengaluru City Grid & Sites
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(sites) {
                    detectTapGestures { tapOffset ->
                        val centerX = size.width / 2f
                        val centerY = size.height / 2f
                        val radiusScale = (size.width.coerceAtMost(size.height) / 2f) * 0.85f

                        // Find closest site within 40dp tap radius
                        val tappedSite = sites.minByOrNull { site ->
                            // Normalize coordinates around Bengaluru center (12.9716, 77.5946)
                            val normX = ((site.longitude - 77.68) / 0.15).toFloat()
                            val normY = -((site.latitude - 12.95) / 0.15).toFloat()
                            val siteX = centerX + normX * radiusScale
                            val siteY = centerY + normY * radiusScale

                            val dx = tapOffset.x - siteX
                            val dy = tapOffset.y - siteY
                            kotlin.math.sqrt(dx * dx + dy * dy)
                        }

                        if (tappedSite != null) {
                            val normX = ((tappedSite.longitude - 77.68) / 0.15).toFloat()
                            val normY = -((tappedSite.latitude - 12.95) / 0.15).toFloat()
                            val siteX = centerX + normX * radiusScale
                            val siteY = centerY + normY * radiusScale
                            val dx = tapOffset.x - siteX
                            val dy = tapOffset.y - siteY
                            val dist = kotlin.math.sqrt(dx * dx + dy * dy)
                            if (dist < 100f) {
                                onSiteSelected(tappedSite)
                            }
                        }
                    }
                }
        ) {
            val centerX = size.width / 2f
            val centerY = size.height / 2f
            val maxR = (size.width.coerceAtMost(size.height) / 2f) * 0.85f

            // Radar concentric distance rings (5km, 10km, 20km)
            val ringColors = Color(0xFF1E293B)
            val dashedStroke = Stroke(
                width = 2f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 10f), 0f)
            )

            drawCircle(color = ringColors, radius = maxR * 0.35f, center = Offset(centerX, centerY), style = dashedStroke)
            drawCircle(color = ringColors, radius = maxR * 0.70f, center = Offset(centerX, centerY), style = dashedStroke)
            drawCircle(color = ringColors, radius = maxR * 1.00f, center = Offset(centerX, centerY), style = dashedStroke)

            // Grid lines
            drawLine(
                color = Color(0x33334155),
                start = Offset(centerX, 0f),
                end = Offset(centerX, size.height),
                strokeWidth = 1.5f
            )
            drawLine(
                color = Color(0x33334155),
                start = Offset(0f, centerY),
                end = Offset(size.width, centerY),
                strokeWidth = 1.5f
            )

            // Pulsing live radar wave
            drawCircle(
                color = Color(0xFF3B82F6).copy(alpha = pulseAlpha),
                radius = maxR * pulseRadius,
                center = Offset(centerX, centerY),
                style = Stroke(width = 3f)
            )

            // Center User Location Beacon (Whitefield / Central Bengaluru)
            drawCircle(
                color = Color(0xFF60A5FA).copy(alpha = 0.3f),
                radius = 28f,
                center = Offset(centerX, centerY)
            )
            drawCircle(
                color = Color(0xFF2563EB),
                radius = 12f,
                center = Offset(centerX, centerY)
            )
            drawCircle(
                color = Color.White,
                radius = 5f,
                center = Offset(centerX, centerY)
            )

            // Draw Site Pins
            sites.forEach { site ->
                val normX = ((site.longitude - 77.68) / 0.15).toFloat()
                val normY = -((site.latitude - 12.95) / 0.15).toFloat()
                val siteX = centerX + normX * maxR
                val siteY = centerY + normY * maxR

                val isSelected = selectedSite?.id == site.id

                // Glow ring
                if (isSelected) {
                    drawCircle(
                        color = AmberSafety.copy(alpha = 0.5f),
                        radius = 28f,
                        center = Offset(siteX, siteY)
                    )
                }

                // Pin circle
                drawCircle(
                    color = if (isSelected) AmberSafety else Color(0xFFF59E0B),
                    radius = if (isSelected) 16f else 11f,
                    center = Offset(siteX, siteY)
                )
                drawCircle(
                    color = Color(0xFF1E3A8A),
                    radius = if (isSelected) 8f else 5f,
                    center = Offset(siteX, siteY)
                )
            }
        }

        // Overlay: Map Legend & Distance Indicators
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = Color(0xDD0F172A),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2563EB))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "You: " + userLocation.areaName + if (userLocation.isRealGps) " (GPS)" else "",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(AmberSafety)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${sites.size} Active Sites",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Selected Site Bottom Card
        if (selectedSite != null) {
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(AmberSafety)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = selectedSite.area.uppercase(),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = selectedSite.status,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GreenSuccess
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = selectedSite.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = selectedSite.address,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        IconButton(onClick = { onSiteSelected(null) }) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Developer", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = selectedSite.developer, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Column {
                            Text(text = "Workers Needed", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                text = "${selectedSite.activeWorkersHired} / ${selectedSite.totalWorkersNeeded}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmberSafety
                            )
                        }
                        Column {
                            Text(text = "Completion", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = selectedSite.estimatedCompletion, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                ExternalActionHelper.openMapDirections(
                                    context,
                                    selectedSite.latitude,
                                    selectedSite.longitude,
                                    selectedSite.name
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.NearMe, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Navigate to Site", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = {
                                ExternalActionHelper.callPhone(context, "+91 98451 22340")
                            },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Call, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Call Mestri", color = NavyPrimary, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}
