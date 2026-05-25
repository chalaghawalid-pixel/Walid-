package com.example.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.ui.theme.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassCard
import com.example.ui.viewmodel.DiscoveryDevice
import com.example.ui.viewmodel.MainViewModel

@SuppressLint("DefaultLocale")
@Composable
fun ShareScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val translations = viewModel.translations

    val activeProgress by viewModel.transferProgress.collectAsState()
    val isTransferring by viewModel.isTransferring.collectAsState()
    val activeFileName by viewModel.activeTransferFile.collectAsState()
    val selectedFilesState by viewModel.selectedFiles.collectAsState()
    val speedKbps by viewModel.currentTransferSpeed.collectAsState()
    val activeDeviceName by viewModel.activeDeviceName.collectAsState()

    val isScanning by viewModel.isScanning.collectAsState()
    val bondedList by viewModel.bondedDevices.collectAsState()
    val discoveredList by viewModel.discoveredDevices.collectAsState()

    // Requesting Bluetooth runtime permissions
    val bluetoothPermissions = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
        listOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_ADVERTISE
        )
    } else {
        listOf(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    var permissionsGranted by remember {
        mutableStateOf(
            bluetoothPermissions.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { map ->
        val granted = map.values.all { it }
        permissionsGranted = granted
        if (granted) {
            viewModel.loadBondedDevices()
        }
    }

    LaunchedEffect(Unit) {
        if (!permissionsGranted) {
            launcher.launch(bluetoothPermissions.toTypedArray())
        } else {
            viewModel.loadBondedDevices()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Active Transfer Screen Overlay / Overlay Panel
        if (isTransferring) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    bgColor = Color.Black.copy(alpha = 0.4f),
                    borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    borderWidth = 2.dp
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    ) {
                        // High-tech pulse transfer animation
                        ActiveBeamRadarPulse()

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = translations.activeTransfer,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Text(
                            text = "${translations.transferSpeed} ${String.format("%.1f", speedKbps)} ${translations.speedUnit}",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        activeFileName?.let { file ->
                            Text(
                                text = file.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = formatFileSize(file.size),
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.5f)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Progress Indicator
                        LinearProgressIndicator(
                            progress = { activeProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = Color.White.copy(alpha = 0.1f)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "${(activeProgress * 100).toInt()}%",
                            fontSize = 14.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "To: $activeDeviceName",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.7f),
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            }
        } else if (selectedFilesState.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudUpload,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                        modifier = Modifier.size(96.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = translations.selectLanguage,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.05f))
                            .clickable { viewModel.toggleLanguage() }
                            .padding(horizontal = 20.dp, vertical = 6.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = translations.selectFilesFirst,
                        fontSize = 15.sp,
                        color = Color.White.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        } else {
            // Devices Selection Layer
            Text(
                text = "${translations.selectedFilesCount}${selectedFilesState.size} (${formatFileSize(selectedFilesState.sumOf { it.size })})",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Bluetooth Permission or State error
            if (!permissionsGranted) {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    bgColor = AccentPink.copy(alpha = 0.1f),
                    borderColor = AccentPink.copy(alpha = 0.4f)
                ) {
                    Text(text = translations.btPermissionsError, color = Color.White, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    GlassButton(
                        onClick = { launcher.launch(bluetoothPermissions.toTypedArray()) },
                        text = translations.grantBtPermissions
                    )
                }
            } else if (!viewModel.isBluetoothEnabled()) {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    bgColor = AccentPink.copy(alpha = 0.1f),
                    borderColor = AccentPink.copy(alpha = 0.4f)
                ) {
                    Text(text = translations.bluetoothDisabled, color = Color.White, fontSize = 14.sp)
                }
            } else {
                // Radar visual scanner header
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    bgColor = Color.White.copy(alpha = 0.04f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = translations.radarSearching,
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = translations.radarSearchingSub,
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 11.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        GlassButton(
                            onClick = {
                                if (isScanning) {
                                    viewModel.stopBluetoothScanning(context)
                                } else {
                                    viewModel.startBluetoothScanning(context)
                                }
                            },
                            text = if (isScanning) translations.stopScanButton else translations.scanBtButton,
                            gradientColors = if (isScanning) listOf(AccentPink, Color(0xFFC00030)) else listOf(AccentCyan, Color(0xFF007A8A))
                        )
                    }
                }

                // Scrollable Devices list
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (bondedList.isEmpty() && discoveredList.isEmpty() && !isScanning) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 48.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = translations.noDevicesFound,
                                    color = Color.White.copy(alpha = 0.4f),
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    // Bonded (paired) devices section
                    if (bondedList.isNotEmpty()) {
                        item {
                            Text(
                                text = translations.bondedDevices,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                        }

                        items(bondedList, key = { "bonded_" + it.address }) { device ->
                            DeviceRow(device = device) {
                                viewModel.initiateTransfer(context, device)
                            }
                        }
                    }

                    // Discovered New devices section
                    if (discoveredList.isNotEmpty()) {
                        item {
                            Text(
                                text = translations.discoveredDevices,
                                color = MaterialTheme.colorScheme.secondary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(top = 16.dp, bottom = 6.dp)
                            )
                        }

                        items(discoveredList, key = { "discovered_" + it.address }) { device ->
                            DeviceRow(device = device) {
                                viewModel.initiateTransfer(context, device)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DeviceRow(device: DiscoveryDevice, onClick: () -> Unit) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        bgColor = Color.White.copy(alpha = 0.05f)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        brush = Brush.radialGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Bluetooth,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = device.name,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = device.address,
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 12.sp
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.3f),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun ActiveBeamRadarPulse() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulseTransition")

    val pulseScale1 by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 2.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Pulse1"
    )

    val pulseAlpha1 by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Pulse1Alpha"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(120.dp)
    ) {
        // Echo wave ring
        Box(
            modifier = Modifier
                .fillMaxSize(1f)
                .align(Alignment.Center)
                .size((80 * pulseScale1).dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = pulseAlpha1))
        )

        // Native Core orb
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Brush.radialGradient(listOf(AccentCyan, AccentViolet))),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.BluetoothSearching,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
