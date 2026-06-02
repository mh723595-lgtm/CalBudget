package com.example.calbudget.presentation.home

import com.example.calbudget.domain.model.Transaction
import com.example.calbudget.domain.model.User

// State khusus untuk Home/Dashboard
// Terpisah dari TransactionUiState agar concern tidak bercampur
data class HomeUiState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val totalBalance: Double = 0.0,
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val recentTransactions: List<Transaction> = emptyList(),
    val errorMessage: String? = null
) {
    // Computed — berapa persen pengeluaran dari pemasukan
    // Dipakai untuk progress indicator sederhana
    val expensePercentage: Float
        get() = if (totalIncome > 0) (totalExpense / totalIncome).toFloat().coerceIn(0f, 1f)
        else 0f

    val isBalancePositive: Boolean get() = totalBalance >= 0
}