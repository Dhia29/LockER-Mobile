package com.example.lockermobile.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lockermobile.data.repository.SupabaseRepository
import com.example.lockermobile.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: SupabaseRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<Resource<Unit>?>(null)
    val authState: StateFlow<Resource<Unit>?> = _authState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = Resource.Loading
            try {
                repository.signIn(email, password)
                _authState.value = Resource.Success(Unit)
            } catch (e: Exception) {
                _authState.value = Resource.Error(e.message ?: "Login failed")
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = Resource.Loading
            try {
                repository.signUp(email, password)
                _authState.value = Resource.Success(Unit)
            } catch (e: Exception) {
                _authState.value = Resource.Error(e.message ?: "Registration failed")
            }
        }
    }
}
