package com.example.tugasbesarptb_colife.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.tugasbesarptb_colife.data.local.AppDatabase
import com.example.tugasbesarptb_colife.data.local.entity.KategoriPengeluaran
import com.example.tugasbesarptb_colife.data.repository.KategoriRepository
import kotlinx.coroutines.launch

class KategoriViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: KategoriRepository
    val allKategori: LiveData<List<KategoriPengeluaran>>

    init {
        val kategoriDao = AppDatabase.getInstance(application).kategoriPengeluaranDao()
        repository = KategoriRepository(kategoriDao)
        allKategori = repository.allKategori
    }

    fun insert(kategori: KategoriPengeluaran) = viewModelScope.launch {
        repository.insert(kategori)
    }
}
