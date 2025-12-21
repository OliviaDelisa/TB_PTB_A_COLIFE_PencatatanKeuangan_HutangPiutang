package com.example.tugasbesarptb_colife.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pengeluaran")
data class Pengeluaran(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nama: String,
    val tanggal: String,
    val jumlah: String,
    val kategori: String
)
