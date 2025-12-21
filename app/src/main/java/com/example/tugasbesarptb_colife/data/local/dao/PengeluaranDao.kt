package com.example.tugasbesarptb_colife.data.local.dao

import androidx.room.*
import com.example.tugasbesarptb_colife.data.local.entity.Pengeluaran
import kotlinx.coroutines.flow.Flow

@Dao
interface PengeluaranDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPengeluaran(pengeluaran: Pengeluaran)

    @Update
    suspend fun updatePengeluaran(pengeluaran: Pengeluaran)

    @Delete
    suspend fun deletePengeluaran(pengeluaran: Pengeluaran)

    @Query("SELECT * FROM pengeluaran ORDER BY tanggal DESC")
    fun getAllPengeluaran(): Flow<List<Pengeluaran>>

    @Query("SELECT * FROM pengeluaran WHERE id = :id")
    fun getPengeluaranById(id: Int): Flow<Pengeluaran?>
}
