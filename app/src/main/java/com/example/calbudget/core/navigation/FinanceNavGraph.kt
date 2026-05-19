package com.example.calbudget.core.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.calbudget.presentation.splash.SplashScreen
import com.example.calbudget.presentation.main.MainScreen

@Composable
fun FinanceNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {
        // Splash Screen
        composable(Routes.Splash.route) {
            SplashScreen(
                onNavigateToMain = {
                    navController.navigate(Routes.Home.route) {
                        // Hapus splash dari backstack
                        // User tidak bisa back ke splash
                        popUpTo(Routes.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // Main Screen (berisi Bottom Nav)
        composable(Routes.Home.route) {
            MainScreen(rootNavController = navController)
        }

        // AddTransaction - screen full, bukan bottom nav
        composable(Routes.AddTransaction.route) {
            com.example.calbudget.presentation.transactions.AddTransactionScreen(
                onNavigateBack = {navController.popBackStack()}
            )
        }
    }
}