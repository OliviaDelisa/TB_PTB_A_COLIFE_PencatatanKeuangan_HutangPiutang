package com.example.tugasbesarptb_colife.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tugasbesarptb_colife.data.local.entity.Pemasukan

@Dao
interface PemasukanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPemasukan(pemasukan: Pemasukan)

    @Update
    suspend fun updatePemasukan(pemasukan: Pemasukan)

    @Delete
    suspend fun deletePemasukan(pemasukan: Pemasukan)

    @Query("SELECT * FROM pemasukan_table ORDER BY id DESC")
    fun getAllPemasukan(): LiveData<List<Pemasukan>>

    @Query("SELECT * FROM pemasukan_table WHERE id = :pemasukanId")
    fun getPemasukanById(pemasukanId: Int): LiveData<Pemasukan>
}