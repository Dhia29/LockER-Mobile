package com.example.lockermobile.data.repository

import com.example.lockermobile.domain.model.ChatConversation
import com.example.lockermobile.domain.model.ChatMessage
import com.example.lockermobile.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor() : ChatRepository {
    override fun getConversations(): Flow<List<ChatConversation>> = flow {
        emit(listOf(
            ChatConversation("1", "John Doe", "https://i.pravatar.cc/150?u=john", "Hey, are you available for a quick call?", "10:30 AM", 2),
            ChatConversation("2", "Jane Smith", "https://i.pravatar.cc/150?u=jane", "The project looks great!", "Yesterday", 0)
        ))
    }

    override fun getMessages(chatId: String): Flow<List<ChatMessage>> = flow {
        emit(listOf(
            ChatMessage("1", "Hello! I saw your application for the Android Developer role.", "10:00 AM", false),
            ChatMessage("2", "Thank you for reaching out! Yes, I am very interested.", "10:05 AM", true),
            ChatMessage("3", "Great. Can we schedule a technical interview for tomorrow?", "10:10 AM", false)
        ))
    }

    override fun sendMessage(chatId: String, text: String): Flow<Result<Unit>> = flow {
        emit(Result.success(Unit))
    }
}
