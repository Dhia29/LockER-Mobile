package com.example.lockermobile.domain.model

data class ChatConversation(
    val id: String,
    val senderName: String,
    val senderAvatar: String,
    val lastMessage: String,
    val timestamp: String,
    val unreadCount: Int
)
