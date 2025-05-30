package com.ramzez.diary.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ramzez.diary.model.DailyEntry
import java.io.InputStreamReader
import java.text.DateFormatSymbols
import java.util.*

object QuoteUtils {
    fun getTodayQuote(context: Context): String {
        return try {
            val input = context.assets.open("parsed_epub.json")
            val reader = InputStreamReader(input)
            val type = object : TypeToken<List<DailyEntry>>() {}.type
            val entries: List<DailyEntry> = Gson().fromJson(reader, type)

            val calendar = Calendar.getInstance()
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val monthIndex = calendar.get(Calendar.MONTH)
            val monthName = DateFormatSymbols(Locale("ru")).months[monthIndex]
            val todayFormatted = "$day $monthName"

            val entry = entries.firstOrNull { it.date.contains(todayFormatted) }
            entry?.quote ?: "Цитата не найдена"
        } catch (e: Exception) {
            "Ошибка загрузки цитаты"
        }
    }
}
