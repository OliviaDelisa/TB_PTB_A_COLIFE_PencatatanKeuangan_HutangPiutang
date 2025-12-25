package com.example.tugasbesarptb_colife.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.example.tugasbesarptb_colife.data.local.entity.Piutang
import com.example.tugasbesarptb_colife.data.repository.PiutangRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class PiutangViewModel(private val repository: PiutangRepository) : ViewModel() {

    val allPiutang: LiveData<List<Piutang>> = repository.getAllPiutang()

    private val _selectedPiutang = MutableLiveData<Piutang?>()
    val selectedPiutang: LiveData<Piutang?> get() = _selectedPiutang

    
    fun selectPiutang(piutang: Piutang) { _selectedPiutang.value = piutang }
    fun clearSelection() { _selectedPiutang.value = null }

  
    fun insertPiutang(piutang: Piutang) = viewModelScope.launch {
        repository.insert(piutang)
    }


    fun updatePiutang(piutang: Piutang) = viewModelScope.launch {
        repository.update(piutang)
    }


    fun deletePiutang(piutang: Piutang) = viewModelScope.launch {
        repository.delete(piutang)
    }

    fun markPiutangSelesai(piutang: Piutang) = viewModelScope.launch {
        val updated = piutang.copy(
            selesai = true,
            tanggalSelesai = getTodayDate()
        )
        repository.update(updated)
    }

    fun uploadBukti(piutang: Piutang, uri: Uri, context: Context) = viewModelScope.launch {
        repository.uploadBukti(piutang, uri, context)
    }

    fun syncPendingPiutang() = viewModelScope.launch {
        repository.syncPending()
    }


   
    private fun getTodayDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }
}

















