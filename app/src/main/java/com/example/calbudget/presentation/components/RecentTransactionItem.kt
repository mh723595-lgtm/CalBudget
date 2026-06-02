package com.example.calbudget.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.calbudget.core.category.CategoryManager
import com.example.calbudget.core.theme.*
import com.example.calbudget.core.utils.CurrencyFormatter
import com.example.calbudget.core.utils.DateFormatter
import com.example.calbudget.domain.model.Transaction
import com.example.calbudget.domain.model.TransactionType

@Composable
fun RecentTransactionItem(
    transaction: Transaction,
    modifier: Modifier = Modifier
) {
    val category = remember(transaction.category) {
        CategoryManager.getCategoryById(transaction.category)
    }
    val isIncome = transaction.type == TransactionType.INCOME
    val amountColor = if (isIncome) IncomeColor else ExpenseColor
    val prefix = if (isIncome) "+" else "-"

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(NeoBrutalWhite, RoundedCornerShape(NeoBrutal.RadiusMedium))
            .border(1.5.dp, NeoBrutalBlack.copy(alpha = 0.15f),
                RoundedCornerShape(NeoBrutal.RadiusMedium))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Category icon
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(category.color.copy(alpha = 0.15f))
                .border(1.5.dp, category.color, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(category.emoji, style = MaterialTheme.typography.titleMedium)
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
                text = "${category.label} · ${DateFormatter.formatRelative(transaction.date)}",
                style = MaterialTheme.typography.bodySmall,
                color = NeoBrutalBlack.copy(alpha = 0.5f),
                maxLines = 1
            )
        }

        // Amount
        Text(
            text = "$prefix${CurrencyFormatter.formatRupiah(transaction.amount)}",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = amountColor
            )
        )
    }
}