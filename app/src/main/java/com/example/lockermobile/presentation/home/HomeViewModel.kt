package com.example.lockermobile.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lockermobile.domain.repository.JobRepository
import com.example.lockermobile.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val jobRepository: JobRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        getUser()
        getJobs()
    }

    private fun getUser() {
        viewModelScope.launch {
            userRepository.getCurrentUser().collect { user ->
                _state.update { it.copy(user = user) }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
        getJobs()
    }

    fun onCategoryChange(category: String) {
        _state.update { it.copy(selectedCategory = category) }
        getJobs()
    }

    private fun getJobs() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            jobRepository.getJobs(
                query = _state.value.searchQuery,
                category = _state.value.selectedCategory
            ).collect { jobs ->
                _state.update { it.copy(
                    jobs = jobs,
                    isLoading = false
                ) }
            }
        }
    }
}
