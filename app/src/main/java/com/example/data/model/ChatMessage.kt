package com.example.data.model

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val sources: List<String> = emptyList(),
    val suggestedPrompts: List<String> = emptyList()
)
