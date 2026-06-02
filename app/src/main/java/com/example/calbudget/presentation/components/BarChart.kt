package com.example.calbudget.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calbudget.core.theme.*
import com.example.calbudget.domain.model.BarGroup

@Composable
fun BarChart(
    groups: List<BarGroup>,
    modifier: Modifier = Modifier
) {
    if (groups.isEmpty() || groups.all { it.incomeValue == 0.0 && it.expenseValue == 0.0 }) {
        EmptyChartState("Belum ada data untuk periode ini")
        return
    }

    val animProgress = remember { Animatable(0f) }
    LaunchedEffect(groups) {
        animProgress.snapTo(0f)
        animProgress.animateTo(1f, tween(800, easing = FastOutSlowInEasing))
    }

    val maxValue = groups.maxOf { maxOf(it.incomeValue, it.expenseValue) }
        .takeIf { it > 0 } ?: 1.0

    Column(modifier = modifier.fillMaxWidth()) {
        // Legend
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    Modifier.size(10.dp)
                        .background(IncomeColor, RoundedCornerShape(2.dp))
                        .border(1.dp, NeoBrutalBlack, RoundedCornerShape(2.dp))
                )
                Text("Masuk", style = MaterialTheme.typography.labelSmall,
                    color = NeoBrutalBlack.copy(alpha = 0.7f))
            }
            Spacer(Modifier.width(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    Modifier.size(10.dp)
                        .background(ExpenseColor, RoundedCornerShape(2.dp))
                        .border(1.dp, NeoBrutalBlack, RoundedCornerShape(2.dp))
                )
                Text("Keluar", style = MaterialTheme.typography.labelSmall,
                    color = NeoBrutalBlack.copy(alpha = 0.7f))
            }
        }

        Spacer(Modifier.height(8.dp))

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val chartHeight = canvasHeight - 28.dp.toPx()
            val groupCount = groups.size
            val groupWidth = canvasWidth / groupCount
            val barWidth = groupWidth * 0.28f
            val gap = barWidth * 0.3f

            // Garis baseline
            drawLine(
                color = Color.Black.copy(alpha = 0.15f),
                start = Offset(0f, chartHeight),
                end = Offset(canvasWidth, chartHeight),
                strokeWidth = 1.5f
            )

            groups.forEachIndexed { index, group ->
                val groupX = index * groupWidth + groupWidth / 2f

                // Bar income
                val incomeH = ((group.incomeValue / maxValue) * chartHeight * animProgress.value).toFloat()
                if (incomeH > 0) {
                    val left = groupX - barWidth - gap / 2
                    drawRoundRect(
                        color = Color.Black,
                        topLeft = Offset(left - 1.5f, chartHeight - incomeH - 1.5f),
                        size = Size(barWidth + 3f, incomeH + 3f),
                        cornerRadius = CornerRadius(6f, 6f)
                    )
                    drawRoundRect(
                        color = IncomeColor,
                        topLeft = Offset(left, chartHeight - incomeH),
                        size = Size(barWidth, incomeH),
                        cornerRadius = CornerRadius(6f, 6f)
                    )
                }

                // Bar expense
                val expenseH = ((group.expenseValue / maxValue) * chartHeight * animProgress.value).toFloat()
                if (expenseH > 0) {
                    val left = groupX + gap / 2
                    drawRoundRect(
                        color = Color.Black,
                        topLeft = Offset(left - 1.5f, chartHeight - expenseH - 1.5f),
                        size = Size(barWidth + 3f, expenseH + 3f),
                        cornerRadius = CornerRadius(6f, 6f)
                    )
                    drawRoundRect(
                        color = ExpenseColor,
                        topLeft = Offset(left, chartHeight - expenseH),
                        size = Size(barWidth, expenseH),
                        cornerRadius = CornerRadius(6f, 6f)
                    )
                }

                // Label X
                drawContext.canvas.nativeCanvas.drawText(
                    group.label,
                    groupX,
                    canvasHeight,
                    android.graphics.Paint().apply {
                        textAlign = android.graphics.Paint.Align.CENTER
                        textSize = 10.sp.toPx()
                        color = NeoBrutalBlack.copy(alpha = 0.6f).toArgb()
                    }
                )
            }
        }
    }
}

// Shared empty state untuk semua chart
@Composable
fun EmptyChartState(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(NeoBrutalGray, RoundedCornerShape(NeoBrutal.RadiusMedium))
            .border(1.5.dp, NeoBrutalBlack.copy(alpha = 0.15f),
                RoundedCornerShape(NeoBrutal.RadiusMedium)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text("📊", style = MaterialTheme.typography.headlineMedium)
            Text(message, style = MaterialTheme.typography.bodySmall,
                color = NeoBrutalBlack.copy(alpha = 0.5f))
        }
    }
}