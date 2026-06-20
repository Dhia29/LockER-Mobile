package com.example.lockermobile.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lockermobile.core.data.SessionManager
import com.example.lockermobile.data.local.seeder.DatabaseSeeder
import com.example.lockermobile.domain.model.UserRole
import com.example.lockermobile.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val seeder: DatabaseSeeder
) : ViewModel() {

    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination = _startDestination.asStateFlow()

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            // Seed data on first launch
            try {
                seeder.seed()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            
            delay(1500L) // Minimum splash duration
            
            sessionManager.userRole.collect { userRole ->
                if (userRole != null) {
                    _startDestination.value = when (userRole) {
                        UserRole.ADMIN -> Screen.AdminDashboard.route
                        UserRole.JOB_SEEKER -> Screen.Home.route
                    }
                } else {
                    _startDestination.value = Screen.Login.route
                }
                _isReady.value = true
            }
        }
    }
}
