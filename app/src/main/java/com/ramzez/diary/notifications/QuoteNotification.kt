package com.ramzez.diary.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.ramzez.diary.MainActivity
import com.ramzez.diary.R

/**
 * Утилитарный объект для создания и отображения уведомлений с цитатой дня.
 */
object QuoteNotification {

    private const val CHANNEL_ID = "quote_channel"
    private const val NOTIFICATION_ID = 1

    /**
     * Создает и показывает "постоянное" уведомление.
     * @param context Контекст для доступа к системным службам.
     * @param quote Текст цитаты для отображения.
     */
    fun show(context: Context, quote: String) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // На Android 8+ необходимо создать канал для уведомлений.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Цитаты дня",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }

        // Интент на открытие MainActivity при клике на тело уведомления.
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Интент для кнопки "Закрыть", который будет обработан NotificationDismissReceiver.
        val dismissIntent = Intent(context, NotificationDismissReceiver::class.java).apply {
            putExtra("NOTIFICATION_ID", NOTIFICATION_ID)
        }
        val dismissPendingIntent = PendingIntent.getBroadcast(
            context,
            NOTIFICATION_ID,
            dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Стих на день")
            .setContentText(quote)
            .setSmallIcon(R.drawable.ic_notification)
            .setStyle(NotificationCompat.BigTextStyle().bigText(quote)) // Позволяет отображать длинный текст
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(false) // Уведомление не закроется по клику на него
            .setOngoing(true)     // Уведомление нельзя будет смахнуть (убрать свайпом)
            .addAction(0, "Закрыть", dismissPendingIntent) // Добавляем кнопку "Закрыть"
            .build()

        manager.notify(NOTIFICATION_ID, notification)
    }
} 