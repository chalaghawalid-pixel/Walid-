package com.example.data

import kotlinx.coroutines.flow.Flow

class DataRepository(private val appDao: AppDao) {
    val allTransfers: Flow<List<TransferRecord>> = appDao.getAllTransfers()
    val allDevices: Flow<List<DevicePairing>> = appDao.getAllDevices()

    suspend fun insertTransfer(record: TransferRecord): Long {
        return appDao.insertTransfer(record)
    }

    suspend fun clearAllHistory() {
        appDao.clearAllHistory()
    }

    suspend fun insertDevice(device: DevicePairing) {
        appDao.insertDevice(device)
    }

    suspend fun updateDeviceFavorite(id: Int, isFav: Boolean) {
        appDao.updateDeviceFavorite(id, isFav)
    }

    suspend fun deleteDevice(id: Int) {
        appDao.deleteDevice(id)
    }
}
