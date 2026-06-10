package com.example.lockermobile.presentation.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lockermobile.domain.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatDetailViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val chatId: String = checkNotNull(savedStateHandle["chatId"])
    
    private val _state = MutableStateFlow(ChatDetailState())
    val state = _state.asStateFlow()

    init {
        loadMessages()
    }

    private fun loadMessages() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            chatRepository.getMessages(chatId).collect { messages ->
                _state.update { it.copy(
                    isLoading = false,
                    messages = messages,
                    senderName = "Chat $chatId" // Mock name
                ) }
            }
        }
    }

    fun onMessageChange(text: String) {
        _state.update { it.copy(messageText = text) }
    }

    fun sendMessage() {
        if (_state.value.messageText.isBlank()) return
        
        viewModelScope.launch {
            chatRepository.sendMessage(chatId, _state.value.messageText).collect { result ->
                result.onSuccess {
                    _state.update { it.copy(messageText = "") }
                }
            }
        }
    }
}
