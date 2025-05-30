package com.ramzez.diary.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class QuoteAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val quote = getTodayQuote(context) // Здесь получаем цитату
        QuoteNotification.show(context, quote)
    }

    private fun getTodayQuote(context: Context): String {
        // Пример простой цитаты — можешь подключить JSON, как в ViewModel
        return "С ним победят призванные, избранные и верны (Отк. 17:14)"
    }
}
