package com.example.tugasbesarptb_colife.data.repository

import androidx.lifecycle.LiveData
import com.example.tugasbesarptb_colife.data.local.dao.PengeluaranDao
import com.example.tugasbesarptb_colife.data.local.dao.PengeluaranWithKategori
import com.example.tugasbesarptb_colife.data.local.entity.Pengeluaran
import com.example.tugasbesarptb_colife.model.ServerResponse
import com.example.tugasbesarptb_colife.network.ApiClient
import retrofit2.Response

class PengeluaranRepository(private val pengeluaranDao: PengeluaranDao) {

    val allPengeluaran: LiveData<List<PengeluaranWithKategori>> = pengeluaranDao.getAllPengeluaranWithKategori()

    suspend fun insert(pengeluaran: Pengeluaran) {
        pengeluaranDao.insertPengeluaran(pengeluaran)
    }

    suspend fun update(pengeluaran: Pengeluaran) {
        pengeluaranDao.updatePengeluaran(pengeluaran)
    }

    suspend fun delete(pengeluaran: Pengeluaran) {
        pengeluaranDao.deletePengeluaran(pengeluaran)
    }

    fun getPengeluaranById(id: Int): LiveData<Pengeluaran> {
        return pengeluaranDao.getPengeluaranById(id)
    }

    suspend fun sendPengeluaranToServer(pengeluaran: Pengeluaran): Response<ServerResponse> {
        return ApiClient.instance.postPengeluaran(pengeluaran)
    }
}