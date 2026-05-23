package com.example.calbudget.presentation.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.calbudget.core.category.CategoryManager
import com.example.calbudget.domain.model.TransactionCategory
import com.example.calbudget.domain.model.TransactionType

// Filter bar horizontal scroll
// selectedCategoryId = null berarti "Semua"
@Composable
fun CategoryFilterRow(
    selectedType: TransactionType?,        // null = tampilkan semua
    selectedCategoryId: String?,           // null = "Semua"
    onCategorySelected: (String?) -> Unit, // null = pilih "Semua"
    modifier: Modifier = Modifier
) {
    // Ambil kategori sesuai tipe yang difilter
    val categories = when (selectedType) {
        TransactionType.INCOME -> CategoryManager.getCategoriesForType(TransactionType.INCOME)
        TransactionType.EXPENSE -> CategoryManager.getCategoriesForType(TransactionType.EXPENSE)
        null -> CategoryManager.allCategories
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(4.dp))

        // Chip "Semua" — di awal row
        AllCategoryChip(
            isSelected = selectedCategoryId == null,
            onClick = { onCategorySelected(null) }
        )

        // Chip tiap kategori
        categories.forEach { category ->
            CategoryChip(
                category = category,
                isSelected = selectedCategoryId == category.id,
                onClick = { onCategorySelected(category.id) }
            )
        }

        Spacer(modifier = Modifier.width(4.dp))
    }
}

// Chip "Semua" — chip khusus untuk reset filter
@Composable
private fun AllCategoryChip(
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // Buat fake category untuk chip "Semua"
    val allCategory = TransactionCategory(
        id = "all",
        label = "Semua",
        emoji = "✨",
        color = com.example.calbudget.core.theme.NeoBrutalBlack,
        type = com.example.calbudget.domain.model.CategoryType.BOTH
    )
    CategoryChip(
        category = allCategory,
        isSelected = isSelected,
        onClick = onClick
    )
}