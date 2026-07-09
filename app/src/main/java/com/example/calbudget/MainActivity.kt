package com.example.calbudget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.calbudget.core.navigation.FinanceNavGraph
import com.example.calbudget.core.theme.FinanceAppTheme
import com.example.calbudget.data.repository.SettingsRepository
import com.example.calbudget.domain.model.AppSettings
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settingsRepository: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        // installSplashScreen() HARUS dipanggil SEBELUM super.onCreate()
        installSplashScreen()
        super.onCreate(savedInstanceState)

        // Edge to edge = konten bisa sampai ke status bar (modern)
        enableEdgeToEdge()

        setContent {
            val settings by settingsRepository.settingsFlow
                .collectAsState(initial= AppSettings())
            FinanceAppTheme(darkTheme = settings.isDarkMode) {
                FinanceNavGraph()
            }
        }
    }
}
