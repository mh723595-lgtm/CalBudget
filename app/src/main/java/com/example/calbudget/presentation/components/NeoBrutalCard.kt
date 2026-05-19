package com.example.calbudget.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.calbudget.core.theme.*

// Reusable Neo Brutal Card
// Cara pakai:
// NeoBrutalCard { Text("isi card") }
@Composable
fun NeoBrutalCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = NeoBrutalWhite,
    borderColor: Color = NeoBrutalBlack,
    shadowColor: Color = NeoBrutalBlack,
    cornerRadius: Dp = NeoBrutal.RadiusMedium,
    shadowOffsetX: Dp = NeoBrutal.ShadowOffsetX,
    shadowOffsetY: Dp = NeoBrutal.ShadowOffsetY,
    content: @Composable BoxScope.() -> Unit
) {
    // Teknik Neo Brutalism: buat "shadow" dengan box offset di belakang
    Box(
        modifier = modifier
    ) {
        // Layer shadow (box gelap di belakang)
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = shadowOffsetX, y = shadowOffsetY)
                .background(
                    color = shadowColor,
                    shape = RoundedCornerShape(cornerRadius)
                )
        )

        // Layer card utama (di depan shadow)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(cornerRadius)
                )
                .border(
                    width = NeoBrutal.BorderWidth,
                    color = borderColor,
                    shape = RoundedCornerShape(cornerRadius)
                )
                .padding(16.dp),
            content = content
        )
    }
}