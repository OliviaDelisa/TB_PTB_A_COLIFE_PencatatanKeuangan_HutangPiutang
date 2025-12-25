package com.example.tugasbesarptb_colife.data.repository

import androidx.lifecycle.LiveData
import com.example.tugasbesarptb_colife.data.local.entity.KategoriPengeluaran
import com.example.tugasbesarptb_colife.data.local.dao.KategoriPengeluaranDao

class KategoriRepository(private val kategoriPengeluaranDao: KategoriPengeluaranDao) {

    val allKategori: LiveData<List<KategoriPengeluaran>> = kategoriPengeluaranDao.getAllKategori()

    suspend fun insert(kategori: KategoriPengeluaran) {
        kategoriPengeluaranDao.insertKategori(kategori)
    }
}