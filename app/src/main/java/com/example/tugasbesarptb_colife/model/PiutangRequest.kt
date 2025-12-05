package com.example.tugasbesarptb_colife.model

data class PiutangRequest(
    val userId: Int,
    val nama: String,
    val jumlah: Int,
    val tanggalTenggat: String,
    val tanggalDibuat: String,
    val tanggalSelesai: String?,
    val selesai: Boolean,
    val buktiPembayaranUri: String?
)

