package com.ramzez.diary.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Создаём DataStore как extension property на Context
private val Context.dataStore by preferencesDataStore(name = "settings")

object SettingsKeys {
    val NOTIFICATION_HOUR = intPreferencesKey("notification_hour")
    val NOTIFICATION_MINUTE = intPreferencesKey("notification_minute")
}

class SettingsRepository(private val context: Context) {

    val notificationTimeFlow: Flow<Pair<Int, Int>> = context.dataStore.data
        .map { prefs ->
            val hour = prefs[SettingsKeys.NOTIFICATION_HOUR] ?: 8
            val minute = prefs[SettingsKeys.NOTIFICATION_MINUTE] ?: 0
            hour to minute
        }

    suspend fun saveNotificationTime(hour: Int, minute: Int) {
        context.dataStore.edit { prefs ->
            prefs[SettingsKeys.NOTIFICATION_HOUR] = hour
            prefs[SettingsKeys.NOTIFICATION_MINUTE] = minute
        }
    }
}