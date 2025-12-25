package com.example.tugasbesarptb_colife.data.repository

import androidx.lifecycle.LiveData
import com.example.tugasbesarptb_colife.data.local.dao.KategoriPengeluaranDao
import com.example.tugasbesarptb_colife.data.local.entity.KategoriPengeluaran
import com.example.tugasbesarptb_colife.model.KategoriRequest
import com.example.tugasbesarptb_colife.network.ApiService

class KategoriRepository(
    private val dao: KategoriPengeluaranDao,
    private val api: ApiService
) {

    val allKategori: LiveData<List<KategoriPengeluaran>> =
        dao.getAllKategori()

    suspend fun syncKategori(userId: Int) {
        val response = api.getKategori()
        if (response.isSuccessful) {
            response.body()?.forEach { kategoriResponse ->
                dao.insertKategori(
                    KategoriPengeluaran(
                        id = kategoriResponse.id,
                        nama = kategoriResponse.nama,
                        target = kategoriResponse.target,
                        warna = kategoriResponse.warna
                    )
                )
            }
        }
    }

    suspend fun insert(kategori: KategoriPengeluaran, userId: Int) {
        // simpan lokal
        dao.insertKategori(kategori)

        // kirim ke server
        api.addKategori(
            KategoriRequest(
                nama = kategori.nama,
                target = kategori.target,
                warna = kategori.warna,
                user_id = userId
            )
        )
    }
}
