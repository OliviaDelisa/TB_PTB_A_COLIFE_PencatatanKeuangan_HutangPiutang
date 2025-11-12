package com.example.tugasbesarptb_colife.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.tugasbesarptb_colife.model.Pengeluaran

class PengeluaranViewModel : ViewModel() {
    private val _daftarPengeluaran = mutableStateListOf<Pengeluaran>()
    val daftarPengeluaran: List<Pengeluaran> = _daftarPengeluaran

    init {
        // Sample data to match the UI preview
        _daftarPengeluaran.add(Pengeluaran("Makanan", "19/10/25", "30.000.000"))
        _daftarPengeluaran.add(Pengeluaran("Minuman", "30/10/25", "3.300.000"))
    }

    fun tambahPengeluaran(pengeluaran: Pengeluaran) {
        _daftarPengeluaran.add(pengeluaran)
    }

    fun hapusPengeluaran(pengeluaran: Pengeluaran) {
        _daftarPengeluaran.remove(pengeluaran)
    }
}
