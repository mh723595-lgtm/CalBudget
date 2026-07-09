package com.example.calbudget.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calbudget.data.repository.AuthRepository
import com.example.calbudget.data.repository.SettingsRepository
import com.example.calbudget.domain.model.Currency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        // Observe settings dari DataStore secara reaktif
        viewModelScope.launch {
            settingsRepository.settingsFlow.collect { settings ->
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        settings = settings,
                        // Pre-fill username input dengan nilai tersimpan
                        // hanya jika belum ada input dari user
                        usernameInput = if (state.usernameInput.isEmpty())
                            settings.username
                        else
                            state.usernameInput
                    )
                }
            }
        }
    }

    // =========================================
    // DARK MODE
    // =========================================

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setDarkMode(enabled)
            _uiState.update {
                it.copy(
                    snackbarMessage = if (enabled) "Mode gelap aktif 🌙"
                    else "Mode terang aktif ☀️"
                )
            }
        }
    }

    // =========================================
    // CURRENCY
    // =========================================

    fun showCurrencyPicker() {
        _uiState.update { it.copy(showCurrencyPicker = true) }
    }

    fun dismissCurrencyPicker() {
        _uiState.update { it.copy(showCurrencyPicker = false) }
    }

    fun setCurrency(currency: Currency) {
        viewModelScope.launch {
            settingsRepository.setCurrency(currency)
            _uiState.update {
                it.copy(
                    showCurrencyPicker = false,
                    snackbarMessage = "Mata uang diubah ke ${currency.displayName}"
                )
            }
        }
    }

    // =========================================
    // USERNAME
    // =========================================

    fun showUsernameDialog() {
        _uiState.update {
            it.copy(
                showUsernameDialog = true,
                usernameInput = it.settings.username,
                usernameError = null
            )
        }
    }

    fun dismissUsernameDialog() {
        _uiState.update {
            it.copy(showUsernameDialog = false, usernameError = null)
        }
    }

    fun onUsernameInputChange(value: String) {
        _uiState.update { it.copy(usernameInput = value, usernameError = null) }
    }

    fun saveUsername() {
        val name = _uiState.value.usernameInput.trim()

        // Validasi
        when {
            name.isBlank() -> {
                _uiState.update { it.copy(usernameError = "Nama tidak boleh kosong") }
                return
            }
            name.length < 2 -> {
                _uiState.update { it.copy(usernameError = "Nama minimal 2 karakter") }
                return
            }
            name.length > 30 -> {
                _uiState.update { it.copy(usernameError = "Nama maksimal 30 karakter") }
                return
            }
        }

        viewModelScope.launch {
            settingsRepository.setUsername(name)
            _uiState.update {
                it.copy(
                    showUsernameDialog = false,
                    usernameError = null,
                    snackbarMessage = "Nama berhasil diperbarui ✓"
                )
            }
        }
    }

    // =========================================
    // NOTIFICATION
    // =========================================

    fun toggleNotification(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setNotificationEnabled(enabled)
        }
    }

    // =========================================
    // LOGOUT
    // =========================================

    fun logout(onLogout: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout()
            onLogout()
        }
    }

    // =========================================
    // RESET DATA
    // =========================================

    fun clearAllSettings() {
        viewModelScope.launch {
            settingsRepository.clearAll()
            _uiState.update {
                it.copy(snackbarMessage = "Preferensi direset ke default")
            }
        }
    }

    // =========================================
    // FEEDBACK
    // =========================================

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }
}