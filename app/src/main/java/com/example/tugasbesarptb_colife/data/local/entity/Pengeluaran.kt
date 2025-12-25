package com.example.tugasbesarptb_colife.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "pengeluaran_table",
    foreignKeys = [ForeignKey(
        entity = KategoriPengeluaran::class,
        parentColumns = ["id"],
        childColumns = ["kategoriId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Pengeluaran(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nama: String,
    val jumlah: Long,
    val tanggal: String,
    val kategoriId: Int
)