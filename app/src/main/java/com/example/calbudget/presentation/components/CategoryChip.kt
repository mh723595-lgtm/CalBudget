package com.example.calbudget.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.calbudget.core.theme.*
import com.example.calbudget.domain.model.TransactionCategory

// =========================================
// CATEGORY CHIP — komponen paling reusable
// Dipakai di: filter bar, form picker, display
// =========================================
@Composable
fun CategoryChip(
    category: TransactionCategory,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showLabel: Boolean = true      // bisa tampil emoji saja atau dengan label
) {
    val backgroundColor = if (isSelected) category.color else NeoBrutalWhite
    val textColor = if (isSelected) Color.White else NeoBrutalBlack
    val borderWidth = if (isSelected) NeoBrutal.BorderWidth else 1.5.dp

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(NeoBrutal.RadiusSmall))
            .background(backgroundColor)
            .border(
                width = borderWidth,
                color = NeoBrutalBlack,
                shape = RoundedCornerShape(NeoBrutal.RadiusSmall)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = if (showLabel) 12.dp else 10.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        if (showLabel) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(text = category.emoji, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = category.label,
                    style = MaterialTheme.typography.labelLarge,
                    color = textColor
                )
            }
        } else {
            // Hanya emoji — untuk tampilan kompak
            Text(text = category.emoji, style = MaterialTheme.typography.titleMedium)
        }
    }
}

// =========================================
// CATEGORY DISPLAY — hanya tampilkan, tidak clickable
// Dipakai di TransactionItem list
// =========================================
@Composable
fun CategoryDisplay(
    category: TransactionCategory,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(NeoBrutal.RadiusSmall))
            .background(category.color.copy(alpha = 0.15f))
            .border(
                width = 1.5.dp,
                color = category.color,
                shape = RoundedCornerShape(NeoBrutal.RadiusSmall)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = category.emoji, style = MaterialTheme.typography.bodySmall)
            Text(
                text = category.label,
                style = MaterialTheme.typography.labelSmall,
                color = NeoBrutalBlack
            )
        }
    }
}