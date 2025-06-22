package com.ramzez.diary.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ramzez.diary.data.SettingsRepository
import com.ramzez.diary.viewmodel.SettingsViewModel

/**
 * Определяет граф навигации для всего приложения.
 * @param navController Контроллер, управляющий навигацией.
 */
@Composable
fun AppNavGraph(navController: NavHostController) {
    // Создаем SettingsViewModel здесь (на уровне графа), чтобы она была общей
    // для всех экранов и жила до тех пор, пока жив сам граф.
    // Это позволяет ей выполнять фоновые задачи (проверку будильника)
    // и предоставлять единый источник данных для экрана настроек.
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

    // LaunchedEffect с ключом Unit выполнится один раз при первой композиции,
    // что идеально подходит для инициализации фоновых проверок.
    LaunchedEffect(Unit) {
        viewModel.scheduleInitialQuoteIfNotSet()
    }

    NavHost(navController = navController, startDestination = "daily") {
        composable("daily") {
            DailyScreen(
                onOpenSettings = { navController.navigate("settings") }
            )
        }
        composable("settings") {
            // Передаем уже существующую ViewModel на экран настроек.
            SettingsScreen(
                onBack = { navController.popBackStack() },
                viewModel = viewModel
            )
        }
    }
}