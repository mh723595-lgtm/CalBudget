package com.example.calbudget.presentation.auth

import com.example.calbudget.domain.model.User

// State untuk seluruh auth flow
data class AuthUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,

    // Form fields — dipakai oleh login & register
    val email: String = "",
    val password: String = "",
    val displayName: String = "",      // register only
    val confirmPassword: String = "",  // register only
    val isPasswordVisible: Boolean = false,

    // Validation errors
    val emailError: String? = null,
    val passwordError: String? = null,
    val displayNameError: String? = null,
    val confirmPasswordError: String? = null
)

// Reset password state
data class ResetPasswordUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val email: String = ""
)