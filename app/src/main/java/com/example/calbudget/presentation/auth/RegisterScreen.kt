package com.example.calbudget.presentation.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.calbudget.core.theme.NeoBrutal
import com.example.calbudget.core.theme.NeoBrutalBlack
import com.example.calbudget.core.theme.NeoBrutalRed
import com.example.calbudget.core.theme.NeoBrutalWhite
import com.example.calbudget.core.theme.NeoBrutalYellow
import com.example.calbudget.presentation.components.NeoBrutalCard
import com.example.calbudget.presentation.components.NeoBrutalTextField

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            viewModel.resetSuccessState()
            onRegisterSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NeoBrutalYellow)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Back button
            Row(modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = onNavigateToLogin) {
                    Icon(Icons.Default.ArrowBack, "Kembali", tint = NeoBrutalBlack)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Buat Akun Baru",
                style = MaterialTheme.typography.displayMedium,
                color = NeoBrutalBlack,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Daftar gratis, mulai catat keuangan",
                style = MaterialTheme.typography.bodyLarge,
                color = NeoBrutalBlack.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            NeoBrutalCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = NeoBrutalWhite
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                    // Nama
                    NeoBrutalTextField(
                        value = state.displayName,
                        onValueChange = viewModel::onDisplayNameChange,
                        label = "Nama Lengkap",
                        placeholder = "Nama kamu",
                        errorMessage = state.displayNameError
                    )

                    // Email
                    NeoBrutalTextField(
                        value = state.email,
                        onValueChange = viewModel::onEmailChange,
                        label = "Email",
                        placeholder = "contoh@email.com",
                        errorMessage = state.emailError,
                        keyboardType = KeyboardType.Email
                    )

                    // Password
                    PasswordField(
                        value = state.password,
                        onValueChange = viewModel::onPasswordChange,
                        label = "Password",
                        placeholder = "Minimal 6 karakter",
                        isVisible = state.isPasswordVisible,
                        onToggleVisibility = viewModel::togglePasswordVisibility,
                        errorMessage = state.passwordError
                    )

                    // Confirm Password
                    PasswordField(
                        value = state.confirmPassword,
                        onValueChange = viewModel::onConfirmPasswordChange,
                        label = "Konfirmasi Password",
                        placeholder = "Ulangi password",
                        isVisible = state.isPasswordVisible,
                        onToggleVisibility = viewModel::togglePasswordVisibility,
                        errorMessage = state.confirmPasswordError
                    )

                    // Error global
                    AnimatedVisibility(visible = state.errorMessage != null) {
                        NeoBrutalCard(
                            backgroundColor = NeoBrutalRed.copy(alpha = 0.1f),
                            borderColor = NeoBrutalRed
                        ) {
                            Text(
                                "⚠ ${state.errorMessage}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = NeoBrutalRed
                            )
                        }
                    }

                    // Tombol Daftar
                    NeoBrutalButton(
                        text = if (state.isLoading) "Mendaftar..." else "Daftar Sekarang",
                        onClick = viewModel::register,
                        isLoading = state.isLoading,
                        backgroundColor = NeoBrutalBlack,
                        textColor = NeoBrutalWhite
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onNavigateToLogin) {
                Text("Sudah punya akun? ", style = MaterialTheme.typography.bodyMedium,
                    color = NeoBrutalBlack)
                Text("Masuk", style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
                    color = NeoBrutalBlack)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    isVisible: Boolean,
    onToggleVisibility: () -> Unit,
    errorMessage: String?
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = NeoBrutal.BorderWidth,
                    color = if (errorMessage != null) NeoBrutalRed else NeoBrutalBlack,
                    shape = RoundedCornerShape(NeoBrutal.RadiusSmall)
                ),
            placeholder = { Text(placeholder) },
            visualTransformation = if (isVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = onToggleVisibility) {
                    Icon(
                        if (isVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                unfocusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                errorBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                focusedContainerColor = NeoBrutalWhite,
                unfocusedContainerColor = NeoBrutalWhite
            ),
            shape = RoundedCornerShape(NeoBrutal.RadiusSmall),
            isError = errorMessage != null,
            singleLine = true,
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                keyboardType = KeyboardType.Password
            )
        )
        if (errorMessage != null) {
            Text("⚠ $errorMessage", style = MaterialTheme.typography.bodySmall,
                color = NeoBrutalRed)
        }
    }
}