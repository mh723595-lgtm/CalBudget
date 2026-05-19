package com.example.calbudget.core.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.calbudget.core.theme.NeoBrutalBlack
import com.example.calbudget.core.theme.ShadowColor

// =========================================
// NEO BRUTALISM CONSTANTS
// Gunakan konstanta ini agar konsisten!
// =========================================

object NeoBrutal {
    // Border
    val BorderWidth = 3.dp           // Border hitam tebal
    val BorderColor = NeoBrutalBlack

    // Shadow offset — signature Neo Brutalism
    val ShadowOffsetX = 4.dp
    val ShadowOffsetY = 4.dp

    // Radius
    val RadiusSmall = 8.dp
    val RadiusMedium = 16.dp
    val RadiusLarge = 24.dp

    // Border stroke helper
    val stroke = BorderStroke(BorderWidth, NeoBrutalBlack)
    fun strokeColor(color: Color = NeoBrutalBlack) = BorderStroke(BorderWidth, color)
}

// Extension function untuk Neo Brutal shadow effect
// Cara pakai: Modifier.neoBrutalShadow()
fun Modifier.neoBrutalShadow(
    offsetX: Dp = NeoBrutal.ShadowOffsetX,
    offsetY: Dp = NeoBrutal.ShadowOffsetY,
    color: Color = ShadowColor
): Modifier = this
    .offset(x = offsetX, y = offsetY)