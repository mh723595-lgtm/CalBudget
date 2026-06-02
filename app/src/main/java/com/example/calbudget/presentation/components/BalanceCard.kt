package com.example.calbudget.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calbudget.core.theme.*
import com.example.calbudget.core.utils.CurrencyFormatter

@Composable
fun BalanceCard(
    totalBalance: Double,
    totalIncome: Double,
    totalExpense: Double,
    modifier: Modifier = Modifier
) {
    // Animasi count-up angka saldo
    val animatedBalance by animateFloatAsState(
        targetValue = totalBalance.toFloat(),
        animationSpec = tween(
            durationMillis = 1200,
            easing = FastOutSlowInEasing
        ),
        label = "balance_animation"
    )

    val isPositive = totalBalance >= 0
    val cardColor = if (isPositive) NeoBrutalYellow else NeoBrutalRed.copy(alpha = 0.8f)

    // Neo Brutal card dengan shadow
    Box(modifier = modifier.fillMaxWidth()) {
        // Shadow layer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .offset(x = NeoBrutal.ShadowOffsetX, y = NeoBrutal.ShadowOffsetY)
                .background(
                    color = NeoBrutalBlack,
                    shape = RoundedCornerShape(NeoBrutal.RadiusLarge)
                )
        )

        // Card utama
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(cardColor, RoundedCornerShape(NeoBrutal.RadiusLarge))
                .border(NeoBrutal.BorderWidth, NeoBrutalBlack, RoundedCornerShape(NeoBrutal.RadiusLarge))
                .padding(20.dp)
        ) {
            // Dekorasi lingkaran background
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 30.dp, y = (-20).dp)
                    .background(
                        color = NeoBrutalBlack.copy(alpha = 0.06f),
                        shape = RoundedCornerShape(60.dp)
                    )
            )
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 20.dp, y = 20.dp)
                    .background(
                        color = NeoBrutalBlack.copy(alpha = 0.04f),
                        shape = RoundedCornerShape(40.dp)
                    )
            )

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                // Label
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "💰",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "Total Saldo",
                        style = MaterialTheme.typography.titleMedium,
                        color = NeoBrutalBlack.copy(alpha = 0.7f)
                    )
                }

                // Nominal — animasi count-up
                Text(
                    text = CurrencyFormatter.formatRupiah(animatedBalance.toDouble()),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = NeoBrutalBlack
                )

                // Income vs Expense summary bar
                BalanceSummaryBar(
                    income = totalIncome,
                    expense = totalExpense
                )
            }
        }
    }
}

@Composable
private fun BalanceSummaryBar(income: Double, expense: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.TrendingUp,
                contentDescription = null,
                tint = IncomeColor,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = CurrencyFormatter.formatRupiahShort(income),
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = NeoBrutalBlack
            )
        }
        // Pemisah
        Box(
            modifier = Modifier
                .width(1.dp)
                .height(16.dp)
                .background(NeoBrutalBlack.copy(alpha = 0.2f))
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.TrendingDown,
                contentDescription = null,
                tint = ExpenseColor,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = CurrencyFormatter.formatRupiahShort(expense),
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = NeoBrutalBlack
            )
        }
    }
}