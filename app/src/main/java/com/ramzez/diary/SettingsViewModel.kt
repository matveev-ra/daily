package com.ramzez.diary.viewmodel

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramzez.diary.data.SettingsRepository
import com.ramzez.diary.notifications.QuoteAlarmReceiver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*

/**
 * ViewModel для экрана настроек ([com.ramzez.diary.ui.SettingsScreen]).
 * Управляет логикой сохранения и планирования времени уведомлений.
 */
class SettingsViewModel(
    private val context: Context,
    private val repository: SettingsRepository
) : ViewModel() {

    private val _notificationHour = MutableStateFlow(8)
    val notificationHour: StateFlow<Int> = _notificationHour

    private val _notificationMinute = MutableStateFlow(0)
    val notificationMinute: StateFlow<Int> = _notificationMinute

    init {
        // Подписываемся на изменения времени в репозитории
        // и обновляем StateFlow при получении новых данных.
        repository.notificationTimeFlow.onEach { (hour, minute) ->
            _notificationHour.value = hour
            _notificationMinute.value = minute
        }.launchIn(viewModelScope)
    }

    /**
     * Проверяет, установлен ли уже будильник для уведомлений.
     * Если нет, устанавливает его на основе сохраненного времени.
     * Используется флаг `FLAG_NO_CREATE`, чтобы только проверить существование PendingIntent, не создавая новый.
     */
    fun scheduleInitialQuoteIfNotSet() {
        val intent = Intent(context, QuoteAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )

        // Если будильник еще не установлен (pendingIntent == null), то планируем его.
        if (pendingIntent == null) {
            viewModelScope.launch {
                repository.notificationTimeFlow.collect { (hour, minute) ->
                    scheduleDailyQuote(hour, minute)
                }
            }
        }
    }

    /**
     * Сохраняет новое время и немедленно переустанавливает будильник.
     */
    fun saveNotificationTime(hour: Int, minute: Int) {
        viewModelScope.launch {
            repository.saveNotificationTime(hour, minute)
            scheduleDailyQuote(hour, minute)
        }
    }

    /**
     * Планирует повторяющийся ежедневный будильник (`AlarmManager`)
     * для срабатывания [QuoteAlarmReceiver].
     * @param hour Час для срабатывания уведомления.
     * @param minute Минута для срабатывания уведомления.
     */
    private fun scheduleDailyQuote(hour: Int, minute: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, QuoteAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Устанавливаем время, учитывая, что если оно уже прошло сегодня,
        // то первое срабатывание будет завтра.
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