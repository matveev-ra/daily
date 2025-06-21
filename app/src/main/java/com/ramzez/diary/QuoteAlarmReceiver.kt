package com.ramzez.diary.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ramzez.diary.utils.QuoteUtils

class QuoteAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val quote = QuoteUtils.getTodayQuote(context) // Загружаем цитату из JSON
        QuoteNotification.show(context, quote)
    }
}
