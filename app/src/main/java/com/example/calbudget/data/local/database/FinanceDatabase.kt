package com.example.calbudget.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.calbudget.data.local.dao.TransactionDao
import com.example.calbudget.data.local.entity.TransactionEntity

// @Database — daftarkan semua Entity dan versi DB
// entities = semua tabel yang ada
// version = naikkan angka ini kalau ada perubahan schema
// exportSchema = false untuk project kecil (true untuk production dengan migration)
@Database(
    entities = [TransactionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FinanceDatabase : RoomDatabase() {

    // Setiap DAO didaftarkan sebagai abstract function
    abstract fun transactionDao(): TransactionDao

    companion object {
        const val DATABASE_NAME = "finance_database"
    }
}