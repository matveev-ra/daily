package com.ramzez.diary.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ramzez.diary.utils.QuoteUtils

/**
 * BroadcastReceiver, который срабатывает по ежедневному будильнику.
 * Его единственная задача - получить сегодняшнюю цитату и показать уведомление.
 */
class QuoteAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val quote = QuoteUtils.getTodayQuote(context)
        QuoteNotification.show(context, quote)
    }
} 