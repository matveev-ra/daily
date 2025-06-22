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

/**
 * AppWidgetProvider для виджета с цитатой дня.
 * Отвечает за обновление и отображение виджета на главном экране.
 */
class QuoteWidget : AppWidgetProvider() {

    /**
     * Вызывается для обновления виджета через определенные интервалы
     * (задано в `quote_widget_info.xml`) и принудительно из `WidgetUpdateReceiver`.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    /**
     * Вызывается один раз при добавлении первого виджета на экран.
     * Используется для инициализации ежедневного планировщика обновления виджета.
     */
    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        WidgetUpdateReceiver.scheduleWidgetUpdate(context)
    }

    /**
     * Заполняет макет виджета актуальными данными.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.quote_widget)

        // Получаем цитату на сегодня и очищаем от Markdown для простого отображения.
        val quote = QuoteUtils.getQuoteForDate(context, LocalDate.now())
        val cleanQuote = removeMarkdownLinks(quote)
        views.setTextViewText(R.id.widget_quote_text, cleanQuote)

        // Устанавливаем обработчик клика на виджет, который открывает MainActivity.
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        views.setOnClickPendingIntent(R.id.widget_quote_text, pendingIntent)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    /**
     * Вспомогательная функция для очистки текста цитаты от Markdown-ссылок
     * и форматирования для лучшего отображения в виджете.
     */
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

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        // Можно добавить отмену планировщика, если нужно
    }
}