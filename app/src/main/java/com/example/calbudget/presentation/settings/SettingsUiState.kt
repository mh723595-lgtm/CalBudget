package com.example.calbudget.presentation.settings

import com.example.calbudget.domain.model.AppSettings
import com.example.calbudget.domain.model.Currency

data class SettingsUiState(
    val isLoading: Boolean = true,
    val settings: AppSettings = AppSettings(),

    // Dialog ubah username
    val showUsernameDialog: Boolean = false,
    val usernameInput: String = "",
    val usernameError: String? = null,

    // Bottom sheet pilih currency
    val showCurrencyPicker: Boolean = false,

    // Snackbar feedback
    val snackbarMessage: String? = null
)