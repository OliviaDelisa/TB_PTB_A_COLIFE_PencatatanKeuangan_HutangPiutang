package com.example.tugasbesarptb_colife.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.tugasbesarptb_colife.data.local.dao.PiutangDao
import com.example.tugasbesarptb_colife.data.local.entity.Piutang

class PiutangRepository(private val piutangDao: PiutangDao) {

    val allPiutang: LiveData<List<Piutang>> = piutangDao.getAllPiutang().asLiveData()

    suspend fun insert(piutang: Piutang) {
        piutangDao.insert(piutang)
    }

    suspend fun update(piutang: Piutang) {
        piutangDao.update(piutang)
    }

    suspend fun delete(piutang: Piutang) {
        piutangDao.delete(piutang)

    }

    suspend fun deleteAllPiutang() {
        piutangDao.deleteAllPiutang()
    }

}
