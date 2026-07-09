package com.example.calbudget.data.repository

import com.example.calbudget.data.local.datastore.SettingsDataStore
import com.example.calbudget.domain.model.AppSettings
import com.example.calbudget.domain.model.Currency
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: SettingsDataStore
) : SettingsRepository {

    override val settingsFlow: Flow<AppSettings> = dataStore.settingsFlow

    override suspend fun setDarkMode(enabled: Boolean) =
        dataStore.setDarkMode(enabled)

    override suspend fun setCurrency(currency: Currency) =
        dataStore.setCurrency(currency)

    override suspend fun setUsername(name: String) =
        dataStore.setUsername(name.trim())  // trim spasi

    override suspend fun setNotificationEnabled(enabled: Boolean) =
        dataStore.setNotificationEnabled(enabled)

    override suspend fun clearAll() =
        dataStore.clearAll()
}