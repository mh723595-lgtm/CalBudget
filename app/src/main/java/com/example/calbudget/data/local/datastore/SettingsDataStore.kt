package com.example.calbudget.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.calbudget.domain.model.AppSettings
import com.example.calbudget.domain.model.Currency
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

// Extension property — DataStore singleton per Context
// Hanya boleh ada SATU instance dengan nama yang sama
// Letakkan di luar class agar tidak duplikasi
private val Context.dataStore: DataStore<Preferences>
        by preferencesDataStore(name = "finance_settings")

@Singleton
class SettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    // =========================================
    // KEYS — semua key DataStore di satu tempat
    // Kenapa private object?
    // → Hindari typo string key
    // → Kalau key berubah, cukup ubah di sini
    // =========================================
    private object Keys {
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val CURRENCY = stringPreferencesKey("currency")
        val USERNAME = stringPreferencesKey("username")
        val IS_NOTIFICATION_ENABLED = booleanPreferencesKey("notification_enabled")
        val LANGUAGE = stringPreferencesKey("language")
    }

    // =========================================
    // READ — Flow<AppSettings>
    // .catch → kalau DataStore corrupt (IOException),
    //          emit default settings agar app tidak crash
    // =========================================
    val settingsFlow: Flow<AppSettings> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                // File corrupt → emit kosong, pakai default
                emit(emptyPreferences())
            } else {
                // Error lain → lempar ke atas
                throw exception
            }
        }
        .map { preferences ->
            AppSettings(
                isDarkMode = preferences[Keys.IS_DARK_MODE] ?: false,
                currency = try {
                    Currency.valueOf(preferences[Keys.CURRENCY] ?: Currency.IDR.name)
                } catch (e: IllegalArgumentException) {
                    Currency.IDR  // fallback kalau value tidak valid
                },
                username = preferences[Keys.USERNAME] ?: "",
                isNotificationEnabled = preferences[Keys.IS_NOTIFICATION_ENABLED] ?: true,
                language = preferences[Keys.LANGUAGE] ?: "id"
            )
        }

    // =========================================
    // WRITE — suspend functions
    // edit() = atomic write, thread-safe
    // Jangan pakai commit() seperti SharedPreferences!
    // =========================================

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.IS_DARK_MODE] = enabled
        }
    }

    suspend fun setCurrency(currency: Currency) {
        context.dataStore.edit { prefs ->
            prefs[Keys.CURRENCY] = currency.name
        }
    }

    suspend fun setUsername(name: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.USERNAME] = name
        }
    }

    suspend fun setNotificationEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.IS_NOTIFICATION_ENABLED] = enabled
        }
    }

    // Reset semua ke default — untuk fitur "reset preferensi"
    suspend fun clearAll() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}