package com.ramzez.diary

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.ramzez.diary.MainActivity
import com.ramzez.diary.R
import com.ramzez.diary.utils.QuoteUtils

class QuoteWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.quote_widget)

        val quote = getTodayQuote(context) // Получаем цитату
        val cleanQuote = removeMarkdownLinks(quote) // Очищаем от markdown-ссылок
        views.setTextViewText(R.id.widget_quote_text, cleanQuote)

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        views.setOnClickPendingIntent(R.id.widget_quote_text, pendingIntent)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun removeMarkdownLinks(text: String): String {
        // Удаляем внешние скобки вокруг markdown-ссылки
        val cleanedText = text.replace(Regex("\\(\\[([^\\]]+)]\\([^\\)]+\\)\\)"), "[$1]($1)")

        // Преобразуем Markdown-ссылку в текст с новой строки
        val regex = Regex("\\[([^\\]]+)]\\([^\\)]+\\)")
        return regex.replace(cleanedText) { matchResult ->
            "\n(${matchResult.groupValues[1]})"
        }
    }

    private fun getTodayQuote(context: Context): String {
        // твой способ загрузки цитаты из JSON или другого источника
        return "С ним победят призванные, избранные и верны([Отк. 17:14](citation1))"
    }
}