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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.calbudget.core.category.CategoryManager
import com.example.calbudget.core.theme.*
import com.example.calbudget.domain.model.TransactionType
import com.example.calbudget.presentation.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    viewModel: TransactionViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.addTransactionState.collectAsState()
    var showCategoryPicker by remember { mutableStateOf(false) }

    // Resolve kategori yang dipilih
    val selectedCategory = remember(state.selectedCategoryId) {
        CategoryManager.getCategoryById(state.selectedCategoryId)
    }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            viewModel.resetAddTransactionState()
            onNavigateBack()
        }
    }

    // Bottom sheet kategori
    if (showCategoryPicker) {
        CategoryPickerSheet(
            transactionType = state.selectedType,
            selectedCategoryId = state.selectedCategoryId,
            onCategorySelected = viewModel::onCategoryChange,
            onDismiss = { showCategoryPicker = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tambah Transaksi", style = MaterialTheme.typography.headlineSmall) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = NeoBrutalYellow)
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // ===== TIPE TRANSAKSI =====
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Tipe Transaksi", style = MaterialTheme.typography.titleMedium)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(NeoBrutal.BorderWidth, NeoBrutalBlack,
                            RoundedCornerShape(NeoBrutal.RadiusSmall))
                        .background(NeoBrutalWhite, RoundedCornerShape(NeoBrutal.RadiusSmall)),
                    horizontalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    TransactionType.entries.forEach { type ->
                        val isSelected = state.selectedType == type
                        val bgColor = when {
                            isSelected && type == TransactionType.INCOME -> IncomeColor
                            isSelected -> ExpenseColor
                            else -> NeoBrutalWhite
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(bgColor)
                                .clickable { viewModel.onTypeChange(type) }
                                .padding(vertical = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (type == TransactionType.INCOME) "⬆ Pemasukan"
                                else "⬇ Pengeluaran",
                                style = MaterialTheme.typography.labelLarge,
                                color = if (isSelected) NeoBrutalWhite else NeoBrutalBlack
                            )
                        }
                    }
                }
            }

            // ===== JUDUL =====
            NeoBrutalTextField(
                value = state.title,
                onValueChange = viewModel::onTitleChange,
                label = "Judul",
                placeholder = "Contoh: Makan siang di warteg",
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

            // ===== KATEGORI — pakai bottom sheet =====
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Kategori", style = MaterialTheme.typography.titleMedium)

                NeoBrutalCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showCategoryPicker = true },
                    backgroundColor = NeoBrutalWhite
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(
                                        selectedCategory.color.copy(alpha = 0.15f),
                                        RoundedCornerShape(NeoBrutal.RadiusSmall)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(selectedCategory.emoji,
                                    style = MaterialTheme.typography.titleLarge)
                            }
                            Column {
                                Text(
                                    selectedCategory.label,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    "Ketuk untuk ganti",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = NeoBrutalBlack.copy(alpha = 0.5f)
                                )
                            }
                        }
                        Icon(
                            Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = NeoBrutalBlack.copy(alpha = 0.5f)
                        )
                    }
                }
            }

            // ===== CATATAN =====
            NeoBrutalTextField(
                value = state.note,
                onValueChange = viewModel::onNoteChange,
                label = "Catatan (opsional)",
                placeholder = "Tambahkan catatan...",
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(4.dp))

            // ===== TOMBOL SIMPAN =====
            Box {
                // Shadow
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .offset(x = 4.dp, y = 4.dp)
                        .background(NeoBrutalBlack, RoundedCornerShape(NeoBrutal.RadiusSmall))
                )
                Button(
                    onClick = viewModel::saveTransaction,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .border(NeoBrutal.BorderWidth, NeoBrutalBlack,
                            RoundedCornerShape(NeoBrutal.RadiusSmall)),
                    enabled = !state.isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeoBrutalYellow,
                        contentColor = NeoBrutalBlack,
                        disabledContainerColor = NeoBrutalGray
                    ),
                    shape = RoundedCornerShape(NeoBrutal.RadiusSmall),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = NeoBrutalBlack,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("💾  Simpan Transaksi",
                            style = MaterialTheme.typography.labelLarge)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}