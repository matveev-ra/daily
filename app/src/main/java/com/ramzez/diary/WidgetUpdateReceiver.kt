package com.ramzez.diary

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import java.util.*

/**
 * BroadcastReceiver, отвечающий за планирование точного обновления виджета
 * один раз в день (в полночь).
 */
class WidgetUpdateReceiver : BroadcastReceiver() {
    
    companion object {
        private const val WIDGET_UPDATE_ACTION = "com.ramzez.diary.WIDGET_UPDATE"
        
        /**
         * Планирует следующее обновление виджета на полночь с помощью AlarmManager.
         * Использует `setExactAndAllowWhileIdle` для точного срабатывания даже в режиме Doze.
         */
        fun scheduleWidgetUpdate(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, WidgetUpdateReceiver::class.java).apply {
                action = WIDGET_UPDATE_ACTION
            }
            
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            
            // Устанавливаем время на полночь следующего дня
            val calendar = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_MONTH, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }
    
    /**
     * Вызывается, когда срабатывает будильник.
     * Этот метод инициирует обновление виджета и планирует следующее обновление.
     */
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == WIDGET_UPDATE_ACTION) {
            // Инициируем обновление всех экземпляров QuoteWidget.
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                ComponentName(context, QuoteWidget::class.java)
            )
            
            if (appWidgetIds.isNotEmpty()) {
                val widgetIntent = Intent(context, QuoteWidget::class.java).apply {
                    action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
                }
                context.sendBroadcast(widgetIntent)
            }
            
            // Сразу же планируем следующее обновление на завтрашнюю полночь.
            scheduleWidgetUpdate(context)
        }
    }
}