package com.example.tugasbesarptb_colife.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.tugasbesarptb_colife.data.local.AppDatabase
import com.example.tugasbesarptb_colife.data.repository.PemasukanRepository
import com.example.tugasbesarptb_colife.data.local.entity.Pemasukan
import kotlinx.coroutines.launch

class PemasukanViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PemasukanRepository
    val allPemasukan: LiveData<List<Pemasukan>>

    init {
        val pemasukanDao = AppDatabase.getInstance(application).pemasukanDao()
        repository = PemasukanRepository(pemasukanDao)
        allPemasukan = repository.allPemasukan
    }

    fun insert(pemasukan: Pemasukan) = viewModelScope.launch {
        repository.insert(pemasukan)
    }

    fun update(pemasukan: Pemasukan) = viewModelScope.launch {
        repository.update(pemasukan)
    }

    fun delete(pemasukan: Pemasukan) = viewModelScope.launch {
        repository.delete(pemasukan)
    }

    fun getPemasukanById(id: Int): LiveData<Pemasukan> {
        return repository.getPemasukanById(id)
    }
}
