package com.ramzez.diary.model

data class DailyEntry(
    val month: String,
    val date: String,
    val quote: String,
    val commentary: String,
    val scriptureReferences: List<ScriptureReference>
)

data class ScriptureReference(
    val id: String,
    val reference: String,
    val text: String
)