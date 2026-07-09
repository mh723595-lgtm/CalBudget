package com.example.calbudget.data.repository

import com.example.calbudget.domain.model.AppSettings
import com.example.calbudget.domain.model.Currency
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settingsFlow: Flow<AppSettings>

    suspend fun setDarkMode(enabled: Boolean)
    suspend fun setCurrency(currency: Currency)
    suspend fun setUsername(name: String)
    suspend fun setNotificationEnabled(enabled: Boolean)
    suspend fun clearAll()
}