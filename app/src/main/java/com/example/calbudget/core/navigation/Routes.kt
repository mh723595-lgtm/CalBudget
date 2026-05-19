package com.example.calbudget.core.navigation

import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.Settings

// Sealed class lebih aman dari String biasa
// Kalau typo, langsung error saat compile, bukan saat runtime
sealed class Routes(val route: String) {

    // Auth flow
    data object Splash : Routes("splash")
    data object Login : Routes("login")
    data object Register : Routes("register")

    // Main flow (bottom nav)
    data object Home : Routes("home")
    data object Transactions : Routes("transactions")
    data object AddTransaction : Routes("add_transaction")
    data object Statistics : Routes("statistics")
    data object Settings : Routes("settings")

    // Detail screens
    data object TransactionDetail : Routes("transaction_detail/{transactionId}") {
        fun createRoute(id: String) = "transaction_detail/$id"
    }
}

// Bottom nav items — terpisah dari Routes agar mudah manage icon
sealed class BottomNavItem(
    val route: String,
    val title: String,
    val selectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val unselectedIcon: androidx.compose.ui.graphics.vector.ImageVector
) {
    data object Home : BottomNavItem(
        route = Routes.Home.route,
        title = "Beranda",
        selectedIcon = androidx.compose.material.icons.Icons.Filled.Home,
        unselectedIcon = androidx.compose.material.icons.Icons.Outlined.Home
    )
    data object Transactions : BottomNavItem(
        route = Routes.Transactions.route,
        title = "Transaksi",
        selectedIcon = androidx.compose.material.icons.Icons.Filled.Receipt,
        unselectedIcon = androidx.compose.material.icons.Icons.Outlined.Receipt
    )
    data object Statistics : BottomNavItem(
        route = Routes.Statistics.route,
        title = "Statistik",
        selectedIcon = androidx.compose.material.icons.Icons.Filled.BarChart,
        unselectedIcon = androidx.compose.material.icons.Icons.Outlined.BarChart
    )
    data object Settings : BottomNavItem(
        route = Routes.Settings.route,
        title = "Pengaturan",
        selectedIcon = androidx.compose.material.icons.Icons.Filled.Settings,
        unselectedIcon = androidx.compose.material.icons.Icons.Outlined.Settings
    )
}