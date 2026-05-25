package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device_pairings")
data class DevicePairing(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val deviceName: String,
    val macAddress: String, // Bluetooth MAC Address
    val lastSeen: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val isPaired: Boolean = false
)
