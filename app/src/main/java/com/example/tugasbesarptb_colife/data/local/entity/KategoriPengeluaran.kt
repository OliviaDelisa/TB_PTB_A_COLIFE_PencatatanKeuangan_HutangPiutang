package com.example.tugasbesarptb_colife.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kategori_pengeluaran_table")
data class KategoriPengeluaran(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nama: String,
    val target: Long,
    val warna: Int // Menyimpan warna sebagai Int (ARGB)
)
