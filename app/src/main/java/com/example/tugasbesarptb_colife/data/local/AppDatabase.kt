package com.example.tugasbesarptb_colife.data.local

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.tugasbesarptb_colife.data.local.dao.PiutangDao
import com.example.tugasbesarptb_colife.data.local.entity.Piutang

@Database(
    entities = [Piutang::class],
    version = 6, // versi terbaru
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun piutangDao(): PiutangDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Migrasi dari versi 1 ke 2
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE piutang ADD COLUMN buktiPembayaranUri TEXT")
            }
        }

        // Migrasi dari versi 2 ke 3
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE piutang ADD COLUMN serverId INTEGER DEFAULT 0")
            }
        }

        // Migrasi dari versi 3 ke 4
        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE piutang ADD COLUMN pendingSync INTEGER NOT NULL DEFAULT 0")
            }
        }

        // Migrasi dari versi 4 ke 5 (kosong jika tidak ada perubahan)
        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // tidak ada perubahan
            }
        }

        // Migrasi dari versi 5 ke 6 (misal kalau ada kolom baru di Piutang)
        private val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // contoh: tambah kolom baru kalau perlu
                // database.execSQL("ALTER TABLE piutang ADD COLUMN contohBaru TEXT")
            }
        }

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "colife_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6)
                    .fallbackToDestructiveMigration() // fallback aman jika migrasi gagal
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
