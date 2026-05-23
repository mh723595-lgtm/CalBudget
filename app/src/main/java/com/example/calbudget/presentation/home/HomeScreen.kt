package com.example.calbudget.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.calbudget.core.theme.NeoBrutalBlack
import com.example.calbudget.core.theme.NeoBrutalRed
import com.example.calbudget.core.theme.NeoBrutalWhite
import com.example.calbudget.core.theme.NeoBrutalYellow
import com.example.calbudget.presentation.auth.AuthViewModel
import com.example.calbudget.presentation.auth.NeoBrutalButton
import com.example.calbudget.presentation.components.NeoBrutalCard

@Composable
fun HomeScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    onLogout: () -> Unit = {}
) {
    val user by authViewModel.currentUser.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NeoBrutalWhite)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Beranda",
            style = MaterialTheme.typography.headlineLarge,
            color = NeoBrutalBlack
        )

        // User info card
        user?.let { u ->
            NeoBrutalCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = NeoBrutalYellow
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("👋 Halo, ${u.displayName}!",
                        style = MaterialTheme.typography.headlineSmall)
                    Text(u.email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = NeoBrutalBlack.copy(alpha = 0.7f))
                }
            }
        }

        Text(
            "Dashboard lengkap akan hadir di Tahap 5 🚀",
            style = MaterialTheme.typography.bodyLarge,
            color = NeoBrutalBlack.copy(alpha = 0.5f)
        )

        Spacer(modifier = Modifier.weight(1f))

        // Logout
        NeoBrutalButton(
            text = "Keluar",
            onClick = onLogout,
            isLoading = false,
            backgroundColor = NeoBrutalRed,
            textColor = NeoBrutalWhite
        )
    }
}