package com.ramzez.diary.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Всплывающее окно (AlertDialog) для отображения полного текста библейской ссылки.
 * @param reference Название ссылки (например, "Иоанна 3:16"), отображается в заголовке.
 * @param text Полный текст стиха, отображается в теле диалога.
 * @param onDismiss Лямбда-функция, вызываемая при закрытии окна.
 */
@Composable
fun ReferencePopup(reference: String, text: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Закрыть")
            }
        },
        title = { Text(reference) },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp)
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,

                )
            }
        }
    )
}

