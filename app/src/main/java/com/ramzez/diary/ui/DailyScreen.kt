package com.ramzez.diary.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ramzez.diary.viewmodel.DailyViewModel
import com.ramzez.diary.ui.components.TextWithReferences
import com.ramzez.diary.ui.components.ReferencePopup
import androidx.compose.runtime.collectAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyScreen(
    onOpenSettings: () -> Unit,
    viewModel: DailyViewModel = viewModel()
) {
    val entries by viewModel.entries.collectAsState()
    val index by viewModel.currentIndex.collectAsState()
    val entry = entries.getOrNull(index)

    var selectedReference by remember { mutableStateOf<String?>(null) }

    entry?.let {
        val referenceMap = it.scriptureReferences.associateBy { ref -> ref.id }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Цитата дня") },
                    actions = {
                        IconButton(onClick = onOpenSettings) {
                            Icon(Icons.Default.Settings, contentDescription = "Настройки")
                        }
                    }
                )
            }
        ) { padding ->
            Column(modifier = Modifier.padding(padding).padding(16.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = viewModel::goToPreviousDay) { Text("<") }
                    Text(it.date, style = MaterialTheme.typography.titleLarge)
                    Button(onClick = viewModel::goToNextDay) { Text(">") }
                }

                Spacer(Modifier.height(16.dp))

                TextWithReferences(
                    text = it.quote,
                    references = referenceMap,
                    onClick = { id -> selectedReference = id },
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                Spacer(Modifier.height(16.dp))

                TextWithReferences(
                    text = it.commentary,
                    references = referenceMap,
                    onClick = { id -> selectedReference = id },
                    fontSize = 16.sp
                )
            }

            selectedReference?.let { refId ->
                referenceMap[refId]?.let { ref ->
                    ReferencePopup(reference = ref.reference, text = ref.text) {
                        selectedReference = null
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
