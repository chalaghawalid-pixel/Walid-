package com.example.ui.screens

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Podcasts
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassCard
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentPink
import com.example.ui.theme.AccentViolet
import com.example.ui.viewmodel.MainViewModel

@Composable
fun ReceiveScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val translations = viewModel.translations

    val discoverableLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        Toast.makeText(context, "Discoverability enabled!", Toast.LENGTH_SHORT).show()
    }

    val isTransferring by viewModel.isTransferring.collectAsState()
    val transferProgress by viewModel.transferProgress.collectAsState()
    val activeFile by viewModel.activeTransferFile.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isTransferring && activeFile != null) {
            // Incoming transfer progress view
            GlassCard(
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    ActiveBeamRadarPulse()

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = if (viewModel.isArabic.value) "جاري استقبال الملف..." else "Receiving File...",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = activeFile!!.name,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 4.dp),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LinearProgressIndicator(
                        progress = { transferProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = Color.White.copy(alpha = 0.1f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${(transferProgress * 100).toInt()}%",
                        fontSize = 14.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        } else {
            // Idle beacon listening view
            val infiniteTransition = rememberInfiniteTransition(label = "beaconAnimation")
            val pulseSize by infiniteTransition.animateFloat(
                initialValue = 180f,
                targetValue = 240f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "PulseBeacon"
            )

            val pulseAlpha by infiniteTransition.animateFloat(
                initialValue = 0.3f,
                targetValue = 0.05f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "PulseAlpha"
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(260.dp)
            ) {
                // Background ripple
                Box(
                    modifier = Modifier
                        .size(pulseSize.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary.copy(alpha = pulseAlpha))
                )

                // High intensity inner background
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(AccentViolet.copy(alpha = 0.5f), Color.Transparent)
                            )
                        )
                )

                // Core beacon bubble
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(AccentViolet, AccentPink)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Podcasts,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(42.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = if (viewModel.isArabic.value) "يتم الآن الاستماع لبث أورا شير" else "Listening for AuraShare Beams",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (viewModel.isArabic.value) "تأكد من تفعيل البلوتوث لكي يتمكن المرسلون القريبون من اكتشاف جهازك وفحصه." else "Ensure your Bluetooth is enabled so nearby senders can discover and scan your device.",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.5f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Real Discoverability Activation Button
            GlassButton(
                onClick = {
                    val discoverIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
                        putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120)
                    }
                    try {
                        discoverableLauncher.launch(discoverIntent)
                    } catch (e: SecurityException) {
                        Toast.makeText(context, "Permission missing for discoverability", Toast.LENGTH_SHORT).show()
                    }
                },
                text = if (viewModel.isArabic.value) "اجعل الجهاز مرئياً" else "Make Device Discoverable",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                },
                gradientColors = listOf(AccentViolet, AccentPink)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Wi-Fi Hotspot sharing panel (تفعيل نقطة الاتصال)
            GlassCard(
                modifier = Modifier.fillMaxWidth(0.95f),
                bgColor = Color.White.copy(alpha = 0.04f),
                borderColor = AccentCyan.copy(alpha = 0.30f)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Wifi,
                            contentDescription = translations.hotspotCardTitle,
                            tint = AccentCyan,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = translations.hotspotCardTitle,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = translations.hotspotCardSub,
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.65f),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    GlassButton(
                        onClick = {
                            val intent = Intent().apply {
                                action = "android.settings.TETHER_SETTINGS"
                            }
                            try {
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                val fallbackIntent = Intent().apply {
                                    action = android.provider.Settings.ACTION_WIRELESS_SETTINGS
                                }
                                try {
                                    context.startActivity(fallbackIntent)
                                } catch (ex: Exception) {
                                    Toast.makeText(context, "Cannot open hotspot settings.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        text = translations.hotspotBtn,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Wifi,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        gradientColors = listOf(AccentCyan, AccentViolet)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Instructions tips
            GlassCard(
                modifier = Modifier.fillMaxWidth(0.95f),
                bgColor = Color.White.copy(alpha = 0.03f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Tips info",
                        tint = AccentCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (viewModel.isArabic.value)
                            "تأكد من إبقاء الأجهزة قريبة ومفعلة لضمان أقصى سرعة ممكنة لنقل ومزامنة الملفات لاسلكياً."
                            else "Both devices must be paired or have Bluetooth/Hotspot interface up to perform direct high speed wireless transfers.",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.62f)
                    )
                }
            }
        }
    }
}
