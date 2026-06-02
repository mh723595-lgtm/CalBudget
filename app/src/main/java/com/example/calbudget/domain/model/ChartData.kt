package com.example.calbudget.domain.model

import androidx.compose.ui.graphics.Color

data class PieSlice(
    val label: String,
    val value: Double,
    val color: Color,
    val percentage: Float
)

data class BarGroup(
    val label: String,
    val incomeValue: Double,
    val expenseValue: Double
)

enum class StatsPeriod(val label: String) {
    WEEK("Minggu ini"),
    MONTH("Bulan ini"),
    THREE_MONTHS("3 Bulan"),
    YEAR("Tahun ini")
}