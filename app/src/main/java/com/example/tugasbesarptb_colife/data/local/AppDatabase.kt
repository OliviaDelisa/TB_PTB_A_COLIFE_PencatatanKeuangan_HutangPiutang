package com.example.tugasbesarptb_colife.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.tugasbesarptb_colife.data.local.dao.PiutangDao
import com.example.tugasbesarptb_colife.data.local.entity.Piutang

@Database(
    entities = [Piutang::class],
    version = 2, // versi baru
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // DAO
    abstract fun piutangDao(): PiutangDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Migration dari versi 1 ke 2
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Tambahkan kolom baru untuk menyimpan URI bukti pembayaran
                database.execSQL("ALTER TABLE Piutang ADD COLUMN buktiPembayaranUri TEXT")
            }
        }

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "colife_database" // nama database
                )
                    .addMigrations(MIGRATION_1_2) // panggil migration
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
