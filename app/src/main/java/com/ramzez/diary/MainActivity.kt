package com.ramzez.diary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ramzez.diary.ui.DailyScreen
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.ramzez.diary.notifications.QuoteReceiver
import java.util.*
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ramzez.diary.notifications.QuoteAlarmReceiver
import com.ramzez.diary.ui.AppNavGraph
import androidx.navigation.compose.rememberNavController

/**
 * Главная и единственная Activity в приложении.
 * Является точкой входа и хостом для Composable-контента.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Начиная с Android 13 (TIRAMISU), для показа уведомлений
        // необходимо запрашивать разрешение у пользователя.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }

        // Этот код для виджета, он остается
        WidgetUpdateReceiver.scheduleWidgetUpdate(this)

        setContent {
            val navController = rememberNavController()
            AppNavGraph(navController)
        }
    }

    /**
     * Обрабатывает результат запроса разрешений.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Разрешение получено. При следующем запуске будильник установится корректно
                // через логику в AppNavGraph.
            } else {
                // Разрешение не получено — уведомления не будут отображаться
            }
        }
    }
}