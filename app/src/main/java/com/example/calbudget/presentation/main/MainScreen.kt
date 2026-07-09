package com.example.calbudget.presentation.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.calbudget.core.navigation.BottomNavItem
import com.example.calbudget.core.navigation.Routes
import com.example.calbudget.presentation.components.NeoBrutalBottomNavBar
import com.example.calbudget.presentation.home.HomeScreen
import com.example.calbudget.presentation.settings.SettingsScreen
import com.example.calbudget.presentation.statistics.StatisticsScreen
import com.example.calbudget.presentation.transactions.TransactionsScreen

@Composable
fun MainScreen(
    rootNavController: NavHostController,
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Transactions,
        BottomNavItem.Statistics,
        BottomNavItem.Settings
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NeoBrutalBottomNavBar(
                items = bottomNavItems,
                currentDestination = currentDestination,
                onItemClick = { item ->
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.Home.route) {
                HomeScreen(
                    onNavigateToTransactions = {
                        // Navigasi ke tab Transaksi
                        navController.navigate(Routes.Transactions.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onLogout = onLogout
                )
            }
            composable(Routes.Transactions.route) {
                TransactionsScreen(
                    onNavigateToAdd = {
                        rootNavController.navigate(Routes.AddTransaction.route)
                    }
                )
            }
            composable(Routes.Statistics.route) { StatisticsScreen() }
            composable(Routes.Settings.route) {
                SettingsScreen(
                    onLogout = onLogout,
                    onNavigateToExport = {
//                        rootNavController.navigate(Routes.Export.route)
                    }
                )
            }
        }
    }
}