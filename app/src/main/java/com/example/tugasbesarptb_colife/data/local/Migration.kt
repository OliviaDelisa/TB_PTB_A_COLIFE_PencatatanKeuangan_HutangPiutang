package com.example.tugasbesarptb_colife.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Tambah kolom baru untuk simpan URI foto
        database.execSQL("ALTER TABLE Piutang ADD COLUMN buktiPembayaranUri TEXT")
    }
}

// TODO: Implementasikan migrasi dari versi 2 ke 3
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Tambahkan query migrasi Anda di sini
    }
}

// TODO: Implementasikan migrasi dari versi 3 ke 4
val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Tambahkan query migrasi Anda di sini
    }
}
