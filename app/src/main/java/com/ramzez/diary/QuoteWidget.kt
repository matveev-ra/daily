package com.ramzez.diary

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import com.ramzez.diary.MainActivity
import com.ramzez.diary.R
import com.ramzez.diary.utils.QuoteUtils
import java.time.LocalDate
import java.time.LocalDate.now

class QuoteWidget : AppWidgetProvider() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.quote_widget)

        val quote = QuoteUtils.getQuoteForDate(context, LocalDate.now())
        val cleanQuote = removeMarkdownLinks(quote)
        views.setTextViewText(R.id.widget_quote_text, cleanQuote)

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        views.setOnClickPendingIntent(R.id.widget_quote_text, pendingIntent)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
    private fun removeMarkdownLinks(text: String): String {
        // Шаг 1: Удаляем Markdown-ссылки, оставляя только текст
        val regex = Regex("\\[([^\\]]+)]\\([^\\)]+\\)")
        val step1 = regex.replace(text) { matchResult ->
            matchResult.groupValues[1]
        }

        // Шаг 2: Удаляем лишние закрывающие скобки в конце строки (если они были от Markdown)
        val step2 = step1.replace(Regex("\\)+$"), ")")

        // Шаг 3: Добавляем перенос строки перед первой открывающей скобкой " ("
        val step3 = step2.replaceFirst(Regex(" ?\\("), "\n(")

        return step3
    }



    private fun removeMarkdownLinks__(text: String): String {
        // Шаг 1: Удаляем Markdown-ссылки, сохраняя только текст внутри квадратных скобок
        val regex = Regex("\\[([^\\]]+)]\\([^\\)]+\\)")
        val step1 = regex.replace(text) { matchResult ->
            matchResult.groupValues[1]
        }

        // Шаг 2: Удаляем лишние закрывающие скобки в конце строки
        return step1.replace(Regex("\n\\)+$"), ")")
    }
}