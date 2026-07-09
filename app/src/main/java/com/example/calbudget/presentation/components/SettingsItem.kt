package com.example.calbudget.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.calbudget.core.theme.*

// =========================================
// CLICK ITEM — bisa diklik (buka dialog/navigasi)
// =========================================
@Composable
fun SettingsClickItem(
    emoji: String,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    accentColor: Color = NeoBrutalYellow
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(NeoBrutalWhite)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Icon box
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    accentColor.copy(alpha = 0.15f),
                    RoundedCornerShape(NeoBrutal.RadiusSmall)
                )
                .border(
                    1.5.dp,
                    NeoBrutalBlack.copy(alpha = 0.12f),
                    RoundedCornerShape(NeoBrutal.RadiusSmall)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(emoji, style = MaterialTheme.typography.titleMedium)
        }

        // Teks
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = NeoBrutalBlack
            )
            if (subtitle.isNotEmpty()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = NeoBrutalBlack.copy(alpha = 0.5f)
                )
            }
        }

        // Chevron kanan
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = NeoBrutalBlack.copy(alpha = 0.35f),
            modifier = Modifier.size(20.dp)
        )
    }
}

// =========================================
// TOGGLE ITEM — dengan Switch
// =========================================
@Composable
fun SettingsToggleItem(
    emoji: String,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    accentColor: Color = NeoBrutalYellow
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(NeoBrutalWhite)
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Icon box
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    accentColor.copy(alpha = 0.15f),
                    RoundedCornerShape(NeoBrutal.RadiusSmall)
                )
                .border(
                    1.5.dp,
                    NeoBrutalBlack.copy(alpha = 0.12f),
                    RoundedCornerShape(NeoBrutal.RadiusSmall)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(emoji, style = MaterialTheme.typography.titleMedium)
        }

        // Teks
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = NeoBrutalBlack
            )
            if (subtitle.isNotEmpty()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = NeoBrutalBlack.copy(alpha = 0.5f)
                )
            }
        }

        // Switch Neo Brutal style
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = NeoBrutalBlack,
                checkedTrackColor = NeoBrutalYellow,
                checkedBorderColor = NeoBrutalBlack,
                uncheckedThumbColor = NeoBrutalBlack.copy(alpha = 0.4f),
                uncheckedTrackColor = NeoBrutalGray,
                uncheckedBorderColor = NeoBrutalBlack.copy(alpha = 0.3f)
            )
        )
    }
}

// =========================================
// SECTION HEADER — judul section uppercase
// =========================================
@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text = title.uppercase(),
        style = MaterialTheme.typography.labelLarge,
        color = NeoBrutalBlack.copy(alpha = 0.4f),
        modifier = Modifier.padding(
            start = 16.dp, end = 16.dp,
            top = 20.dp, bottom = 6.dp
        )
    )
}

// =========================================
// DIVIDER — tipis antar item
// =========================================
@Composable
fun SettingsDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 70.dp),
        color = NeoBrutalBlack.copy(alpha = 0.07f),
        thickness = 1.dp
    )
}