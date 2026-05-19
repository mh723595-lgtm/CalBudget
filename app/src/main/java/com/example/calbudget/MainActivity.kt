package com.example.calbudget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.calbudget.core.theme.CalBudgetTheme
import com.example.calbudget.core.theme.FinanceAppTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // installSplashScreen() HARUS dipanggil SEBELUM super.onCreate()
        installSplashScreen()
        super.onCreate(savedInstanceState)

        // Edge to edge = konten bisa sampai ke status bar (modern)
        enableEdgeToEdge()

        setContent {
            FinanceAppTheme {
                FinanceNavGraph()
            }
        }
    }
}
