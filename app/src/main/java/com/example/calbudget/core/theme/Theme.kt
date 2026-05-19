package com.example.calbudget.core.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// =========================================
// LIGHT SCHEME — Neo Brutalism utama
// =========================================
private val NeoBrutalLightColorScheme = lightColorScheme(
    primary = NeoBrutalYellow,
    onPrimary = NeoBrutalBlack,
    primaryContainer = NeoBrutalYellowDark,
    onPrimaryContainer = NeoBrutalBlack,

    secondary = NeoBrutalBlue,
    onSecondary = NeoBrutalWhite,

    tertiary = NeoBrutalGreen,
    onTertiary = NeoBrutalWhite,

    background = NeoBrutalWhite,
    onBackground = NeoBrutalBlack,

    surface = NeoBrutalWhite,
    onSurface = NeoBrutalBlack,

    surfaceVariant = NeoBrutalGray,
    onSurfaceVariant = NeoBrutalBlack,

    error = NeoBrutalRed,
    onError = NeoBrutalWhite,

    outline = NeoBrutalBlack,  // Border hitam tebal Neo Brutalism
)

// =========================================
// DARK SCHEME
// =========================================
private val NeoBrutalDarkColorScheme = darkColorScheme(
    primary = NeoBrutalYellow,
    onPrimary = NeoBrutalBlack,
    primaryContainer = NeoBrutalYellowDark,
    onPrimaryContainer = NeoBrutalBlack,

    secondary = NeoBrutalBlue,
    onSecondary = NeoBrutalWhite,

    background = Color(0xFF1A1A1A),
    onBackground = NeoBrutalWhite,

    surface = Color(0xFF2A2A2A),
    onSurface = NeoBrutalWhite,

    surfaceVariant = Color(0xFF333333),
    onSurfaceVariant = NeoBrutalWhite,

    error = NeoBrutalRed,
    outline = NeoBrutalWhite,
)

// Catatan: Perlu import Color di bawah
// tambahkan: import androidx.compose.ui.graphics.Color
// di baris import sebelum darkColorScheme

@Composable
fun FinanceAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        NeoBrutalDarkColorScheme
    } else {
        NeoBrutalLightColorScheme
    }

    // Set status bar color (transparant + icon sesuai tema)
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = android.graphics.Color.TRANSPARENT
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = FinanceTypography,
        content = content
    )
}