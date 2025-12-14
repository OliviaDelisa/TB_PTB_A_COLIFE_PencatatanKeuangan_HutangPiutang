package com.example.tugasbesarptb_colife.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pemasukan_table")
data class Pemasukan(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val sumber: String,
    val tanggal: String,
    val jumlah: String
)
