package com.example.lockermobile.presentation.chat

import com.example.lockermobile.domain.model.ChatConversation

data class ChatState(
    val conversations: List<ChatConversation> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
