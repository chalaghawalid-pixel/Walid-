package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transfer_records")
data class TransferRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fileName: String,
    val fileSize: Long,
    val fileType: String, // PHOTO, VIDEO, APP, DOCUMENT
    val direction: String, // SEND, RECEIVE
    val status: String, // COMPLETED, FAILED, IN_PROGRESS
    val speedKbps: Float, // speed in Kb/s over Bluetooth
    val deviceName: String,
    val timestamp: Long = System.currentTimeMillis()
)
