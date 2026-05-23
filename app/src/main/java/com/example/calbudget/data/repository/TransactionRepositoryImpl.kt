package com.example.calbudget.data.repository

import com.example.calbudget.data.local.dao.TransactionDao
import com.example.calbudget.data.local.entity.toEntity
import com.example.calbudget.data.remote.FirestoreTransactionService
import com.example.calbudget.data.repository.AuthRepository
import com.example.calbudget.domain.model.Transaction
import com.example.calbudget.domain.model.TransactionType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

// @Singleton = hanya satu instance di seluruh app (via Hilt)
// @Inject constructor = Hilt tau cara buat object ini
@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val dao: TransactionDao,
    private val firestoreService: FirestoreTransactionService,
    private val authRepository: AuthRepository
) : TransactionRepository {

    // Scope untuk fire-and-forget Firestore sync
    // SupervisorJob = kalau sync gagal, tidak crash coroutine lain
    private val syncScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    // Ambil userId saat ini (null kalau belum login)
    private suspend fun getCurrentUserId(): String? =
        authRepository.currentUser.first()?.uid

    override suspend fun addTransaction(transaction: Transaction): Result<Unit> {
        return try {
            val withId = if (transaction.id.isEmpty()) {
                transaction.copy(id = UUID.randomUUID().toString())
            } else transaction

            // 1. Simpan ke Room (local — pasti berhasil)
            dao.insertTransaction(withId.toEntity())

            // 2. Sync ke Firestore (fire-and-forget — tidak block UI)
            syncScope.launch {
                getCurrentUserId()?.let { userId ->
                    firestoreService.syncTransaction(userId, withId)
                }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateTransaction(transaction: Transaction): Result<Unit> {
        return try {
            dao.updateTransaction(transaction.toEntity())

            syncScope.launch {
                getCurrentUserId()?.let { userId ->
                    firestoreService.syncTransaction(userId, transaction)
                }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteTransaction(transactionId: String): Result<Unit> {
        return try {
            dao.deleteTransactionById(transactionId)

            syncScope.launch {
                getCurrentUserId()?.let { userId ->
                    firestoreService.deleteTransaction(userId, transactionId)
                }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTransactionById(id: String): Transaction? =
        dao.getTransactionById(id)?.toDomain()

    override fun getAllTransactions(): Flow<List<Transaction>> =
        dao.getAllTransactions().map { it.map { e -> e.toDomain() } }

    override fun getTransactionsByType(type: TransactionType): Flow<List<Transaction>> =
        dao.getTransactionsByType(type.name).map { it.map { e -> e.toDomain() } }

    override fun getTransactionsByCategory(category: String): Flow<List<Transaction>> =
        dao.getTransactionsByCategory(category).map { it.map { e -> e.toDomain() } }


    override fun getTotalIncome(): Flow<Double> = dao.getTotalIncome()
    override fun getTotalExpense(): Flow<Double> = dao.getTotalExpense()
    override fun getTransactionCount(): Flow<Int> = dao.getTransactionCount()
}