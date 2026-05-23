package com.example.calbudget.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.calbudget.core.category.CategoryManager
import com.example.calbudget.core.theme.NeoBrutalBlack
import com.example.calbudget.core.theme.NeoBrutalYellow
import com.example.calbudget.domain.model.TransactionType
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryPickerSheet(
    transactionType: TransactionType,
    selectedCategoryId: String,
    onCategorySelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val categories = CategoryManager.getCategoriesForType(transactionType)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = com.example.calbudget.core.theme.NeoBrutalWhite,
        dragHandle = {
            // Handle Neo Brutalism — garis tebal
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 8.dp)
                    .width(48.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
            ) {
                // Handle bar
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            NeoBrutalBlack.copy(alpha = 0.3f)
                        )
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Text(
                text = "Pilih Kategori",
                style = MaterialTheme.typography.headlineSmall,
                color = NeoBrutalBlack,
                modifier = Modifier.padding(top = 8.dp)
            )

            // Grid 3 kolom
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories, key = { it.id }) { category ->
                    CategoryGridItem(
                        category = category,
                        isSelected = selectedCategoryId == category.id,
                        onClick = {
                            onCategorySelected(category.id)
                            onDismiss()
                        }
                    )
                }
            }
        }
    }
}

// Item dalam grid picker — lebih besar dari chip biasa
@Composable
private fun CategoryGridItem(
    category: com.example.calbudget.domain.model.TransactionCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (isSelected) category.color
    else com.example.calbudget.core.theme.NeoBrutalGray

    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)                          // Bujursangkar
            .background(
                bgColor,
                RoundedCornerShape(
                    com.example.calbudget.core.theme.NeoBrutal.RadiusSmall
                )
            )
            .border(
                width = if (isSelected)
                    com.example.calbudget.core.theme.NeoBrutal.BorderWidth
                else 1.5.dp,
                color = NeoBrutalBlack,
                shape = RoundedCornerShape(
                    com.example.calbudget.core.theme.NeoBrutal.RadiusSmall
                )
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = category.emoji,
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = category.label,
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) androidx.compose.ui.graphics.Color.White
                else NeoBrutalBlack,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                maxLines = 2
            )
        }
    }
}