package com.ramzez.diary.ui.components

import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.ramzez.diary.model.ScriptureReference

/**
 * Composable-компонент для отображения текста с интерактивными ссылками.
 * Находит в тексте Markdown-подобные ссылки (например, `[текст](id)`) и делает их кликабельными.
 * @param text Текст для отображения, содержащий разметку ссылок.
 * @param references Карта, где ключ - это ID ссылки из разметки, а значение - объект [ScriptureReference].
 * @param onClick Лямбда-функция, вызываемая при клике на ссылку, передает ID ссылки.
 * @param fontWeight Начертание основного текста.
 * @param fontSize Размер основного текста.
 * @param fontStyle Стиль основного текста (например, курсив).
 * @param lineHeight Межстрочный интервал.
 */
@Composable
fun TextWithReferences(
    text: String,
    references: Map<String, ScriptureReference>,
    onClick: (String) -> Unit,
    fontWeight: FontWeight = FontWeight.Normal,
    fontSize: TextUnit = 16.sp,
    fontStyle: FontStyle? = null,
    lineHeight: TextUnit = TextUnit.Unspecified
) {
    val annotatedText = buildAnnotatedString {
        // Применяем общие стили ко всему параграфу
        withStyle(
            style = ParagraphStyle(lineHeight = lineHeight)
        ) {
            // Применяем стили к тексту, но не к ссылкам
            withStyle(
                style = SpanStyle(
                    fontStyle = fontStyle,
                    fontWeight = fontWeight,
                    fontSize = fontSize
                )
            ) {
                // Новое регулярное выражение: ищет всю конструкцию `[...](...)`
                val regex = Regex("(\\[([^\\]]+)]\\(([^)]+)\\))")
                var lastIndex = 0

                regex.findAll(text).forEach { match ->
                    // Добавляем обычный текст до найденной ссылки
                    append(text.substring(lastIndex, match.range.first))

                    val fullMatch = match.groups[1]?.value ?: "" // Полное совпадение, например `[Рим. 5:5](citation1)`
                    val label = match.groups[2]?.value ?: "" // Текст внутри скобок, например `Рим. 5:5`
                    val id = match.groups[3]?.value ?: ""    // ID, например `citation1`

                    // Добавляем аннотацию для кликабельной части
                    pushStringAnnotation(tag = "REF", annotation = id)
                    // Применяем специальный стиль только для ссылки
                    withStyle(
                        SpanStyle(
                            color = Color(0xFF4A6DA7),
                            textDecoration = TextDecoration.Underline,
                        )
                    ) {
                        // Отображаем только текст в скобках: `(Рим. 5:5)`
                        append("($label)")
                    }
                    pop()

                    // Сдвигаем индекс на длину полного совпадения
                    lastIndex = match.range.last + 1
                }

                // Добавляем оставшийся текст после последней ссылки
                if (lastIndex < text.length) {
                    append(text.substring(lastIndex))
                }
            }
        }
    }

    ClickableText(
        text = annotatedText,
        onClick = { offset ->
            // При клике ищем аннотацию в указанной позиции и вызываем колбэк
            annotatedText
                .getStringAnnotations("REF", offset, offset)
                .firstOrNull()
                ?.let { annotation -> onClick(annotation.item) }
        }
    )
}




//package com.ramzez.diary.ui.components
//
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.text.*
//import androidx.compose.ui.text.style.TextDecoration
//import androidx.compose.ui.Modifier
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.text.BasicText
//import androidx.compose.ui.graphics.Color
//import androidx.compose.foundation.text.ClickableText
//
//@Composable
//fun TextWithReferences(
//    text: String,
//    references: Map<String, com.ramzez.diary.model.ScriptureReference>,
//    onClick: (String) -> Unit
//) {
//    val annotatedText = buildAnnotatedString {
//        val regex = Regex("\\[([^\\]]+)]\\(([^)]+)\\)")
//        var lastIndex = 0
//
//        regex.findAll(text).forEach { match ->
//            append(text.substring(lastIndex, match.range.first))
//            val label = match.groups[1]?.value ?: ""
//            val id = match.groups[2]?.value ?: ""
//            pushStringAnnotation(tag = "REF", annotation = id)
//            withStyle(SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
//                append(label)
//            }
//            pop()
//            lastIndex = match.range.last + 1
//        }
//        if (lastIndex < text.length) {
//            append(text.substring(lastIndex))
//        }
//    }
//
//    ClickableText(
//        text = annotatedText,
//        onClick = { offset ->
//            annotatedText.getStringAnnotations(tag = "REF", start = offset, end = offset)
//                .firstOrNull()?.let { annotation ->
//                    onClick(annotation.item)
//                }
//        }
//    )
//}
