
package com.example.tugasbesarptb_colife.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tugasbesarptb_colife.data.local.dao.KategoriPengeluaranDao
import com.example.tugasbesarptb_colife.data.local.dao.PemasukanDao
import com.example.tugasbesarptb_colife.data.local.dao.PengeluaranDao
import com.example.tugasbesarptb_colife.data.local.dao.PiutangDao
import com.example.tugasbesarptb_colife.data.local.entity.KategoriPengeluaran
import com.example.tugasbesarptb_colife.data.local.entity.Pemasukan
import com.example.tugasbesarptb_colife.data.local.entity.Pengeluaran
import com.example.tugasbesarptb_colife.data.local.entity.Piutang

@Database(
    entities = [Piutang::class, Pemasukan::class, KategoriPengeluaran::class, Pengeluaran::class],
    version = 4, // Naikkan versi
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun piutangDao(): PiutangDao
    abstract fun pemasukanDao(): PemasukanDao
    abstract fun kategoriPengeluaranDao(): KategoriPengeluaranDao
    abstract fun pengeluaranDao(): PengeluaranDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "colife_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
