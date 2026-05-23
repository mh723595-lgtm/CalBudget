package com.example.calbudget.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calbudget.data.repository.AuthRepository
import com.example.calbudget.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    // Observe auth state — reaktif terhadap login/logout
    val currentUser: StateFlow<User?> = authRepository.currentUser
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = authRepository.getCurrentUserOnce()
        )

    // =========================================
    // FORM EVENTS
    // =========================================

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, emailError = null, errorMessage = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, passwordError = null) }
    }

    fun onDisplayNameChange(value: String) {
        _uiState.update { it.copy(displayName = value, displayNameError = null) }
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update { it.copy(confirmPassword = value, confirmPasswordError = null) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    // =========================================
    // LOGIN
    // =========================================

    fun loginWithEmail() {
        val state = _uiState.value

        // Validasi
        val emailError = validateEmail(state.email)
        val passwordError = if (state.password.isBlank()) "Password tidak boleh kosong" else null

        if (emailError != null || passwordError != null) {
            _uiState.update {
                it.copy(emailError = emailError, passwordError = passwordError)
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            authRepository.loginWithEmail(state.email.trim(), state.password)
                .onSuccess { user ->
                    _uiState.update { it.copy(isLoading = false, isSuccess = true, user = user) }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = e.message)
                    }
                }
        }
    }

    // =========================================
    // GOOGLE SIGN-IN
    // =========================================

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            authRepository.loginWithGoogle(idToken)
                .onSuccess { user ->
                    _uiState.update { it.copy(isLoading = false, isSuccess = true, user = user) }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = e.message)
                    }
                }
        }
    }

    // =========================================
    // REGISTER
    // =========================================

    fun register() {
        val state = _uiState.value

        val displayNameError = if (state.displayName.isBlank()) "Nama tidak boleh kosong" else null
        val emailError = validateEmail(state.email)
        val passwordError = when {
            state.password.isBlank() -> "Password tidak boleh kosong"
            state.password.length < 6 -> "Password minimal 6 karakter"
            else -> null
        }
        val confirmError = when {
            state.confirmPassword.isBlank() -> "Konfirmasi password wajib diisi"
            state.confirmPassword != state.password -> "Password tidak cocok"
            else -> null
        }

        if (displayNameError != null || emailError != null ||
            passwordError != null || confirmError != null
        ) {
            _uiState.update {
                it.copy(
                    displayNameError = displayNameError,
                    emailError = emailError,
                    passwordError = passwordError,
                    confirmPasswordError = confirmError
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            authRepository.registerWithEmail(
                email = state.email.trim(),
                password = state.password,
                displayName = state.displayName.trim()
            )
                .onSuccess { user ->
                    _uiState.update { it.copy(isLoading = false, isSuccess = true, user = user) }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = e.message)
                    }
                }
        }
    }

    // =========================================
    // LOGOUT
    // =========================================

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _uiState.value = AuthUiState()
        }
    }

    // =========================================
    // RESET PASSWORD
    // =========================================

    private val _resetState = MutableStateFlow(ResetPasswordUiState())
    val resetState: StateFlow<ResetPasswordUiState> = _resetState.asStateFlow()

    fun sendResetPassword(email: String) {
        if (email.isBlank()) {
            _resetState.update { it.copy(errorMessage = "Masukkan email terlebih dahulu") }
            return
        }
        viewModelScope.launch {
            _resetState.update { it.copy(isLoading = true) }
            authRepository.resetPassword(email)
                .onSuccess {
                    _resetState.update { it.copy(isLoading = false, isSuccess = true) }
                }
                .onFailure { e ->
                    _resetState.update { it.copy(isLoading = false, errorMessage = e.message) }
                }
        }
    }

    // =========================================
    // HELPERS
    // =========================================

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun resetSuccessState() {
        _uiState.update { it.copy(isSuccess = false) }
    }

    private fun validateEmail(email: String): String? = when {
        email.isBlank() -> "Email tidak boleh kosong"
        !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Format email tidak valid"
        else -> null
    }
}