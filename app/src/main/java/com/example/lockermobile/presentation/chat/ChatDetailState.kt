package com.example.lockermobile.presentation.chat

import com.example.lockermobile.domain.model.ChatMessage

data class ChatDetailState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val senderName: String = "",
    val messageText: String = "",
    val error: String? = null
)
