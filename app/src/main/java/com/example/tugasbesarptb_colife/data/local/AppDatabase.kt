package com.example.tugasbesarptb_colife.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.tugasbesarptb_colife.data.local.dao.PengeluaranDao
import com.example.tugasbesarptb_colife.data.local.dao.PiutangDao
import com.example.tugasbesarptb_colife.data.local.entity.Pengeluaran
import com.example.tugasbesarptb_colife.data.local.entity.Piutang

@Database(
    entities = [Piutang::class, Pengeluaran::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun pengeluaranDao(): PengeluaranDao
    abstract fun piutangDao(): PiutangDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE Piutang ADD COLUMN buktiPembayaranUri TEXT")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE `pengeluaran` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `nama` TEXT NOT NULL,
                        `tanggal` TEXT NOT NULL,
                        `jumlah` TEXT NOT NULL,
                        `kategori` TEXT NOT NULL
                    )
                """.trimIndent())
            }
        }

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "colife_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
