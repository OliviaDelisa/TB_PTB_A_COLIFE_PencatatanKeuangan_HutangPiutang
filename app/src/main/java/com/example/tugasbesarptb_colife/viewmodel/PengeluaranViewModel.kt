package com.example.tugasbesarptb_colife.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.tugasbesarptb_colife.data.local.AppDatabase
import com.example.tugasbesarptb_colife.data.local.dao.PengeluaranWithKategori
import com.example.tugasbesarptb_colife.data.local.entity.Pengeluaran
import com.example.tugasbesarptb_colife.data.repository.PengeluaranRepository
import kotlinx.coroutines.launch

class PengeluaranViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PengeluaranRepository
    val allPengeluaran: LiveData<List<PengeluaranWithKategori>>

    init {
        val pengeluaranDao = AppDatabase.getInstance(application).pengeluaranDao()
        repository = PengeluaranRepository(pengeluaranDao)
        allPengeluaran = repository.allPengeluaran
    }

    fun insert(pengeluaran: Pengeluaran) = viewModelScope.launch {
        repository.insert(pengeluaran)
    }

    fun update(pengeluaran: Pengeluaran) = viewModelScope.launch {
        repository.update(pengeluaran)
    }

    fun delete(pengeluaran: Pengeluaran) = viewModelScope.launch {
        repository.delete(pengeluaran)
    }

    fun getPengeluaranById(id: Int): LiveData<Pengeluaran> {
        return repository.getPengeluaranById(id)
    }
}
