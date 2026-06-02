package com.example.calbudget.presentation.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.calbudget.core.theme.*
import com.example.calbudget.core.utils.DateFormatter
import com.example.calbudget.presentation.auth.AuthViewModel
import com.example.calbudget.presentation.components.*

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    onNavigateToTransactions: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val uiState by homeViewModel.uiState.collectAsState()

    // Animasi fade-in saat screen pertama kali muncul
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { isVisible = true }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(tween(400)) + slideInVertically(
            initialOffsetY = { it / 8 },
            animationSpec = tween(400)
        )
    ) {
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
            // ① GREETING HEADER
            item {
                GreetingHeader(
                    name = uiState.user?.displayName ?: "Pengguna",
                    onLogout = onLogout
                )
            }

            // Loading state
            if (uiState.isLoading) {
                item { DashboardLoadingState() }
                return@LazyColumn
            }

            // ② BALANCE CARD
            item {
                BalanceCard(
                    totalBalance = uiState.totalBalance,
                    totalIncome = uiState.totalIncome,
                    totalExpense = uiState.totalExpense
                )
            }

            // ③ INCOME + EXPENSE SUMMARY
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SummaryCard(
                        title = "Pemasukan",
                        amount = uiState.totalIncome,
                        icon = "⬆",
                        accentColor = IncomeColor,
                        modifier = Modifier.weight(1f)
                    )
                    SummaryCard(
                        title = "Pengeluaran",
                        amount = uiState.totalExpense,
                        icon = "⬇",
                        accentColor = ExpenseColor,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // ④ PROGRESS BAR (kalau ada pemasukan)
            if (uiState.totalIncome > 0) {
                item {
                    NeoBrutalCard(modifier = Modifier.fillMaxWidth()) {
                        ExpenseProgressBar(
                            expensePercentage = uiState.expensePercentage
                        )
                    }
                }
            }

            // ⑤ RECENT TRANSACTIONS
            item {
                RecentTransactionsHeader(
                    onSeeAll = onNavigateToTransactions
                )
            }

            if (uiState.recentTransactions.isEmpty()) {
                item { EmptyTransactionState() }
            } else {
                items(uiState.recentTransactions.size) { index ->
                    RecentTransactionItem(
                        transaction = uiState.recentTransactions[index]
                    )
                }
            }

            // ⑥ QUICK TIP CARD
            item {
                QuickTipCard(expensePercentage = uiState.expensePercentage)
            }
        }
    }
}

// =========================================
// GREETING HEADER
// =========================================
@Composable
private fun GreetingHeader(
    name: String,
    onLogout: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = DateFormatter.getGreeting() + ",",
                style = MaterialTheme.typography.bodyLarge,
                color = NeoBrutalBlack.copy(alpha = 0.6f)
            )
            Text(
                text = name.split(" ").firstOrNull() ?: name,  // hanya nama depan
                style = MaterialTheme.typography.headlineMedium,
                color = NeoBrutalBlack
            )
        }

        // Avatar + logout
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(NeoBrutalYellow)
                .then(
                    Modifier.border(
                        width = NeoBrutal.BorderWidth,
                        color = NeoBrutalBlack,
                        shape = CircleShape
                    )
                )
                .clickable { onLogout() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name.firstOrNull()?.uppercaseChar()?.toString() ?: "U",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                color = NeoBrutalBlack
            )
        }
    }
}

// =========================================
// RECENT TRANSACTIONS HEADER
// =========================================
@Composable
private fun RecentTransactionsHeader(onSeeAll: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Transaksi Terbaru",
            style = MaterialTheme.typography.headlineSmall,
            color = NeoBrutalBlack
        )
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(NeoBrutal.RadiusSmall))
                .clickable(onClick = onSeeAll)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = "Lihat semua",
                style = MaterialTheme.typography.labelLarge,
                color = NeoBrutalBlack.copy(alpha = 0.6f)
            )
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = NeoBrutalBlack.copy(alpha = 0.6f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

// =========================================
// EMPTY STATE
// =========================================
@Composable
private fun EmptyTransactionState() {
    NeoBrutalCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = NeoBrutalGray
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("📭", style = MaterialTheme.typography.displayMedium)
            Text(
                "Belum ada transaksi",
                style = MaterialTheme.typography.titleMedium,
                color = NeoBrutalBlack
            )
            Text(
                "Tambahkan dari tab Transaksi",
                style = MaterialTheme.typography.bodySmall,
                color = NeoBrutalBlack.copy(alpha = 0.5f)
            )
        }
    }
}

// =========================================
// QUICK TIP CARD — motivasi keuangan
// =========================================
@Composable
private fun QuickTipCard(expensePercentage: Float) {
    val (emoji, tip) = when {
        expensePercentage == 0f ->
            "🌱" to "Mulai catat pengeluaranmu hari ini!"
        expensePercentage < 0.5f ->
            "🎉" to "Keren! Pengeluaranmu masih di bawah 50% pemasukan."
        expensePercentage < 0.8f ->
            "⚠️" to "Hati-hati, pengeluaranmu sudah cukup banyak bulan ini."
        else ->
            "🚨" to "Pengeluaranmu melebihi 80% pemasukan! Kurangi pengeluaran."
    }

    NeoBrutalCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = NeoBrutalYellow.copy(alpha = 0.3f)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(emoji, style = MaterialTheme.typography.headlineMedium)
            Text(
                text = tip,
                style = MaterialTheme.typography.bodyMedium,
                color = NeoBrutalBlack,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

// =========================================
// LOADING STATE
// =========================================
@Composable
private fun DashboardLoadingState() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Skeleton balance card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(NeoBrutalGray, RoundedCornerShape(NeoBrutal.RadiusLarge))
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(88.dp)
                    .background(NeoBrutalGray, RoundedCornerShape(NeoBrutal.RadiusMedium))
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(88.dp)
                    .background(NeoBrutalGray, RoundedCornerShape(NeoBrutal.RadiusMedium))
            )
        }
        repeat(3) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .background(NeoBrutalGray, RoundedCornerShape(NeoBrutal.RadiusMedium))
            )
        }
    }
}