package com.example.tugasbesarptb_colife.data.repository

import androidx.lifecycle.LiveData
import com.example.tugasbesarptb_colife.data.local.entity.Pemasukan
import com.example.tugasbesarptb_colife.data.local.dao.PemasukanDao

class  PemasukanRepository(private val pemasukanDao: PemasukanDao) {

    val allPemasukan: LiveData<List<Pemasukan>> = pemasukanDao.getAllPemasukan()

    suspend fun insert(pemasukan: Pemasukan) {
        pemasukanDao.insertPemasukan(pemasukan)
    }

    suspend fun update(pemasukan: Pemasukan) {
        pemasukanDao.updatePemasukan(pemasukan)
    }

    suspend fun delete(pemasukan: Pemasukan) {
        pemasukanDao.deletePemasukan(pemasukan)
    }

    fun getPemasukanById(id: Int): LiveData<Pemasukan> {
        return pemasukanDao.getPemasukanById(id)
    }
}