package com.example.calbudget.presentation.transactions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.calbudget.core.theme.*
import com.example.calbudget.core.utils.CurrencyFormatter
import com.example.calbudget.domain.model.Transaction
import com.example.calbudget.domain.model.TransactionType
import com.example.calbudget.presentation.components.NeoBrutalCard
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TransactionsScreen(
    viewModel: TransactionViewModel = hiltViewModel(),
    onNavigateToAdd: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        floatingActionButton = {
            // FAB Neo Brutalism — kuning dengan shadow hitam
            Box {
                // Shadow
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .offset(x = 3.dp, y = 3.dp)
                        .background(color = NeoBrutalBlack, shape = CircleShape)
                )
                // Button
                FloatingActionButton(
                    onClick = onNavigateToAdd,
                    containerColor = NeoBrutalYellow,
                    contentColor = NeoBrutalBlack,
                    shape = CircleShape,
                    modifier = Modifier
                        .size(60.dp)
                        .border(
                            width = NeoBrutal.BorderWidth,
                            color = NeoBrutalBlack,
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Tambah transaksi",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(NeoBrutalWhite)
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Header
            Text(
                text = "Transaksi",
                style = MaterialTheme.typography.headlineLarge,
                color = NeoBrutalBlack
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Konten berdasarkan state
            when (val state = uiState) {
                is TransactionUiState.Loading -> {
                    TransactionLoadingState()
                }
                is TransactionUiState.Empty -> {
                    TransactionEmptyState(onAddClick = onNavigateToAdd)
                }
                is TransactionUiState.Error -> {
                    TransactionErrorState(message = state.message)
                }
                is TransactionUiState.Success -> {
                    // Summary mini card
                    TransactionSummaryRow(
                        income = state.totalIncome,
                        expense = state.totalExpense
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // List transaksi
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(
                            items = state.transactions,
                            key = { it.id }  // key = performa LazyColumn lebih baik
                        ) { transaction ->
                            TransactionItem(
                                transaction = transaction,
                                onDelete = { viewModel.deleteTransaction(transaction.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

// =========================================
// TRANSACTION ITEM CARD
// =========================================
@Composable
private fun TransactionItem(
    transaction: Transaction,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale("id")) }

    // Delete confirmation dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text("Hapus Transaksi?", style = MaterialTheme.typography.headlineSmall)
            },
            text = {
                Text("\"${transaction.title}\" akan dihapus permanen.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeoBrutalRed,
                        contentColor = NeoBrutalWhite
                    )
                ) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    NeoBrutalCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = NeoBrutalWhite
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Category icon circle
            val bgColor = if (transaction.type == TransactionType.INCOME)
                IncomeColor.copy(alpha = 0.15f)
            else
                ExpenseColor.copy(alpha = 0.15f)

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(bgColor)
                    .border(
                        width = 2.dp,
                        color = if (transaction.type == TransactionType.INCOME) IncomeColor
                        else ExpenseColor,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (transaction.type == TransactionType.INCOME)
                        Icons.Default.TrendingUp
                    else
                        Icons.Default.TrendingDown,
                    contentDescription = null,
                    tint = if (transaction.type == TransactionType.INCOME) IncomeColor
                    else ExpenseColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = NeoBrutalBlack,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${transaction.category} • ${dateFormat.format(Date(transaction.date))}",
                    style = MaterialTheme.typography.bodySmall,
                    color = NeoBrutalBlack.copy(alpha = 0.6f)
                )
            }

            // Amount + delete
            Column(horizontalAlignment = Alignment.End) {
                val amountColor = if (transaction.type == TransactionType.INCOME)
                    IncomeColor else ExpenseColor
                val prefix = if (transaction.type == TransactionType.INCOME) "+" else "-"

                Text(
                    text = "$prefix${CurrencyFormatter.formatRupiah(transaction.amount)}",
                    style = MaterialTheme.typography.titleMedium,
                    color = amountColor
                )
                IconButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Hapus",
                        tint = NeoBrutalRed.copy(alpha = 0.6f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

// =========================================
// SUMMARY ROW
// =========================================
@Composable
private fun TransactionSummaryRow(income: Double, expense: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Income card
        NeoBrutalCard(
            modifier = Modifier.weight(1f),
            backgroundColor = IncomeColor.copy(alpha = 0.1f)
        ) {
            Column {
                Text("Pemasukan", style = MaterialTheme.typography.bodySmall, color = NeoBrutalBlack.copy(0.7f))
                Text(
                    CurrencyFormatter.formatRupiahShort(income),
                    style = MaterialTheme.typography.titleLarge,
                    color = IncomeColor
                )
            }
        }
        // Expense card
        NeoBrutalCard(
            modifier = Modifier.weight(1f),
            backgroundColor = ExpenseColor.copy(alpha = 0.1f)
        ) {
            Column {
                Text("Pengeluaran", style = MaterialTheme.typography.bodySmall, color = NeoBrutalBlack.copy(0.7f))
                Text(
                    CurrencyFormatter.formatRupiahShort(expense),
                    style = MaterialTheme.typography.titleLarge,
                    color = ExpenseColor
                )
            }
        }
    }
}

// =========================================
// EMPTY STATE
// =========================================
@Composable
private fun TransactionEmptyState(onAddClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("📭", style = MaterialTheme.typography.displayLarge)
        Text(
            "Belum ada transaksi",
            style = MaterialTheme.typography.headlineSmall,
            color = NeoBrutalBlack
        )
        Text(
            "Tambah transaksi pertamamu sekarang!",
            style = MaterialTheme.typography.bodyMedium,
            color = NeoBrutalBlack.copy(alpha = 0.6f)
        )
        Button(
            onClick = onAddClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = NeoBrutalYellow,
                contentColor = NeoBrutalBlack
            ),
            border = BorderStroke(NeoBrutal.BorderWidth, NeoBrutalBlack)
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Tambah Transaksi", style = MaterialTheme.typography.labelLarge)
        }
    }
}

// =========================================
// LOADING STATE (Shimmer sederhana)
// =========================================
@Composable
private fun TransactionLoadingState() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        repeat(5) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(
                        color = NeoBrutalGray,
                        shape = RoundedCornerShape(NeoBrutal.RadiusMedium)
                    )
            )
        }
    }
}

// =========================================
// ERROR STATE
// =========================================
@Composable
private fun TransactionErrorState(message: String) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("⚠️", style = MaterialTheme.typography.displayMedium)
        Text("Terjadi kesalahan", style = MaterialTheme.typography.headlineSmall)
        Text(message, style = MaterialTheme.typography.bodyMedium, color = NeoBrutalRed)
    }
}