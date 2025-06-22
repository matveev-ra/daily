package com.ramzez.diary.ui

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ramzez.diary.viewmodel.SettingsViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

/**
 * Экран настроек приложения.
 * Позволяет пользователю изменять время ежедневных уведомлений.
 * @param onBack Лямбда-функция для навигации назад.
 * @param viewModel ViewModel, предоставляющая данные о времени и логику сохранения.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel
) {
    val context = LocalContext.current

    val hour by viewModel.notificationHour.collectAsState()
    val minute by viewModel.notificationMinute.collectAsState()

    var showTimePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Настройки") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Время уведомления: %02d:%02d".format(hour, minute),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { showTimePicker = true }) {
                Text("Выбрать время")
            }
        }
    }

    if (showTimePicker) {
        TimePickerDialog(
            context,
            { _, selectedHour, selectedMinute ->
                viewModel.saveNotificationTime(selectedHour, selectedMinute)
                showTimePicker = false
            },
            hour,
            minute,
            true
        ).show()
    }
}