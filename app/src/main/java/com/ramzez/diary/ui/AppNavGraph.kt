package com.ramzez.diary.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ramzez.diary.ui.DailyScreen
import com.ramzez.diary.ui.SettingsScreen
import com.ramzez.diary.viewmodel.SettingsViewModel
import com.ramzez.diary.data.SettingsRepository

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "daily") {
        composable("daily") {
            DailyScreen(
                onOpenSettings = { navController.navigate("settings") }
            )
        }
        composable("settings") {
            // Создаём SettingsViewModel с параметрами через factory
            val viewModel: SettingsViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        @Suppress("UNCHECKED_CAST")
                        return SettingsViewModel(
                            navController.context.applicationContext,
                            SettingsRepository(navController.context.applicationContext)
                        ) as T
                    }
                }
            )
            SettingsScreen(
                onBack = { navController.popBackStack() },
                viewModel = viewModel
            )
        }
    }
}