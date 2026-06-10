package com.example.lockermobile.domain.model

data class ChatMessage(
    val id: String,
    val content: String,
    val timestamp: String,
    val isFromMe: Boolean
)
