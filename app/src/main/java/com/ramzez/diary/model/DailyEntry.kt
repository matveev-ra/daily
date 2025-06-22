package com.ramzez.diary.model

/**
 * Модель данных для одной ежедневной записи.
 * Содержит текст цитаты, комментарий и связанные с ними места Писания.
 */
data class DailyEntry(
    val month: String,
    val date: String,
    val quote: String,
    val commentary: String,
    val scriptureReferences: List<ScriptureReference>
)

/**
 * Модель данных для одного места Писания (библейской ссылки).
 * @param id Уникальный идентификатор, используемый в тексте для связи (например, "ref1").
 * @param reference Краткое название ссылки (например, "Бытие 1:1").
 * @param text Полный текст стиха или отрывка.
 */
data class ScriptureReference(
    val id: String,
    val reference: String,
    val text: String
)