package com.example.tugasbesarptb_colife.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.tugasbesarptb_colife.data.local.entity.Piutang
import kotlinx.coroutines.flow.Flow

@Dao
interface PiutangDao {

    @Insert
    suspend fun insert(piutang: Piutang)

    @Update
    suspend fun update(piutang: Piutang)  // Tambahkan ini

    @Delete
    suspend fun delete(piutang: Piutang)  // Tambahkan ini

    @Query("SELECT * FROM piutang ORDER BY id DESC")
    fun getAllPiutang(): Flow<List<Piutang>>

    @Query("DELETE FROM piutang")
    suspend fun deleteAllPiutang()


}
