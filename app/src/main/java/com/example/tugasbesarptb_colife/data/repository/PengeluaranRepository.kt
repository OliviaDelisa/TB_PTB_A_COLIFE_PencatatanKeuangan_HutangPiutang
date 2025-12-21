package com.example.tugasbesarptb_colife.data.repository

import com.example.tugasbesarptb_colife.data.local.dao.PengeluaranDao
import com.example.tugasbesarptb_colife.data.local.entity.Pengeluaran
import kotlinx.coroutines.flow.Flow

class PengeluaranRepository(private val pengeluaranDao: PengeluaranDao) {

    fun getAllPengeluaran(): Flow<List<Pengeluaran>> = pengeluaranDao.getAllPengeluaran()

    // ✅ Tambahkan fungsi untuk mengambil satu item
    fun getPengeluaranById(id: Int): Flow<Pengeluaran?> = pengeluaranDao.getPengeluaranById(id)

    suspend fun insertPengeluaran(pengeluaran: Pengeluaran) {
        pengeluaranDao.insertPengeluaran(pengeluaran)
    }

    suspend fun updatePengeluaran(pengeluaran: Pengeluaran) {
        pengeluaranDao.updatePengeluaran(pengeluaran)
    }

    suspend fun deletePengeluaran(pengeluaran: Pengeluaran) {
        pengeluaranDao.deletePengeluaran(pengeluaran)
    }
}
