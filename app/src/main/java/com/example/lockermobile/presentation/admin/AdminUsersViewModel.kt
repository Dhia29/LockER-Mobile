package com.example.lockermobile.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lockermobile.data.local.dao.UserDao
import com.example.lockermobile.data.local.entity.UserEntity
import com.example.lockermobile.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminUsersViewModel @Inject constructor(
    private val userDao: UserDao
) : ViewModel() {

    private val _state = MutableStateFlow(AdminUsersState())
    val state = _state.asStateFlow()

    init {
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            userDao.getAllUsers().collect { entities ->
                _state.update { it.copy(
                    isLoading = false,
                    users = entities.map { it.toDomain() }
                ) }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    private fun UserEntity.toDomain(): User {
        return User(
            id = email,
            name = name,
            email = email,
            profilePicture = profilePicture,
            role = roleTitle,
            roleEnum = role,
            location = location,
            bio = bio
        )
    }
}
