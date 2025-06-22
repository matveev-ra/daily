package com.ramzez.diary.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Создаём экземпляр DataStore как extension property на Context.
// Это обеспечивает синглтон-доступ к хранилищу во всем приложении.
private val Context.dataStore by preferencesDataStore(name = "settings")

/**
 * Объект, содержащий ключи для доступа к настройкам в DataStore.
 */
object SettingsKeys {
    val NOTIFICATION_HOUR = intPreferencesKey("notification_hour")
    val NOTIFICATION_MINUTE = intPreferencesKey("notification_minute")
}

/**
 * Репозиторий для работы с настройками уведомлений.
 * Абстрагирует работу с DataStore от остального приложения.
 * @param context Контекст для доступа к DataStore.
 */
class SettingsRepository(private val context: Context) {

    /**
     * Flow, который эмитит время уведомления (час и минута) при его изменении.
     * Если значения не установлены, используются значения по умолчанию (8:00).
     */
    val notificationTimeFlow: Flow<Pair<Int, Int>> = context.dataStore.data
        .map { prefs ->
            val hour = prefs[SettingsKeys.NOTIFICATION_HOUR] ?: 8
            val minute = prefs[SettingsKeys.NOTIFICATION_MINUTE] ?: 0
            hour to minute
        }

    /**
     * Асинхронно сохраняет новое время уведомления в DataStore.
     */
    suspend fun saveNotificationTime(hour: Int, minute: Int) {
        context.dataStore.edit { prefs ->
            prefs[SettingsKeys.NOTIFICATION_HOUR] = hour
            prefs[SettingsKeys.NOTIFICATION_MINUTE] = minute
        }
    }
}