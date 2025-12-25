package com.example.tugasbesarptb_colife.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.tugasbesarptb_colife.data.local.AppDatabase
import com.example.tugasbesarptb_colife.data.repository.KategoriRepository
import com.example.tugasbesarptb_colife.data.local.entity.KategoriPengeluaran
import com.example.tugasbesarptb_colife.network.ApiClient
import kotlinx.coroutines.launch

class KategoriViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: KategoriRepository
    val allKategori: LiveData<List<KategoriPengeluaran>>

    init {
        val db = AppDatabase.getInstance(application)
        repository = KategoriRepository(
            db.kategoriPengeluaranDao(),
            ApiClient.instance
        )
        allKategori = repository.allKategori
    }

    // Ubah fungsi insert untuk menerima userId
    fun insert(kategori: KategoriPengeluaran, userId: Int) = viewModelScope.launch {
        repository.insert(kategori, userId)
    }

    fun sync(userId: Int) = viewModelScope.launch {
        repository.syncKategori(userId)
    }
}
