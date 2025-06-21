package com.ramzez.diary.viewmodel

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramzez.diary.notifications.QuoteAlarmReceiver
import com.ramzez.diary.data.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*

class SettingsViewModel(
    private val context: Context,
    private val repository: SettingsRepository
) : ViewModel() {

    private val _notificationHour = MutableStateFlow(8)
    val notificationHour: StateFlow<Int> = _notificationHour

    private val _notificationMinute = MutableStateFlow(0)
    val notificationMinute: StateFlow<Int> = _notificationMinute

    init {
        repository.notificationTimeFlow.onEach { (hour, minute) ->
            _notificationHour.value = hour
            _notificationMinute.value = minute
        }.launchIn(viewModelScope)
    }

    fun saveNotificationTime(hour: Int, minute: Int) {
        viewModelScope.launch {
            repository.saveNotificationTime(hour, minute)
            scheduleDailyQuote(hour, minute)
        }
    }

    private fun scheduleDailyQuote(hour: Int, minute: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, QuoteAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
}