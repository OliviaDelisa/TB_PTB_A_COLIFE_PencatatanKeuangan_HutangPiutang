package com.example.tugasbesarptb_colife.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.tugasbesarptb_colife.data.local.entity.Pengeluaran

// Data class untuk menampung hasil join
data class PengeluaranWithKategori(
    val id: Int,
    val nama: String,
    val jumlah: Long,
    val tanggal: String,
    val kategoriId: Int,
    val namaKategori: String,
    val warnaKategori: Int
)

@Dao
interface PengeluaranDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPengeluaran(pengeluaran: Pengeluaran)

    @Update
    suspend fun updatePengeluaran(pengeluaran: Pengeluaran)

    @Delete
    suspend fun deletePengeluaran(pengeluaran: Pengeluaran)

    @Query("SELECT * FROM pengeluaran_table WHERE id = :id")
    fun getPengeluaranById(id: Int): LiveData<Pengeluaran>

    @Query("""
        SELECT 
            p.id, 
            p.nama, 
            p.jumlah, 
            p.tanggal, 
            p.kategoriId, 
            k.nama AS namaKategori, 
            k.warna AS warnaKategori
        FROM pengeluaran_table AS p
        INNER JOIN kategori_pengeluaran_table AS k ON p.kategoriId = k.id
        ORDER BY p.id DESC
    """)
    fun getAllPengeluaranWithKategori(): LiveData<List<PengeluaranWithKategori>>
}