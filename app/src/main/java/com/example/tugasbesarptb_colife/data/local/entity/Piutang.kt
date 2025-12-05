package com.example.tugasbesarptb_colife.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "piutang")
data class Piutang(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nama: String,
    val jumlah: Int,
    val tanggalTenggat: String,
    val tanggalDibuat: String,
    val tanggalSelesai: String? = null,
    val selesai: Boolean = false,
    val buktiPembayaranUri: String? = null
) : Parcelable
