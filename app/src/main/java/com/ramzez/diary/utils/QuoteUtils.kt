package com.ramzez.diary.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ramzez.diary.model.DailyEntry
import java.io.InputStreamReader
import java.text.DateFormatSymbols
import java.time.LocalDate
import java.util.*

/**
 * Утилитарный объект для получения цитат из JSON-файла в `assets`.
 */
object QuoteUtils {

    /**
     * Получает цитату для сегодняшней даты.
     * Является удобной оберткой для [getQuoteForDate].
     */
    fun getTodayQuote(context: Context): String {
        return getQuoteForDate(context, LocalDate.now())
    }

    /**
     * Загружает все записи из `parsed_epub.json`, находит нужную по дате и возвращает ее цитату.
     * @param context Контекст приложения для доступа к `assets`.
     * @param date Дата, для которой нужно найти цитату.
     * @return Текст цитаты или сообщение об ошибке, если запись не найдена.
     */
    fun getQuoteForDate(context: Context, date: LocalDate): String {
        return try {
            val input = context.assets.open("parsed_epub.json")
            val reader = InputStreamReader(input)
            val type = object : TypeToken<List<DailyEntry>>() {}.type
            val entries: List<DailyEntry> = Gson().fromJson(reader, type)

            // Форматируем дату для поиска в JSON (например, "1 января")
            val day = date.dayOfMonth
            val monthIndex = date.monthValue - 1
            val monthName = DateFormatSymbols(Locale("ru")).months[monthIndex]
            val formattedDate = "$day $monthName"

            val entry = entries.firstOrNull { it.date.contains(formattedDate) }
            entry?.quote ?: "Цитата не найдена"
        } catch (e: Exception) {
            "Ошибка загрузки цитаты"
        }
    }
}