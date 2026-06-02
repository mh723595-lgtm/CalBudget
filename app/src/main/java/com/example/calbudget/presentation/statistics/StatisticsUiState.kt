package com.example.calbudget.presentation.statistics

import com.example.calbudget.domain.model.BarGroup
import com.example.calbudget.domain.model.PieSlice
import com.example.calbudget.domain.model.StatsPeriod

data class StatisticsUiState(
    val isLoading: Boolean = true,
    val selectedPeriod: StatsPeriod = StatsPeriod.MONTH,
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val totalTransactions: Int = 0,
    val pieSlices: List<PieSlice> = emptyList(),
    val barGroups: List<BarGroup> = emptyList(),
    val topExpenseCategories: List<Pair<String, Double>> = emptyList(),
    val errorMessage: String? = null
) {
    val balance: Double get() = totalIncome - totalExpense
}