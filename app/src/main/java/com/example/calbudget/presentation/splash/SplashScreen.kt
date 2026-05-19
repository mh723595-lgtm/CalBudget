package com.example.calbudget.presentation.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.offset
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import com.example.calbudget.core.theme.*
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    onNavigateToMain: () -> Unit
) {
    // State untuk animasi
    val scale = remember { Animatable(0.5f) }
    val offsetY = remember { Animatable(50f) }

    LaunchedEffect(Unit) {
        // Animasi muncul
        launch {
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        }
        launch {
            offsetY.animateTo(
                targetValue = 0f,
                animationSpec = tween(600, easing = EaseOutCubic)
            )
        }

        // Tunggu 2 detik, lalu navigate
        delay(2000)
        onNavigateToMain()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NeoBrutalYellow),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .scale(scale.value)
                .offset(y = offsetY.value.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Logo card Neo Brutalism
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .offset(x = 4.dp, y = 4.dp)  // Shadow
                    .background(
                        color = NeoBrutalBlack,
                        shape = RoundedCornerShape(NeoBrutal.RadiusMedium)
                    )
            )
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .offset(y = (-4).dp)  // Naik dari shadow
                    .background(
                        color = NeoBrutalWhite,
                        shape = RoundedCornerShape(NeoBrutal.RadiusMedium)
                    )
                    .border(
                        width = NeoBrutal.BorderWidth,
                        color = NeoBrutalBlack,
                        shape = RoundedCornerShape(NeoBrutal.RadiusMedium)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "💰",
                    style = MaterialTheme.typography.displayMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // App title
            Text(
                text = "FINANCE",
                style = MaterialTheme.typography.displayMedium.copy(
                    color = NeoBrutalBlack
                )
            )
            Text(
                text = "Catat. Kelola. Cuan.",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = NeoBrutalBlack
                )
            )
        }
    }
}