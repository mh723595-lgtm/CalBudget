package com.example.calbudget.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.calbudget.core.theme.*
import com.example.calbudget.domain.model.Currency
import com.example.calbudget.presentation.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onLogout: () -> Unit = {},
    onNavigateToExport: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Snackbar saat ada pesan
    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    // ===== DIALOG UBAH NAMA =====
    if (uiState.showUsernameDialog) {
        AlertDialog(
            onDismissRequest = viewModel::dismissUsernameDialog,
            title = {
                Text(
                    "Ubah Nama",
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    NeoBrutalTextField(
                        value = uiState.usernameInput,
                        onValueChange = viewModel::onUsernameInputChange,
                        label = "Nama Tampilan",
                        placeholder = "Masukkan nama kamu",
                        errorMessage = uiState.usernameError
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = viewModel::saveUsername,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeoBrutalYellow,
                        contentColor = NeoBrutalBlack
                    )
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = viewModel::dismissUsernameDialog) {
                    Text("Batal")
                }
            }
        )
    }

    // ===== CURRENCY PICKER BOTTOM SHEET =====
    if (uiState.showCurrencyPicker) {
        ModalBottomSheet(
            onDismissRequest = viewModel::dismissCurrencyPicker,
            containerColor = NeoBrutalWhite
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    "Pilih Mata Uang",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Currency.entries.forEach { currency ->
                    val isSelected = uiState.settings.currency == currency

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (isSelected) NeoBrutalYellow.copy(alpha = 0.15f)
                                else NeoBrutalWhite,
                                RoundedCornerShape(NeoBrutal.RadiusSmall)
                            )
                            .border(
                                width = if (isSelected) NeoBrutal.BorderWidth else 0.dp,
                                color = if (isSelected) NeoBrutalBlack
                                else NeoBrutalWhite,
                                shape = RoundedCornerShape(NeoBrutal.RadiusSmall)
                            )
                            .clickable { viewModel.setCurrency(currency) }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Symbol
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    NeoBrutalGray,
                                    RoundedCornerShape(NeoBrutal.RadiusSmall)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                currency.symbol,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                currency.displayName,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                currency.code,
                                style = MaterialTheme.typography.bodySmall,
                                color = NeoBrutalBlack.copy(alpha = 0.5f)
                            )
                        }

                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(
                                        NeoBrutalBlack,
                                        RoundedCornerShape(12.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "✓",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = NeoBrutalYellow
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // ===== MAIN SCAFFOLD =====
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(NeoBrutalGray)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {

            // ===== HEADER =====
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NeoBrutalWhite)
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        "Pengaturan",
                        style = MaterialTheme.typography.headlineLarge,
                        color = NeoBrutalBlack
                    )
                    if (uiState.settings.username.isNotEmpty()) {
                        Text(
                            "Halo, ${uiState.settings.username} 👋",
                            style = MaterialTheme.typography.bodyMedium,
                            color = NeoBrutalBlack.copy(alpha = 0.5f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ===== SECTION: PROFIL =====
            SettingsSectionHeader("Profil")

            SettingsGroupCard {
                SettingsClickItem(
                    emoji = "👤",
                    title = "Nama Tampilan",
                    subtitle = uiState.settings.username.ifEmpty { "Belum diatur" },
                    onClick = viewModel::showUsernameDialog,
                    accentColor = NeoBrutalBlue
                )
                SettingsDivider()
                SettingsClickItem(
                    emoji = "💱",
                    title = "Mata Uang",
                    subtitle = "${uiState.settings.currency.symbol} · ${uiState.settings.currency.displayName}",
                    onClick = viewModel::showCurrencyPicker,
                    accentColor = NeoBrutalGreen
                )
            }

            // ===== SECTION: TAMPILAN =====
            SettingsSectionHeader("Tampilan")

            SettingsGroupCard {
                SettingsToggleItem(
                    emoji = "🌙",
                    title = "Mode Gelap",
                    subtitle = if (uiState.settings.isDarkMode) "Aktif" else "Nonaktif",
                    checked = uiState.settings.isDarkMode,
                    onCheckedChange = viewModel::toggleDarkMode,
                    accentColor = NeoBrutalPurple
                )
                SettingsDivider()
                SettingsToggleItem(
                    emoji = "🔔",
                    title = "Notifikasi",
                    subtitle = if (uiState.settings.isNotificationEnabled)
                        "Aktif" else "Nonaktif",
                    checked = uiState.settings.isNotificationEnabled,
                    onCheckedChange = viewModel::toggleNotification,
                    accentColor = NeoBrutalOrange
                )
            }

            // ===== SECTION: DATA =====
            SettingsSectionHeader("Data")

            SettingsGroupCard {
                SettingsClickItem(
                    emoji = "📤",
                    title = "Export & Backup",
                    subtitle = "CSV, PDF, dan backup data",
                    onClick = onNavigateToExport,
                    accentColor = NeoBrutalBlue
                )
            }

            // ===== SECTION: TENTANG =====
            SettingsSectionHeader("Tentang")

            SettingsGroupCard {
                SettingsClickItem(
                    emoji = "📱",
                    title = "Versi Aplikasi",
                    subtitle = "1.0.0 (Build 1)",
                    onClick = {},
                    accentColor = NeoBrutalGray
                )
                SettingsDivider()
                SettingsClickItem(
                    emoji = "🔒",
                    title = "Kebijakan Privasi",
                    subtitle = "Lihat kebijakan privasi kami",
                    onClick = {},
                    accentColor = NeoBrutalGray
                )
            }

            // ===== SECTION: AKUN =====
            SettingsSectionHeader("Akun")

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Tombol Logout
                NeoBrutalActionButton(
                    text = "🚪  Keluar dari Akun",
                    containerColor = NeoBrutalYellow,
                    contentColor = NeoBrutalBlack,
                    onClick = { viewModel.logout(onLogout) }
                )

                // Tombol Reset Preferensi (danger)
                var showResetDialog by remember { mutableStateOf(false) }

                if (showResetDialog) {
                    AlertDialog(
                        onDismissRequest = { showResetDialog = false },
                        title = { Text("Reset Preferensi?") },
                        text = {
                            Text(
                                "Semua pengaturan akan direset ke default. " +
                                        "Data transaksi tidak akan terpengaruh."
                            )
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    showResetDialog = false
                                    viewModel.clearAllSettings()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = NeoBrutalRed
                                )
                            ) {
                                Text("Reset")
                            }
                        },
                        dismissButton = {
                            OutlinedButton(onClick = { showResetDialog = false }) {
                                Text("Batal")
                            }
                        }
                    )
                }

                OutlinedButton(
                    onClick = { showResetDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = NeoBrutalRed.copy(alpha = 0.7f)
                    )
                ) {
                    Text(
                        "🗑  Reset Preferensi",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// =========================================
// SETTINGS GROUP CARD — wrapper untuk group item
// =========================================
@Composable
private fun SettingsGroupCard(
    content: @Composable ColumnScope.() -> Unit
) {
    NeoBrutalCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        backgroundColor = NeoBrutalWhite,
        shadowOffsetX = 0.dp,
        shadowOffsetY = 0.dp,
        cornerRadius = NeoBrutal.RadiusMedium
    ) {
        Column(content = content)
    }
}

// =========================================
// ACTION BUTTON — tombol besar Neo Brutal
// =========================================
@Composable
private fun NeoBrutalActionButton(
    text: String,
    containerColor: androidx.compose.ui.graphics.Color,
    contentColor: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        // Shadow
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .offset(x = 4.dp, y = 4.dp)
                .background(
                    NeoBrutalBlack,
                    RoundedCornerShape(NeoBrutal.RadiusSmall)
                )
        )
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .border(
                    NeoBrutal.BorderWidth,
                    NeoBrutalBlack,
                    RoundedCornerShape(NeoBrutal.RadiusSmall)
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = contentColor
            ),
            shape = RoundedCornerShape(NeoBrutal.RadiusSmall),
            elevation = ButtonDefaults.buttonElevation(0.dp)
        ) {
            Text(text, style = MaterialTheme.typography.labelLarge)
        }
    }
}