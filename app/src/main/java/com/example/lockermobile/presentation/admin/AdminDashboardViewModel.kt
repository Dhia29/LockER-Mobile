package com.example.lockermobile.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lockermobile.data.local.dao.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminDashboardViewModel @Inject constructor(
    private val userDao: UserDao,
    private val jobDao: JobDao,
    private val postDao: PostDao
) : ViewModel() {

    private val _state = MutableStateFlow(AdminDashboardState())
    val state = _state.asStateFlow()

    init {
        loadStats()
    }

    private fun loadStats() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            combine(
                userDao.getAllUsers(), // Assuming this exists or will be added
                jobDao.getAllJobs(),
                postDao.getAllPosts()
            ) { users, jobs, posts ->
                _state.update { it.copy(
                    isLoading = false,
                    totalUsers = users.size,
                    totalJobs = jobs.size,
                    totalPosts = posts.size,
                    totalReports = 5 // Mock report count
                ) }
            }.collect()
        }
    }
}
