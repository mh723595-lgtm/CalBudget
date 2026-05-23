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
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.calbudget.core.category.CategoryManager
import com.example.calbudget.core.theme.*
import com.example.calbudget.core.utils.CurrencyFormatter
import com.example.calbudget.domain.model.Transaction
import com.example.calbudget.domain.model.TransactionType
import com.example.calbudget.presentation.components.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TransactionsScreen(
    viewModel: TransactionViewModel = hiltViewModel(),
    onNavigateToAdd: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val filterState by viewModel.filterState.collectAsState()

    Scaffold(
        floatingActionButton = {
            Box {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .offset(x = 3.dp, y = 3.dp)
                        .background(color = NeoBrutalBlack, shape = CircleShape)
                )
                FloatingActionButton(
                    onClick = onNavigateToAdd,
                    containerColor = NeoBrutalYellow,
                    contentColor = NeoBrutalBlack,
                    shape = CircleShape,
                    modifier = Modifier
                        .size(60.dp)
                        .border(NeoBrutal.BorderWidth, NeoBrutalBlack, CircleShape)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Tambah", modifier = Modifier.size(28.dp))
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(NeoBrutalWhite)
                .padding(paddingValues)
        ) {
            // ===== HEADER =====
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Transaksi",
                        style = MaterialTheme.typography.headlineLarge,
                        color = NeoBrutalBlack
                    )
                    // Tombol reset filter — tampil kalau ada filter aktif
                    if (filterState.selectedType != null || filterState.selectedCategoryId != null) {
                        TextButton(onClick = viewModel::resetFilter) {
                            Icon(
                                Icons.Default.FilterList,
                                contentDescription = null,
                                tint = NeoBrutalRed,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text("Reset", color = NeoBrutalRed,
                                style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // ===== TYPE TOGGLE (Semua / Pemasukan / Pengeluaran) =====
            TypeFilterToggle(
                selectedType = filterState.selectedType,
                onTypeSelected = viewModel::onTypeFilterChange,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ===== CATEGORY FILTER ROW =====
            CategoryFilterRow(
                selectedType = filterState.selectedType,
                selectedCategoryId = filterState.selectedCategoryId,
                onCategorySelected = viewModel::onCategoryFilterChange,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ===== CONTENT =====
            when (val state = uiState) {
                is TransactionUiState.Loading -> {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        repeat(5) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp)
                                    .background(NeoBrutalGray, RoundedCornerShape(NeoBrutal.RadiusMedium))
                            )
                        }
                    }
                }

                is TransactionUiState.Empty -> {
                    TransactionEmptyState(
                        hasFilter = filterState.selectedType != null || filterState.selectedCategoryId != null,
                        onAddClick = onNavigateToAdd,
                        onResetFilter = viewModel::resetFilter
                    )
                }

                is TransactionUiState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("⚠️", style = MaterialTheme.typography.displayMedium)
                        Text("Terjadi kesalahan", style = MaterialTheme.typography.headlineSmall)
                        Text(state.message, style = MaterialTheme.typography.bodyMedium, color = NeoBrutalRed)
                    }
                }

                is TransactionUiState.Success -> {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        // Summary mini
                        TransactionSummaryRow(
                            income = state.totalIncome,
                            expense = state.totalExpense
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    LazyColumn(
                        contentPadding = PaddingValues(
                            start = 16.dp, end = 16.dp, bottom = 80.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = state.transactions,
                            key = { it.id }
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
// TYPE FILTER TOGGLE
// =========================================
@Composable
private fun TypeFilterToggle(
    selectedType: TransactionType?,
    onTypeSelected: (TransactionType?) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = listOf(
        null to "Semua",
        TransactionType.INCOME to "⬆ Masuk",
        TransactionType.EXPENSE to "⬇ Keluar"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(NeoBrutal.BorderWidth, NeoBrutalBlack, RoundedCornerShape(NeoBrutal.RadiusSmall))
            .background(NeoBrutalWhite, RoundedCornerShape(NeoBrutal.RadiusSmall))
            .clip(RoundedCornerShape(NeoBrutal.RadiusSmall)),
        horizontalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        options.forEachIndexed { index, (type, label) ->
            val isSelected = selectedType == type
            val bgColor = when {
                isSelected && type == TransactionType.INCOME -> IncomeColor
                isSelected && type == TransactionType.EXPENSE -> ExpenseColor
                isSelected -> NeoBrutalBlack
                else -> NeoBrutalWhite
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(bgColor)
                    .then(
                        if (index < options.size - 1)
                            Modifier.border(
                                width = 0.dp, color = NeoBrutalBlack,
                                shape = RoundedCornerShape(0.dp)
                            )
                        else Modifier
                    )
                    .clickable { onTypeSelected(type) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isSelected) NeoBrutalWhite else NeoBrutalBlack
                )
            }
        }
    }
}

// =========================================
// TRANSACTION ITEM (update: pakai CategoryDisplay)
// =========================================
@Composable
private fun TransactionItem(
    transaction: Transaction,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale("id")) }
    val category = remember(transaction.category) {
        CategoryManager.getCategoryById(transaction.category)
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Hapus Transaksi?", style = MaterialTheme.typography.headlineSmall) },
            text = { Text("\"${transaction.title}\" akan dihapus permanen.") },
            confirmButton = {
                Button(
                    onClick = { onDelete(); showDeleteDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = NeoBrutalRed)
                ) { Text("Hapus") }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteDialog = false }) { Text("Batal") }
            }
        )
    }

    NeoBrutalCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Category emoji circle
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(category.color.copy(alpha = 0.15f))
                    .border(2.dp, category.color, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(category.emoji, style = MaterialTheme.typography.titleLarge)
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = NeoBrutalBlack,
                    maxLines = 1
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // ✨ Pakai CategoryDisplay baru
                    CategoryDisplay(category = category)
                    Text(
                        text = dateFormat.format(Date(transaction.date)),
                        style = MaterialTheme.typography.bodySmall,
                        color = NeoBrutalBlack.copy(alpha = 0.5f)
                    )
                }
            }

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
                        Icons.Default.Delete,
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
// SUMMARY ROW (dari tahap 2, tidak berubah)
// =========================================
@Composable
private fun TransactionSummaryRow(income: Double, expense: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        NeoBrutalCard(
            modifier = Modifier.weight(1f),
            backgroundColor = IncomeColor.copy(alpha = 0.1f)
        ) {
            Column {
                Text("Pemasukan", style = MaterialTheme.typography.bodySmall,
                    color = NeoBrutalBlack.copy(0.7f))
                Text(
                    CurrencyFormatter.formatRupiahShort(income),
                    style = MaterialTheme.typography.titleLarge,
                    color = IncomeColor
                )
            }
        }
        NeoBrutalCard(
            modifier = Modifier.weight(1f),
            backgroundColor = ExpenseColor.copy(alpha = 0.1f)
        ) {
            Column {
                Text("Pengeluaran", style = MaterialTheme.typography.bodySmall,
                    color = NeoBrutalBlack.copy(0.7f))
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
// EMPTY STATE (update: bedakan empty karena filter vs benar kosong)
// =========================================
@Composable
private fun TransactionEmptyState(
    hasFilter: Boolean,
    onAddClick: () -> Unit,
    onResetFilter: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (hasFilter) {
            Text("🔍", style = MaterialTheme.typography.displayLarge)
            Text("Tidak ada hasil", style = MaterialTheme.typography.headlineSmall)
            Text(
                "Tidak ada transaksi untuk filter ini.",
                style = MaterialTheme.typography.bodyMedium,
                color = NeoBrutalBlack.copy(alpha = 0.6f)
            )
            OutlinedButton(onClick = onResetFilter) {
                Text("Reset Filter")
            }
        } else {
            Text("📭", style = MaterialTheme.typography.displayLarge)
            Text("Belum ada transaksi", style = MaterialTheme.typography.headlineSmall)
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
                Icon(Icons.Default.Add, null)
                Spacer(Modifier.width(8.dp))
                Text("Tambah Transaksi", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}