package com.example.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassCard
import com.example.ui.viewmodel.LocalFile
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("DefaultLocale")
@Composable
fun FilesScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val translations = viewModel.translations

    var activeCategory by remember { mutableStateOf("PHOTO") }
    val selectedFilesState by viewModel.selectedFiles.collectAsState()

    // Query result lists from ViewModel
    val photosList by viewModel.mediaPhotos.collectAsState()
    val videosList by viewModel.mediaVideos.collectAsState()
    val docsList by viewModel.mediaDocs.collectAsState()
    val appsList by viewModel.mediaApps.collectAsState()

    // Real system storage picker
    val fileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        uris.forEach { uri ->
            viewModel.addSelectedFileFromUri(context, uri)
        }
    }

    // Permission runner to scan device's MediaStore
    val readStoragePermission = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.scanLocalMediaStore(context)
        }
    }

    // Proactively scan MediaStore if permitted
    LaunchedEffect(Unit) {
        val hasPermission = ContextCompat.checkSelfPermission(context, readStoragePermission) == PackageManager.PERMISSION_GRANTED
        if (hasPermission) {
            viewModel.scanLocalMediaStore(context)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Upper Action Bar with File Selection Summary
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            bgColor = Color.White.copy(alpha = 0.05f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = translations.galleryHeader,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                    Text(
                        text = "${translations.selectedFilesCount}${selectedFilesState.size}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row {
                    // Manual File Picker Button
                    IconButton(
                        onClick = { fileLauncher.launch("*/*") },
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Default.FileOpen,
                            contentDescription = translations.pickRealFiles,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Scanner activation button
                    IconButton(
                        onClick = {
                            val hasPermission = ContextCompat.checkSelfPermission(context, readStoragePermission) == PackageManager.PERMISSION_GRANTED
                            if (hasPermission) {
                                viewModel.scanLocalMediaStore(context)
                            } else {
                                permissionLauncher.launch(readStoragePermission)
                            }
                        },
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = translations.scanRealMediaStore,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }

        // Horizontal Category Select tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val categories = listOf(
                Triple("PHOTO", Icons.Default.Image, translations.filesCategoryPhotos),
                Triple("VIDEO", Icons.Default.Videocam, translations.filesCategoryVideos),
                Triple("APP", Icons.Default.Android, translations.filesCategoryApps),
                Triple("DOCUMENT", Icons.Default.Description, translations.filesCategoryDocuments)
            )

            categories.forEach { (catID, icon, label) ->
                val isActive = activeCategory == catID
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 2.dp)
                        .background(
                            color = if (isActive) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { activeCategory = catID }
                        .padding(vertical = 10.dp, horizontal = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            tint = if (isActive) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.6f),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = label,
                            fontSize = 11.sp,
                            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                            color = if (isActive) Color.White else Color.White.copy(alpha = 0.6f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        // Active List of Query/Pick files
        val displayFiles = when (activeCategory) {
            "PHOTO" -> photosList
            "VIDEO" -> videosList
            "DOCUMENT" -> docsList
            "APP" -> appsList
            else -> emptyList()
        }.filter { it.category == activeCategory } + selectedFilesState.filter { it.category == activeCategory }

        val uniqueFiles = displayFiles.distinctBy { it.uriString }

        if (uniqueFiles.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Inbox,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.2f),
                        modifier = Modifier.size(72.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = translations.noFilesInCategory,
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uniqueFiles, key = { it.uriString }) { file ->
                    val isSelected = selectedFilesState.any { s -> s.uriString == file.uriString }

                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItemPlacement(),
                        bgColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.04f),
                        borderColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.4f) else Color.White.copy(alpha = 0.08f),
                        onClick = {
                            viewModel.selectedFiles.value = if (isSelected) {
                                selectedFilesState.filterNot { s -> s.uriString == file.uriString }.toSet()
                            } else {
                                selectedFilesState + file
                            }
                        }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = when (file.category) {
                                    "PHOTO" -> Icons.Default.Image
                                    "VIDEO" -> Icons.Default.Videocam
                                    "APP" -> Icons.Default.Android
                                    else -> Icons.Default.Description
                                },
                                contentDescription = null,
                                tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.6f),
                                modifier = Modifier.size(36.dp)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = file.name,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${translations.sizeLabel}${formatFileSize(file.size)}",
                                    color = Color.White.copy(alpha = 0.5f),
                                    fontSize = 12.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Icon(
                                imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Outlined.CheckCircle,
                                contentDescription = null,
                                tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.2f),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// Format utility to display human-readable byte sizes
@SuppressLint("DefaultLocale")
fun formatFileSize(bytes: Long): String {
    if (bytes <= 0) return "0 Bytes"
    val units = arrayOf("Bytes", "KB", "MB", "GB")
    var digitGroups = 0
    var sizeNew = bytes.toDouble()
    while (sizeNew >= 1024 && digitGroups < units.size - 1) {
        sizeNew /= 1024
        digitGroups++
    }
    return String.format("%.2f %s", sizeNew, units[digitGroups])
}
