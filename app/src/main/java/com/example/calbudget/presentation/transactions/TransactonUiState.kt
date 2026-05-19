package com.example.calbudget.presentation.transactions

import com.example.calbudget.domain.model.Transaction

// Sealed class = hanya ada state yang sudah kita definisikan
// Tidak bisa ada state lain yang tidak kita duga
sealed class TransactionUiState {

    // Pertama kali load — tampilkan shimmer/loading
    data object Loading : TransactionUiState()

    // Data berhasil diambil — tampilkan list
    data class Success(
        val transactions: List<Transaction>,
        val totalIncome: Double = 0.0,
        val totalExpense: Double = 0.0
    ) : TransactionUiState() {
        // Computed property — balance otomatis dihitung
        val balance: Double get() = totalIncome - totalExpense
    }

    // Error terjadi — tampilkan pesan error
    data class Error(val message: String) : TransactionUiState()

    // Data kosong — tampilkan empty state illustration
    data object Empty : TransactionUiState()
}

// State khusus untuk form Add/Edit Transaction
data class AddTransactionUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,

    // Form fields
    val title: String = "",
    val amount: String = "",       // String karena TextField input
    val selectedType: com.example.calbudget.domain.model.TransactionType =
        com.example.calbudget.domain.model.TransactionType.EXPENSE,
    val selectedCategory: String = "Lainnya",
    val note: String = "",
    val date: Long = System.currentTimeMillis(),

    // Validation errors
    val titleError: String? = null,
    val amountError: String? = null
)