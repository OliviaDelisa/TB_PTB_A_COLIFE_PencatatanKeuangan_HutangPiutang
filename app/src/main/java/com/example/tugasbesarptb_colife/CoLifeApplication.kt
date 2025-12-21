package com.example.tugasbesarptb_colife

import android.app.Application
import com.example.tugasbesarptb_colife.data.local.AppDatabase
import com.example.tugasbesarptb_colife.data.repository.PengeluaranRepository

class CoLifeApplication : Application() {
    // Dibuat dengan lazy agar database dan repository hanya diinisialisasi saat dibutuhkan
    val database by lazy { AppDatabase.getInstance(this) }
    val pengeluaranRepository by lazy { PengeluaranRepository(database.pengeluaranDao()) }
}
