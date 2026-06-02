package com.example.calbudget.presentation.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.calbudget.core.theme.*
import com.example.calbudget.core.utils.CurrencyFormatter
import com.example.calbudget.presentation.components.*

@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(NeoBrutalWhite),
        contentPadding = PaddingValues(
            start = 16.dp, end = 16.dp,
            top = 16.dp, bottom = 80.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Header
        item {
            Text(
                "Statistik",
                style = MaterialTheme.typography.headlineLarge,
                color = NeoBrutalBlack
            )
        }

        // Period chips
        item {
            PeriodFilterChips(
                selectedPeriod = state.selectedPeriod,
                onPeriodSelected = viewModel::onPeriodSelected
            )
        }

        if (state.isLoading) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(if (it == 0) 80.dp else 200.dp)
                                .background(NeoBrutalGray,
                                    RoundedCornerShape(NeoBrutal.RadiusMedium))
                        )
                    }
                }
            }
            return@LazyColumn
        }

        // Summary
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Balance card
                NeoBrutalCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = if (state.balance >= 0)
                        IncomeColor.copy(alpha = 0.12f)
                    else
                        ExpenseColor.copy(alpha = 0.12f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "Saldo Periode",
                                style = MaterialTheme.typography.bodySmall,
                                color = NeoBrutalBlack.copy(alpha = 0.6f)
                            )
                            Text(
                                CurrencyFormatter.formatRupiah(state.balance),
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.ExtraBold
                                ),
                                color = if (state.balance >= 0) IncomeColor else ExpenseColor
                            )
                        }
                        Text(
                            "${state.totalTransactions}\ntransaksi",
                            style = MaterialTheme.typography.bodySmall,
                            color = NeoBrutalBlack.copy(alpha = 0.5f),
                            textAlign = TextAlign.End
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    SummaryCard(
                        title = "Pemasukan",
                        amount = state.totalIncome,
                        icon = "⬆",
                        accentColor = IncomeColor,
                        modifier = Modifier.weight(1f)
                    )
                    SummaryCard(
                        title = "Pengeluaran",
                        amount = state.totalExpense,
                        icon = "⬇",
                        accentColor = ExpenseColor,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Bar chart
        item {
            NeoBrutalCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "📊 Pemasukan vs Pengeluaran",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    BarChart(groups = state.barGroups)
                }
            }
        }

        // Pie chart
        item {
            NeoBrutalCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "🍩 Distribusi Pengeluaran",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    PieChart(
                        slices = state.pieSlices,
                        totalExpense = state.totalExpense
                    )
                }
            }
        }

        // Top expense categories
        if (state.topExpenseCategories.isNotEmpty()) {
            item {
                NeoBrutalCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            "🏆 Pengeluaran Terbesar",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        state.topExpenseCategories.forEachIndexed { index, (label, amount) ->
                            val percentage = if (state.totalExpense > 0)
                                (amount / state.totalExpense).toFloat() else 0f
                            val rankColor = when (index) {
                                0 -> NeoBrutalYellow
                                1 -> NeoBrutalGrayDark
                                2 -> ExpenseColor.copy(alpha = 0.25f)
                                else -> NeoBrutalWhite
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(rankColor,
                                            RoundedCornerShape(NeoBrutal.RadiusSmall))
                                        .border(1.5.dp, NeoBrutalBlack,
                                            RoundedCornerShape(NeoBrutal.RadiusSmall)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "${index + 1}",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = NeoBrutalBlack
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            label,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        )
                                        Text(
                                            "${(percentage * 100).toInt()}%",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = NeoBrutalBlack.copy(alpha = 0.6f)
                                        )
                                    }
                                    Spacer(Modifier.height(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(6.dp)
                                            .background(NeoBrutalGray, RoundedCornerShape(3.dp))
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(percentage)
                                                .fillMaxHeight()
                                                .background(ExpenseColor, RoundedCornerShape(3.dp))
                                        )
                                    }
                                }
                                Text(
                                    CurrencyFormatter.formatRupiahShort(amount),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = ExpenseColor
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}