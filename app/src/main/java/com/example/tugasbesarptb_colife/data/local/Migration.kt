package com.example.tugasbesarptb_colife.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Tambah kolom baru untuk simpan URI foto
        database.execSQL("ALTER TABLE Piutang ADD COLUMN buktiPembayaranUri TEXT")
    }
}
