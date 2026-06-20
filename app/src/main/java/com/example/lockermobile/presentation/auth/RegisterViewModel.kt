package com.example.lockermobile.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lockermobile.domain.model.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.example.lockermobile.domain.repository.AuthRepository

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    fun onFullNameChange(name: String) {
        _state.update { it.copy(fullName = name) }
    }

    fun onEmailChange(email: String) {
        _state.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String) {
        _state.update { it.copy(password = password) }
    }

    fun onConfirmPasswordChange(password: String) {
        _state.update { it.copy(confirmPassword = password) }
    }

    fun onRoleChange(role: UserRole) {
        _state.update { it.copy(role = role) }
    }

    fun register() {
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState.password != currentState.confirmPassword) {
                _state.update { it.copy(error = "Passwords do not match") }
                return@launch
            }
            if (currentState.fullName.isBlank() || currentState.email.isBlank()) {
                _state.update { it.copy(error = "Please fill in all fields") }
                return@launch
            }

            _state.update { it.copy(isLoading = true, error = null) }
            
            try {
                authRepository.register(
                    currentState.fullName,
                    currentState.email,
                    currentState.password,
                    currentState.role
                ).collect { result ->
                    result.onSuccess {
                        _state.update { it.copy(isLoading = false, isRegisterSuccess = true) }
                    }.onFailure { exception ->
                        _state.update { it.copy(isLoading = false, error = exception.message ?: "Registration failed") }
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message ?: "An unexpected error occurred") }
            }
        }
    }
}
