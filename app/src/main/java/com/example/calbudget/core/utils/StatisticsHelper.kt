package com.example.calbudget.core.utils

import com.example.calbudget.core.category.CategoryManager
import com.example.calbudget.domain.model.BarGroup
import com.example.calbudget.domain.model.PieSlice
import com.example.calbudget.domain.model.StatsPeriod
import com.example.calbudget.domain.model.Transaction
import com.example.calbudget.domain.model.TransactionType
import java.text.SimpleDateFormat
import java.util.*

object StatisticsHelper {

    fun filterByPeriod(
        transactions: List<Transaction>,
        period: StatsPeriod
    ): List<Transaction> {
        val calendar = Calendar.getInstance()
        val endTime = calendar.timeInMillis

        val startTime = when (period) {
            StatsPeriod.WEEK -> {
                calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.timeInMillis
            }
            StatsPeriod.MONTH -> {
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.timeInMillis
            }
            StatsPeriod.THREE_MONTHS -> {
                calendar.add(Calendar.MONTH, -3)
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.timeInMillis
            }
            StatsPeriod.YEAR -> {
                calendar.set(Calendar.DAY_OF_YEAR, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.timeInMillis
            }
        }
        return transactions.filter { it.date in startTime..endTime }
    }

    fun buildPieSlices(transactions: List<Transaction>): List<PieSlice> {
        val expenses = transactions.filter { it.type == TransactionType.EXPENSE }
        val total = expenses.sumOf { it.amount }
        if (total == 0.0) return emptyList()

        return expenses
            .groupBy { it.category }
            .map { (categoryId, txs) ->
                val amount = txs.sumOf { it.amount }
                val category = CategoryManager.getCategoryById(categoryId)
                PieSlice(
                    label = category.label,
                    value = amount,
                    color = category.color,
                    percentage = (amount / total).toFloat()
                )
            }
            .sortedByDescending { it.value }
            .take(6)
    }

    fun buildBarGroups(
        transactions: List<Transaction>,
        period: StatsPeriod
    ): List<BarGroup> {
        return when (period) {
            StatsPeriod.WEEK -> buildDailyGroups(transactions)
            StatsPeriod.MONTH -> buildWeeklyGroups(transactions)
            StatsPeriod.THREE_MONTHS, StatsPeriod.YEAR -> buildMonthlyGroups(transactions)
        }
    }

    private fun buildDailyGroups(transactions: List<Transaction>): List<BarGroup> {
        val dayFormat = SimpleDateFormat("EEE", Locale("id"))
        return (0..6).map { daysAgo ->
            val calendar = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, -daysAgo)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val startOfDay = calendar.timeInMillis
            val endOfDay = startOfDay + 86_400_000L
            val dayTxs = transactions.filter { it.date in startOfDay until endOfDay }
            BarGroup(
                label = if (daysAgo == 0) "Hari ini"
                else dayFormat.format(Date(startOfDay)),
                incomeValue = dayTxs.filter { it.type == TransactionType.INCOME }.sumOf { it.amount },
                expenseValue = dayTxs.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
            )
        }.reversed()
    }

    private fun buildWeeklyGroups(transactions: List<Transaction>): List<BarGroup> {
        return (0..3).map { weeksAgo ->
            val calendar = Calendar.getInstance().apply {
                add(Calendar.WEEK_OF_YEAR, -weeksAgo)
                set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }
            val startOfWeek = calendar.timeInMillis
            val endOfWeek = startOfWeek + 7 * 86_400_000L
            val weekTxs = transactions.filter { it.date in startOfWeek until endOfWeek }
            BarGroup(
                label = "Mg ${4 - weeksAgo}",
                incomeValue = weekTxs.filter { it.type == TransactionType.INCOME }.sumOf { it.amount },
                expenseValue = weekTxs.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
            )
        }.reversed()
    }

    private fun buildMonthlyGroups(transactions: List<Transaction>): List<BarGroup> {
        val monthFormat = SimpleDateFormat("MMM", Locale("id"))
        return (0..5).map { monthsAgo ->
            val calendar = Calendar.getInstance().apply {
                add(Calendar.MONTH, -monthsAgo)
                set(Calendar.DAY_OF_MONTH, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }
            val startOfMonth = calendar.timeInMillis
            calendar.set(
                Calendar.DAY_OF_MONTH,
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            )
            calendar.set(Calendar.HOUR_OF_DAY, 23)
            val endOfMonth = calendar.timeInMillis
            val monthTxs = transactions.filter { it.date in startOfMonth..endOfMonth }
            BarGroup(
                label = monthFormat.format(Date(startOfMonth)),
                incomeValue = monthTxs.filter { it.type == TransactionType.INCOME }.sumOf { it.amount },
                expenseValue = monthTxs.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
            )
        }.reversed()
    }

    fun getTopExpenseCategories(
        transactions: List<Transaction>,
        limit: Int = 5
    ): List<Pair<String, Double>> {
        return transactions
            .filter { it.type == TransactionType.EXPENSE }
            .groupBy { it.category }
            .map { (catId, txs) ->
                val cat = CategoryManager.getCategoryById(catId)
                "${cat.emoji} ${cat.label}" to txs.sumOf { it.amount }
            }
            .sortedByDescending { it.second }
            .take(limit)
    }
}