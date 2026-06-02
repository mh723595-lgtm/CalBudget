package com.example.calbudget.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calbudget.data.repository.AuthRepository
import com.example.calbudget.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = combine(
        authRepository.currentUser,
        transactionRepository.getAllTransactions(),
        transactionRepository.getTotalIncome(),
        transactionRepository.getTotalExpense()
    ) { user, transactions, income, expense ->

        HomeUiState(
            isLoading = false,
            user = user,
            totalBalance = income - expense,
            totalIncome = income,
            totalExpense = expense,
            // Hanya 5 transaksi terbaru untuk dashboard
            recentTransactions = transactions.take(5)
        )
    }
        .catch { e ->
            emit(HomeUiState(isLoading = false, errorMessage = e.message))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState(isLoading = true)
        )
}