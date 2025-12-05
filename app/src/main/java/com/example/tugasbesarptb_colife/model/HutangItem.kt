package com.example.tugasbesarptb_colife.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HutangItem(
    val id: Int,
    val nama: String,
    val tanggal: String,
    val jumlah: Int
) : Parcelable

