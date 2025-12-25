
package com.example.tugasbesarptb_colife.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.tugasbesarptb_colife.data.local.dao.KategoriPengeluaranDao
import com.example.tugasbesarptb_colife.data.local.dao.PemasukanDao
import com.example.tugasbesarptb_colife.data.local.dao.PengeluaranDao
import com.example.tugasbesarptb_colife.data.local.dao.PiutangDao
import com.example.tugasbesarptb_colife.data.local.entity.KategoriPengeluaran
import com.example.tugasbesarptb_colife.data.local.entity.Pemasukan
import com.example.tugasbesarptb_colife.data.local.entity.Pengeluaran
import com.example.tugasbesarptb_colife.data.local.entity.Piutang
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

@Database(
    entities = [Piutang::class, Pemasukan::class, KategoriPengeluaran::class, Pengeluaran::class],
    version = 6, // Naikkan versi ke 6
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
                    .addCallback(object : Callback(){
                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            //pre-populate data
                            Executors.newSingleThreadExecutor().execute {
                                INSTANCE?.let {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        val kategoriDao = it.kategoriPengeluaranDao()
                                        if (kategoriDao.getKategoriCount() == 0) {
                                            kategoriDao.insertKategori(KategoriPengeluaran(nama = "Makanan", target = 0L, warna = 0xFFF44336.toInt()))
                                            kategoriDao.insertKategori(KategoriPengeluaran(nama = "Transportasi", target = 0L, warna = 0xFFFF9800.toInt()))
                                            kategoriDao.insertKategori(KategoriPengeluaran(nama = "Belanja", target = 0L, warna = 0xFF2196F3.toInt()))
                                            kategoriDao.insertKategori(KategoriPengeluaran(nama = "Hiburan", target = 0L, warna = 0xFF4CAF50.toInt()))
                                            kategoriDao.insertKategori(KategoriPengeluaran(nama = "Kesehatan", target = 0L, warna = 0xFF9C27B0.toInt()))
                                        }
                                    }
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
