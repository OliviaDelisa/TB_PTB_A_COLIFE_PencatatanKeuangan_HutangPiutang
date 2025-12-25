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
    suspend fun insert(piutang: Piutang): Long

    @Update
    suspend fun update(piutang: Piutang)

    @Delete
    suspend fun delete(piutang: Piutang)

    @Query("SELECT * FROM piutang WHERE userId = :userId ORDER BY id DESC")
    fun getAllPiutang(userId: Int): Flow<List<Piutang>>

    @Query("DELETE FROM piutang WHERE userId = :userId")
    suspend fun deleteAllPiutang(userId: Int)


    @Query("UPDATE piutang SET buktiPembayaranUri = :uri WHERE id = :piutangId")
    suspend fun updateBuktiPembayaran(piutangId: Long, uri: String)

    @Query("UPDATE piutang SET serverId = :serverId WHERE id = :localId")
    suspend fun updateServerId(localId: Long, serverId: Int?)

    @Query("SELECT * FROM piutang WHERE pendingSync = 1")
    fun getPendingSyncPiutang(): List<Piutang>

    @Query("SELECT * FROM piutang WHERE selesai = 0")
    suspend fun getAllActive(): List<Piutang>

}

