package com.ramzez.diary.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * BroadcastReceiver, который срабатывает при нажатии на кнопку "Закрыть" в уведомлении.
 */
class NotificationDismissReceiver : BroadcastReceiver() {
    /**
     * Получает ID уведомления из интента и отменяет (закрывает) его.
     */
    override fun onReceive(context: Context, intent: Intent) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = intent.getIntExtra("NOTIFICATION_ID", 0)
        manager.cancel(notificationId)
    }
} 