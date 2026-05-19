package com.example.calbudget.data.repository

import com.example.calbudget.data.local.dao.TransactionDao
import com.example.calbudget.data.local.entity.toEntity
import com.example.calbudget.domain.model.Transaction
import com.example.calbudget.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

// @Singleton = hanya satu instance di seluruh app (via Hilt)
// @Inject constructor = Hilt tau cara buat object ini
@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val dao: TransactionDao
) : TransactionRepository {

    override suspend fun addTransaction(transaction: Transaction): Result<Unit> {
        return try {
            // Kalau id kosong, generate UUID baru
            val transactionWithId = if (transaction.id.isEmpty()) {
                transaction.copy(id = UUID.randomUUID().toString())
            } else {
                transaction
            }
            dao.insertTransaction(transactionWithId.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateTransaction(transaction: Transaction): Result<Unit> {
        return try {
            dao.updateTransaction(transaction.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteTransaction(transactionId: String): Result<Unit> {
        return try {
            dao.deleteTransactionById(transactionId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTransactionById(id: String): Transaction? {
        return dao.getTransactionById(id)?.toDomain()
    }

    override fun getAllTransactions(): Flow<List<Transaction>> {
        // .map = transform setiap emit dari Flow
        // toDomain() = convert setiap Entity → Domain Model
        return dao.getAllTransactions().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getTransactionsByType(type: TransactionType): Flow<List<Transaction>> {
        return dao.getTransactionsByType(type.name).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getTransactionsByCategory(category: String): Flow<List<Transaction>> {
        return dao.getTransactionsByCategory(category).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getTotalIncome(): Flow<Double> = dao.getTotalIncome()
    override fun getTotalExpense(): Flow<Double> = dao.getTotalExpense()
    override fun getTransactionCount(): Flow<Int> = dao.getTransactionCount()
}