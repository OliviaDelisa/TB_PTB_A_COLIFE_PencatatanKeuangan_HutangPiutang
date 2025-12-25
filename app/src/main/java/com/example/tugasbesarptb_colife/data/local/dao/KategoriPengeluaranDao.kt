package com.example.tugasbesarptb_colife.data.local.dao
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tugasbesarptb_colife.data.local.entity.KategoriPengeluaran

@Dao
interface KategoriPengeluaranDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertKategori(kategori: KategoriPengeluaran)

    @Query("SELECT * FROM kategori_pengeluaran_table ORDER BY id DESC")
    fun getAllKategori(): LiveData<List<KategoriPengeluaran>>
}