package com.example.tugasbesarptb_colife.pages.pengeluaran.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tugasbesarptb_colife.data.local.entity.Pengeluaran
import com.example.tugasbesarptb_colife.data.repository.PengeluaranRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PengeluaranViewModel(private val repository: PengeluaranRepository) : ViewModel() {

    val allPengeluaran: StateFlow<List<Pengeluaran>> = repository.getAllPengeluaran()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // ✅ Tambahkan fungsi untuk mengambil satu item
    fun getPengeluaranById(id: Int): Flow<Pengeluaran?> = repository.getPengeluaranById(id)

    fun insert(pengeluaran: Pengeluaran) = viewModelScope.launch {
        repository.insertPengeluaran(pengeluaran)
    }

    fun update(pengeluaran: Pengeluaran) = viewModelScope.launch {
        repository.updatePengeluaran(pengeluaran)
    }

    fun delete(pengeluaran: Pengeluaran) = viewModelScope.launch {
        repository.deletePengeluaran(pengeluaran)
    }
}

class PengeluaranViewModelFactory(private val repository: PengeluaranRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PengeluaranViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PengeluaranViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
