package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT * FROM transfer_records ORDER BY timestamp DESC")
    fun getAllTransfers(): Flow<List<TransferRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransfer(record: TransferRecord): Long

    @Query("DELETE FROM transfer_records")
    suspend fun clearAllHistory()

    // Bluetooth Device Pairings / History
    @Query("SELECT * FROM device_pairings ORDER BY lastSeen DESC")
    fun getAllDevices(): Flow<List<DevicePairing>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevice(device: DevicePairing)

    @Query("UPDATE device_pairings SET isFavorite = :isFav WHERE id = :id")
    suspend fun updateDeviceFavorite(id: Int, isFav: Boolean)

    @Query("DELETE FROM device_pairings WHERE id = :id")
    suspend fun deleteDevice(id: Int)
}
