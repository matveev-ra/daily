package com.ramzez.diary
import java.util.Calendar

data class Quote(
    val text: String,
    val comment: String
)


fun getTodayQuote(): Quote {
    // Примерные данные — потом добавим настоящие
    val quotes = listOf(
        Quote("Будь собой", "Не пытайся быть как кто-то другой — ты уникален."),
        Quote("Каждый день — подарок", "Цени момент, в котором ты находишься."),
        Quote("Делай шаг", "Даже маленький шаг лучше бездействия.")
    )

    val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
    val index = dayOfYear % quotes.size

    return quotes[index]
}