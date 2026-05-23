package com.example.calbudget.presentation.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.calbudget.core.theme.*
import com.example.calbudget.domain.model.TransactionType
import com.example.calbudget.presentation.components.NeoBrutalCard
import com.example.calbudget.presentation.components.NeoBrutalTextField

// Kategori — akan diperluas di Tahap 3
private val incomeCategories = listOf(
    "💼 Gaji", "💰 Bisnis", "🎁 Hadiah", "📈 Investasi", "💳 Lainnya"
)
private val expenseCategories = listOf(
    "🍔 Makan", "🚗 Transport", "🏠 Rumah", "🛍️ Belanja",
    "💊 Kesehatan", "🎓 Pendidikan", "🎮 Hiburan", "💳 Lainnya"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    viewModel: TransactionViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.addTransactionState.collectAsState()

    // Kalau sukses disimpan, langsung back
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            viewModel.resetAddTransactionState()
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Tambah Transaksi",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = NeoBrutalYellow
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(NeoBrutalWhite)
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ===== TIPE TRANSAKSI =====
            Text("Tipe Transaksi", style = MaterialTheme.typography.titleMedium)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TransactionType.entries.forEach { type ->
                    val isSelected = state.selectedType == type
                    val bgColor = when {
                        isSelected && type == TransactionType.INCOME -> IncomeColor
                        isSelected && type == TransactionType.EXPENSE -> ExpenseColor
                        else -> NeoBrutalWhite
                    }
                    val label = if (type == TransactionType.INCOME) "⬆ Pemasukan"
                    else "⬇ Pengeluaran"

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                width = NeoBrutal.BorderWidth,
                                color = NeoBrutalBlack,
                                shape = RoundedCornerShape(NeoBrutal.RadiusSmall)
                            )
                            .background(bgColor, RoundedCornerShape(NeoBrutal.RadiusSmall))
                            .clickable { viewModel.onTypeChange(type) }
                            .padding(vertical = 12.dp),
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

            // ===== JUDUL =====
            NeoBrutalTextField(
                value = state.title,
                onValueChange = viewModel::onTitleChange,
                label = "Judul",
                placeholder = "Contoh: Makan siang",
                errorMessage = state.titleError
            )

            // ===== NOMINAL =====
            NeoBrutalTextField(
                value = state.amount,
                onValueChange = viewModel::onAmountChange,
                label = "Nominal (Rp)",
                placeholder = "0",
                errorMessage = state.amountError,
                keyboardType = KeyboardType.Decimal
            )

            // ===== KATEGORI =====
            val categories = if (state.selectedType == TransactionType.INCOME)
                incomeCategories else expenseCategories

            Text("Kategori", style = MaterialTheme.typography.titleMedium)
            CategoryGrid(
                categories = categories,
                selectedCategory = state.selectedCategoryId,
                onCategorySelected = viewModel::onCategoryChange
            )

            // ===== CATATAN =====
            NeoBrutalTextField(
                value = state.note,
                onValueChange = viewModel::onNoteChange,
                label = "Catatan (opsional)",
                placeholder = "Tambahkan catatan...",
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ===== TOMBOL SIMPAN =====
            NeoBrutalCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = NeoBrutalYellow
            ) {
                Button(
                    onClick = viewModel::saveTransaction,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeoBrutalYellow,
                        contentColor = NeoBrutalBlack
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = NeoBrutalBlack,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            "💾 Simpan Transaksi",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Snackbar untuk error
    if (state.errorMessage != null) {
        LaunchedEffect(state.errorMessage) {
            // Tampilkan error, lalu clear
            viewModel.clearError()
        }
    }
}

// =========================================
// CATEGORY GRID — reusable chip grid
// =========================================
@Composable
private fun CategoryGrid(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    val chunkedCategories = categories.chunked(3)

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        chunkedCategories.forEach { rowCategories ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                rowCategories.forEach { category ->
                    val isSelected = selectedCategory == category
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                width = if (isSelected) NeoBrutal.BorderWidth else 1.dp,
                                color = NeoBrutalBlack,
                                shape = RoundedCornerShape(NeoBrutal.RadiusSmall)
                            )
                            .background(
                                color = if (isSelected) NeoBrutalYellow else NeoBrutalWhite,
                                shape = RoundedCornerShape(NeoBrutal.RadiusSmall)
                            )
                            .clickable { onCategorySelected(category) }
                            .padding(vertical = 10.dp, horizontal = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = category,
                            style = MaterialTheme.typography.bodySmall,
                            color = NeoBrutalBlack,
                            maxLines = 1
                        )
                    }
                }
                // Fill sisa kolom kalau tidak penuh 3
                repeat(3 - rowCategories.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}