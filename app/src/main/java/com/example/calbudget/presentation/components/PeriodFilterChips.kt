package com.example.calbudget.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.calbudget.core.theme.*
import com.example.calbudget.domain.model.StatsPeriod

@Composable
fun PeriodFilterChips(
    selectedPeriod: StatsPeriod,
    onPeriodSelected: (StatsPeriod) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(4.dp))
        StatsPeriod.entries.forEach { period ->
            val isSelected = selectedPeriod == period
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(NeoBrutal.RadiusSmall))
                    .background(if (isSelected) NeoBrutalBlack else NeoBrutalWhite)
                    .border(
                        width = if (isSelected) NeoBrutal.BorderWidth else 1.5.dp,
                        color = NeoBrutalBlack,
                        shape = RoundedCornerShape(NeoBrutal.RadiusSmall)
                    )
                    .clickable { onPeriodSelected(period) }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = period.label,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isSelected) NeoBrutalYellow else NeoBrutalBlack
                )
            }
        }
        Spacer(Modifier.width(4.dp))
    }
}