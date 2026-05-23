package com.example.calbudget.presentation.auth

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.example.calbudget.R
import com.example.calbudget.core.theme.*
import com.example.calbudget.presentation.components.NeoBrutalCard
import com.example.calbudget.presentation.components.NeoBrutalTextField

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Navigate saat login berhasil
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            viewModel.resetSuccessState()
            onLoginSuccess()
        }
    }

    // Google Sign-In launcher
    val googleSignInClient = remember {
        GoogleSignIn.getClient(
            context,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        )
    }

    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                val account = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    .getResult(ApiException::class.java)
                account.idToken?.let { token ->
                    viewModel.loginWithGoogle(token)
                }
            } catch (e: ApiException) {
                // Google sign-in gagal — tampilkan error
            }
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // ===== HEADER =====
            Text(
                text = "💰",
                style = MaterialTheme.typography.displayLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Selamat Datang",
                style = MaterialTheme.typography.displayMedium,
                color = NeoBrutalBlack,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Masuk ke akun kamu",
                style = MaterialTheme.typography.bodyLarge,
                color = NeoBrutalBlack.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ===== FORM CARD =====
            NeoBrutalCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = NeoBrutalWhite
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

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
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Password", style = MaterialTheme.typography.titleMedium)
                        OutlinedTextField(
                            value = state.password,
                            onValueChange = viewModel::onPasswordChange,
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = NeoBrutal.BorderWidth,
                                    color = if (state.passwordError != null) NeoBrutalRed
                                    else NeoBrutalBlack,
                                    shape = RoundedCornerShape(NeoBrutal.RadiusSmall)
                                ),
                            placeholder = { Text("Minimal 6 karakter") },
                            visualTransformation = if (state.isPasswordVisible)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = viewModel::togglePasswordVisibility) {
                                    Icon(
                                        imageVector = if (state.isPasswordVisible)
                                            Icons.Default.VisibilityOff
                                        else
                                            Icons.Default.Visibility,
                                        contentDescription = "Toggle password"
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
                            isError = state.passwordError != null,
                            singleLine = true,
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                keyboardType = KeyboardType.Password
                            )
                        )
                        if (state.passwordError != null) {
                            Text(
                                "⚠ ${state.passwordError}",
                                style = MaterialTheme.typography.bodySmall,
                                color = NeoBrutalRed
                            )
                        }
                    }

                    // Error message global
                    AnimatedVisibility(visible = state.errorMessage != null) {
                        NeoBrutalCard(
                            backgroundColor = NeoBrutalRed.copy(alpha = 0.1f),
                            borderColor = NeoBrutalRed
                        ) {
                            Text(
                                text = "⚠ ${state.errorMessage}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = NeoBrutalRed
                            )
                        }
                    }

                    // Tombol Login
                    NeoBrutalButton(
                        text = if (state.isLoading) "Masuk..." else "Masuk",
                        onClick = viewModel::loginWithEmail,
                        isLoading = state.isLoading,
                        backgroundColor = NeoBrutalBlack,
                        textColor = NeoBrutalWhite
                    )

                    // Divider
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f))
                        Text("atau", style = MaterialTheme.typography.bodySmall,
                            color = NeoBrutalBlack.copy(alpha = 0.5f))
                        HorizontalDivider(modifier = Modifier.weight(1f))
                    }

                    // Google Sign-In
                    NeoBrutalButton(
                        text = "  Masuk dengan Google",
                        onClick = {
                            googleSignInClient.signOut()  // clear previous account
                            googleLauncher.launch(googleSignInClient.signInIntent)
                        },
                        isLoading = false,
                        backgroundColor = NeoBrutalWhite,
                        textColor = NeoBrutalBlack,
                        prefix = "🔵"
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Navigasi ke Register
            TextButton(onClick = onNavigateToRegister) {
                Text(
                    "Belum punya akun? ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = NeoBrutalBlack
                )
                Text(
                    "Daftar sekarang",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    ),
                    color = NeoBrutalBlack
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// =========================================
// REUSABLE BUTTON — Neo Brutal style
// =========================================
@Composable
fun NeoBrutalButton(
    text: String,
    onClick: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    backgroundColor: androidx.compose.ui.graphics.Color = NeoBrutalYellow,
    textColor: androidx.compose.ui.graphics.Color = NeoBrutalBlack,
    prefix: String = ""
) {
    Box(modifier = modifier.fillMaxWidth()) {
        // Shadow
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .offset(x = 4.dp, y = 4.dp)
                .background(NeoBrutalBlack, RoundedCornerShape(NeoBrutal.RadiusSmall))
        )
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .border(NeoBrutal.BorderWidth, NeoBrutalBlack,
                    RoundedCornerShape(NeoBrutal.RadiusSmall)),
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = backgroundColor,
                contentColor = textColor
            ),
            shape = RoundedCornerShape(NeoBrutal.RadiusSmall),
            elevation = ButtonDefaults.buttonElevation(0.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = textColor,
                    strokeWidth = 2.dp
                )
            } else {
                if (prefix.isNotEmpty()) Text(prefix)
                Text(text, style = MaterialTheme.typography.labelLarge, color = textColor)
            }
        }
    }
}