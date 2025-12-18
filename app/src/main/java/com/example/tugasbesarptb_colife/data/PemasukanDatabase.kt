package com.example.tugasbesarptb_colife.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tugasbesarptb_colife.data.local.dao.PemasukanDao
import com.example.tugasbesarptb_colife.data.local.entity.Pemasukan

@Database(entities = [Pemasukan::class], version = 2, exportSchema = false) // Naikkan versi ke 2
abstract class PemasukanDatabase : RoomDatabase() {

    abstract fun pemasukanDao(): PemasukanDao

    companion object {
        @Volatile
        private var INSTANCE: PemasukanDatabase? = null

        fun getDatabase(context: Context): PemasukanDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PemasukanDatabase::class.java,
                    "pemasukan_database"
                )
                .fallbackToDestructiveMigration() // Tambahkan ini
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
