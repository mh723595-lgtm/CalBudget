package com.example.calbudget.data.local.dao

import androidx.room.*
import com.example.calbudget.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao  // Data Access Object
interface TransactionDao {

    // INSERT — tambah transaksi baru
    // onConflict = REPLACE: kalau id sama, replace data lama
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    // UPDATE — update transaksi existing
    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    // DELETE — hapus transaksi
    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

    // DELETE by ID — lebih fleksibel
    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransactionById(id: String)

    // GET ALL — ambil semua, urutkan dari terbaru
    // Return Flow → UI otomatis recompose kalau data berubah
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    // GET BY ID — untuk edit screen
    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: String): TransactionEntity?

    // GET BY TYPE — filter income / expense
    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY date DESC")
    fun getTransactionsByType(type: String): Flow<List<TransactionEntity>>

    // GET BY CATEGORY
    @Query("SELECT * FROM transactions WHERE category = :category ORDER BY date DESC")
    fun getTransactionsByCategory(category: String): Flow<List<TransactionEntity>>

    // TOTAL INCOME — untuk dashboard
    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = 'INCOME'")
    fun getTotalIncome(): Flow<Double>

    // TOTAL EXPENSE — untuk dashboard
    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = 'EXPENSE'")
    fun getTotalExpense(): Flow<Double>

    // GET BY DATE RANGE — untuk filter tanggal (Tahap 8)
    @Query("""
        SELECT * FROM transactions 
        WHERE date BETWEEN :startDate AND :endDate 
        ORDER BY date DESC
    """)
    fun getTransactionsByDateRange(
        startDate: Long,
        endDate: Long
    ): Flow<List<TransactionEntity>>

    // COUNT — untuk stats sederhana
    @Query("SELECT COUNT(*) FROM transactions")
    fun getTransactionCount(): Flow<Int>
}