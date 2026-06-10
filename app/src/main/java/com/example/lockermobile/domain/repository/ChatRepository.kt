package com.example.lockermobile.domain.repository

import com.example.lockermobile.domain.model.ChatConversation
import com.example.lockermobile.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getConversations(): Flow<List<ChatConversation>>
    fun getMessages(chatId: String): Flow<List<ChatMessage>>
    fun sendMessage(chatId: String, text: String): Flow<Result<Unit>>
}
