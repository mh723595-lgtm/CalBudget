package com.example.calbudget.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.calbudget.core.theme.*
import com.example.calbudget.core.utils.CurrencyFormatter
import com.example.calbudget.domain.model.PieSlice

@Composable
fun PieChart(
    slices: List<PieSlice>,
    totalExpense: Double,
    modifier: Modifier = Modifier
) {
    if (slices.isEmpty()) {
        EmptyChartState("Belum ada data pengeluaran")
        return
    }

    val animProgress = remember { Animatable(0f) }
    LaunchedEffect(slices) {
        animProgress.snapTo(0f)
        animProgress.animateTo(1f, tween(1000, easing = FastOutSlowInEasing))
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Donut chart
            Box(
                modifier = Modifier.size(160.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(160.dp)) {
                    val strokeWidth = 44f
                    val radius = (size.minDimension - strokeWidth) / 2
                    val topLeft = Offset(center.x - radius, center.y - radius)
                    val arcSize = Size(radius * 2, radius * 2)
                    var startAngle = -90f

                    slices.forEach { slice ->
                        val sweepAngle = slice.percentage * 360f * animProgress.value
                        // Border hitam dulu (lebih lebar)
                        drawArc(
                            color = Color.Black,
                            startAngle = startAngle,
                            sweepAngle = sweepAngle,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = strokeWidth + 4f)
                        )
                        // Warna slice di atas border
                        drawArc(
                            color = slice.color,
                            startAngle = startAngle + 0.5f,
                            sweepAngle = sweepAngle - 1f,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = strokeWidth)
                        )
                        startAngle += sweepAngle
                    }
                }

                // Label total di tengah
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Total",
                        style = MaterialTheme.typography.bodySmall,
                        color = NeoBrutalBlack.copy(alpha = 0.5f)
                    )
                    Text(
                        CurrencyFormatter.formatRupiahShort(totalExpense),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = NeoBrutalBlack
                    )
                }
            }

            // Legend
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                slices.forEach { slice ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(slice.color)
                                .border(1.dp, NeoBrutalBlack, CircleShape)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                slice.label,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = NeoBrutalBlack
                            )
                            Text(
                                "${(slice.percentage * 100).toInt()}% · ${CurrencyFormatter.formatRupiahShort(slice.value)}",
                                style = MaterialTheme.typography.labelSmall,
                                color = NeoBrutalBlack.copy(alpha = 0.55f)
                            )
                        }
                    }
                }
            }
        }
    }
}