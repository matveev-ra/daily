package com.ramzez.diary.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ramzez.diary.ui.components.ReferencePopup
import com.ramzez.diary.ui.components.TextWithReferences
import com.ramzez.diary.viewmodel.DailyViewModel

/**
 * Главный экран приложения, отображающий ежедневную цитату и комментарий.
 * @param onOpenSettings Лямбда-функция для навигации на экран настроек.
 * @param viewModel ViewModel, предоставляющая данные о записях.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyScreen(
    onOpenSettings: () -> Unit,
    viewModel: DailyViewModel = viewModel()
) {
    val entries by viewModel.entries.collectAsState()
    val index by viewModel.currentIndex.collectAsState()
    val entry = entries.getOrNull(index)

    // Состояние для отслеживания выбранной ссылки для отображения попапа.
    var selectedReference by remember { mutableStateOf<String?>(null) }

    entry?.let {
        // Преобразуем список ссылок в карту для быстрого доступа по ID.
        val referenceMap = it.scriptureReferences.associateBy { ref -> ref.id }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "Стих на день",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF666666),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = it.date,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF444444),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = onOpenSettings) {
                            Icon(Icons.Default.Settings, contentDescription = "Настройки")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 16.dp)
                    .fillMaxHeight()
            ) {
                // Основной контент сделан прокручиваемым, занимая все доступное пространство.
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(top = 16.dp)
                ) {
                    TextWithReferences(
                        text = it.quote,
                        references = referenceMap,
                        onClick = { id -> selectedReference = id },
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        fontStyle = FontStyle.Italic
                    )

                    Spacer(Modifier.height(16.dp))

                    TextWithReferences(
                        text = it.commentary,
                        references = referenceMap,
                        onClick = { id -> selectedReference = id },
                        fontSize = 16.sp,
                        lineHeight = 22.sp
                    )
                }

                // Кнопки навигации закреплены внизу экрана.
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Button(onClick = viewModel::goToPreviousDay) { Text("<") }
                    Button(onClick = viewModel::goToNextDay) { Text(">") }
                }
            }

            // Показываем попап, если есть выбранная ссылка.
            selectedReference?.let { refId ->
                referenceMap[refId]?.let { ref ->
                    ReferencePopup(reference = ref.reference, text = ref.text) {
                        selectedReference = null // Сбрасываем состояние для закрытия попапа.
                    }
                }
            }
        }
    }
}






//package com.ramzez.diary.ui
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.ramzez.diary.viewmodel.DailyViewModel
//import com.ramzez.diary.ui.components.TextWithReferences
//import com.ramzez.diary.ui.components.ReferencePopup
//import androidx.compose.ui.unit.dp
//
//@Composable
//fun DailyScreen(viewModel: DailyViewModel = viewModel()) {
//    val entries by viewModel.entries.collectAsState()
//    val index by viewModel.currentIndex.collectAsState()
//    val entry = entries.getOrNull(index)
//
//    var selectedReference by remember { mutableStateOf<String?>(null) }
//
//    entry?.let {
//        val referenceMap = it.scriptureReferences.associateBy { ref -> ref.id }
//
//        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
//                Button(onClick = viewModel::goToPreviousDay) { Text("<") }
//                Text(it.date, style = MaterialTheme.typography.titleLarge)
//                Button(onClick = viewModel::goToNextDay) { Text(">") }
//            }
//
//            Spacer(Modifier.height(16.dp))
//
//            TextWithReferences(it.quote, referenceMap) { id -> selectedReference = id }
//
//            Spacer(Modifier.height(16.dp))
//
//            TextWithReferences(it.commentary, referenceMap) { id -> selectedReference = id }
//        }
//
//        selectedReference?.let { refId ->
//            referenceMap[refId]?.let { ref ->
//                ReferencePopup(reference = ref.reference, text = ref.text) {
//                    selectedReference = null
//                }
//            }
//        }
//    }
//}
