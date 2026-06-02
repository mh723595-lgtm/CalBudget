package com.example.calbudget.presentation.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calbudget.core.utils.StatisticsHelper
import com.example.calbudget.data.repository.TransactionRepository
import com.example.calbudget.domain.model.StatsPeriod
import com.example.calbudget.domain.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {

    private val _selectedPeriod = MutableStateFlow(StatsPeriod.MONTH)

    val uiState: StateFlow<StatisticsUiState> = combine(
        repository.getAllTransactions(),
        _selectedPeriod
    ) { allTransactions, period ->
        val filtered = StatisticsHelper.filterByPeriod(allTransactions, period)
        val income = filtered.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
        val expense = filtered.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }

        StatisticsUiState(
            isLoading = false,
            selectedPeriod = period,
            totalIncome = income,
            totalExpense = expense,
            totalTransactions = filtered.size,
            pieSlices = StatisticsHelper.buildPieSlices(filtered),
            barGroups = StatisticsHelper.buildBarGroups(allTransactions, period),
            topExpenseCategories = StatisticsHelper.getTopExpenseCategories(filtered)
        )
    }
        .catch { e -> emit(StatisticsUiState(isLoading = false, errorMessage = e.message)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = StatisticsUiState(isLoading = true)
        )

    fun onPeriodSelected(period: StatsPeriod) {
        _selectedPeriod.value = period
    }
}