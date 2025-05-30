// QuoteReceiver.kt
package com.ramzez.diary.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class QuoteReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val quote = "С ним победят призванные, избранные и верны (Отк. 17:14)" // Здесь можно подгружать из JSON
        QuoteNotification.show(context, quote)
    }
}
