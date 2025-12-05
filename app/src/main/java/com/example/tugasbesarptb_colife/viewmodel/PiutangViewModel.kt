package com.example.tugasbesarptb_colife.viewmodel

import androidx.lifecycle.*
import com.example.tugasbesarptb_colife.data.local.entity.Piutang
import com.example.tugasbesarptb_colife.data.repository.PiutangRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class PiutangViewModel(private val repository: PiutangRepository) : ViewModel() {

    val allPiutang: LiveData<List<Piutang>> = repository.allPiutang

    private val _selectedPiutang = MutableLiveData<Piutang?>()
    val selectedPiutang: LiveData<Piutang?> get() = _selectedPiutang

    fun insertPiutang(nama: String, tanggalTenggat: String, jumlah: Int) = viewModelScope.launch {
        val piutang = Piutang(
            nama = nama,
            jumlah = jumlah,
            tanggalTenggat = tanggalTenggat,
            tanggalDibuat = getCurrentDate(),
            tanggalSelesai = null,
            selesai = false,
            buktiPembayaranUri = null
        )
        repository.insert(piutang)
    }

    // Fungsi baru untuk object Piutang langsung
    fun insertPiutangObject(piutang: Piutang) = viewModelScope.launch {
        repository.insert(piutang)
    }

    fun updatePiutang(piutang: Piutang) = viewModelScope.launch {
        repository.update(piutang)
    }

    fun deletePiutang(piutang: Piutang) = viewModelScope.launch {
        repository.delete(piutang)
    }

    fun selectPiutang(piutang: Piutang) {
        _selectedPiutang.value = piutang
    }

    fun clearSelection() {
        _selectedPiutang.value = null
    }

    fun deleteAllPiutang() = viewModelScope.launch {
        repository.deleteAllPiutang()
    }


    private fun getCurrentDate(): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatter.format(Date())
    }
}
