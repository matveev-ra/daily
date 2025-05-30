package com.ramzez.diary.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ramzez.diary.model.DailyEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.InputStreamReader
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

class DailyViewModel(application: Application) : AndroidViewModel(application) {

    private val _entries = MutableStateFlow<List<DailyEntry>>(emptyList())
    val entries: StateFlow<List<DailyEntry>> = _entries

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    init {
        loadEntries()
    }

    private fun loadEntries() {
        viewModelScope.launch {
            val input = getApplication<Application>().assets.open("parsed_epub.json")
            val reader = InputStreamReader(input)
            val type = object : TypeToken<List<DailyEntry>>() {}.type
            val allEntries: List<DailyEntry> = Gson().fromJson(reader, type)
            _entries.value = allEntries

            // Получаем текущую дату через Calendar
            val calendar = java.util.Calendar.getInstance()
            val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)
            val monthIndex = calendar.get(java.util.Calendar.MONTH) // 0-based
            val monthName = java.text.DateFormatSymbols(java.util.Locale("ru")).months[monthIndex]

            val todayFormatted = "$day $monthName"

            // Поиск записи по текущей дате
            val index = allEntries.indexOfFirst { it.date.contains(todayFormatted) }
            _currentIndex.value = if (index >= 0) index else 0
        }
    }

    fun goToPreviousDay() {
        _currentIndex.value = (_currentIndex.value - 1).coerceAtLeast(0)
    }

    fun goToNextDay() {
        _currentIndex.value = (_currentIndex.value + 1).coerceAtMost(_entries.value.lastIndex)
    }
}