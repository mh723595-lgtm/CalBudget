package com.example.calbudget.core.navigation


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.calbudget.presentation.auth.AuthViewModel
import com.example.calbudget.presentation.auth.LoginScreen
import com.example.calbudget.presentation.auth.RegisterScreen
import com.example.calbudget.presentation.splash.SplashScreen
import com.example.calbudget.presentation.main.MainScreen
import com.example.calbudget.presentation.transactions.AddTransactionScreen

@Composable
fun FinanceNavGraph(
    navController: NavHostController = rememberNavController()
) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val currentUser by authViewModel.currentUser.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {
        // Splash — tentukan ke mana arahnya
        composable(Routes.Splash.route) {
            SplashScreen(
                onNavigateToMain = {
                    val destination = if (currentUser != null) Routes.Home.route
                    else Routes.Login.route
                    navController.navigate(destination) {
                        popUpTo(Routes.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // Login
        composable(Routes.Login.route) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Routes.Register.route)
                },
                onLoginSuccess = {
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Register
        composable(Routes.Register.route) {
            RegisterScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Main (Bottom Nav)
        composable(Routes.Home.route) {
            MainScreen(
                rootNavController = navController,
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Routes.Login.route) {
                        popUpTo(Routes.Home.route) { inclusive = true }
                    }
                }
            )
        }

        // Add Transaction
        composable(Routes.AddTransaction.route) {
            AddTransactionScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}