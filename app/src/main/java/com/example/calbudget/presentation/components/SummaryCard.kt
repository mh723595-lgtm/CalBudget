package com.example.calbudget.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.calbudget.core.theme.*
import com.example.calbudget.core.utils.CurrencyFormatter

// Card pemasukan atau pengeluaran dengan animasi
@Composable
fun SummaryCard(
    title: String,
    amount: Double,
    icon: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val animatedAmount by animateFloatAsState(
        targetValue = amount.toFloat(),
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "summary_amount"
    )

    Box(modifier = modifier) {
        // Shadow
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(88.dp)
                .offset(x = 3.dp, y = 3.dp)
                .background(NeoBrutalBlack, RoundedCornerShape(NeoBrutal.RadiusMedium))
        )

        // Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(88.dp)
                .background(
                    color = accentColor.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(NeoBrutal.RadiusMedium)
                )
                .border(NeoBrutal.BorderWidth, NeoBrutalBlack, RoundedCornerShape(NeoBrutal.RadiusMedium))
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(icon, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    color = NeoBrutalBlack.copy(alpha = 0.6f)
                )
            }
            Text(
                text = CurrencyFormatter.formatRupiah(animatedAmount.toDouble()),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = accentColor
                ),
                maxLines = 1
            )
        }
    }
}

// Tambahkan di bawah SummaryCard.kt

@Composable
fun ExpenseProgressBar(
    expensePercentage: Float,   // 0f - 1f
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = expensePercentage,
        animationSpec = tween(1200, easing = FastOutSlowInEasing),
        label = "expense_progress"
    )

    val progressColor = when {
        expensePercentage < 0.5f -> IncomeColor
        expensePercentage < 0.8f -> NeoBrutalOrange
        else -> ExpenseColor
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Penggunaan anggaran",
                style = MaterialTheme.typography.bodySmall,
                color = NeoBrutalBlack.copy(alpha = 0.6f)
            )
            Text(
                text = "${(expensePercentage * 100).toInt()}%",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = progressColor
            )
        }

        // Track background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .background(NeoBrutalGray, RoundedCornerShape(5.dp))
                .border(1.dp, NeoBrutalBlack.copy(alpha = 0.2f), RoundedCornerShape(5.dp))
        ) {
            // Progress fill — animasi lebar
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress.coerceIn(0f, 1f))
                    .fillMaxHeight()
                    .background(progressColor, RoundedCornerShape(5.dp))
            )
        }
    }
}