package com.example.calbudget.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.example.calbudget.domain.model.Transaction
import com.example.calbudget.domain.model.TransactionType
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreTransactionService @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    // Path: users/{userId}/transactions/{transactionId}
    private fun getUserTransactionsRef(userId: String) =
        firestore.collection("users").document(userId).collection("transactions")

    // Upload satu transaksi ke Firestore
    suspend fun syncTransaction(userId: String, transaction: Transaction): Result<Unit> {
        return try {
            val data = transaction.toFirestoreMap()
            getUserTransactionsRef(userId)
                .document(transaction.id)
                .set(data, SetOptions.merge())  // merge = tidak hapus field yang tidak ada
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)  // Gagal sync = tidak crash, Room tetap jalan
        }
    }

    // Hapus transaksi dari Firestore
    suspend fun deleteTransaction(userId: String, transactionId: String): Result<Unit> {
        return try {
            getUserTransactionsRef(userId)
                .document(transactionId)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Ambil semua transaksi dari Firestore (untuk restore/sync awal)
    suspend fun fetchAllTransactions(userId: String): Result<List<Transaction>> {
        return try {
            val snapshot = getUserTransactionsRef(userId).get().await()
            val transactions = snapshot.documents.mapNotNull { doc ->
                doc.toTransaction()
            }
            Result.success(transactions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // =========================================
    // MAPPING HELPERS
    // =========================================

    private fun Transaction.toFirestoreMap(): Map<String, Any> = mapOf(
        "id" to id,
        "title" to title,
        "amount" to amount,
        "type" to type.name,
        "category" to category,
        "note" to note,
        "date" to date,
        "createdAt" to createdAt
    )

    private fun com.google.firebase.firestore.DocumentSnapshot.toTransaction(): Transaction? {
        return try {
            Transaction(
                id = getString("id") ?: id,
                title = getString("title") ?: return null,
                amount = getDouble("amount") ?: return null,
                type = TransactionType.valueOf(getString("type") ?: "EXPENSE"),
                category = getString("category") ?: "other",
                note = getString("note") ?: "",
                date = getLong("date") ?: System.currentTimeMillis(),
                createdAt = getLong("createdAt") ?: System.currentTimeMillis()
            )
        } catch (e: Exception) {
            null  // dokumen corrupt = skip saja
        }
    }
}