package com.example.calbudget.data.repository

import com.example.calbudget.domain.model.Transaction
import com.example.calbudget.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    // CRUD dasar
    suspend fun addTransaction(transaction: Transaction): Result<Unit>
    suspend fun updateTransaction(transaction: Transaction): Result<Unit>
    suspend fun deleteTransaction(transactionId: String): Result<Unit>
    suspend fun getTransactionById(id: String): Transaction?

    // Queries — return Flow agar UI auto-update
    fun getAllTransactions(): Flow<List<Transaction>>
    fun getTransactionsByType(type: TransactionType): Flow<List<Transaction>>
    fun getTransactionsByCategory(category: String): Flow<List<Transaction>>

    // Summary untuk dashboard
    fun getTotalIncome(): Flow<Double>
    fun getTotalExpense(): Flow<Double>
    fun getTransactionCount(): Flow<Int>
}