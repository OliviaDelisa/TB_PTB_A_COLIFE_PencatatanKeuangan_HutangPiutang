package com.example.tugasbesarptb_colife.model

data class PiutangUpdateRequest(
    val nama: String?,
    val jumlah: Int?,
    val tanggalTenggat: String?,
    val selesai: Boolean?,
    val tanggalSelesai: String?,
    val buktiPembayaranUri: String?
)
