package com.example.lockermobile.presentation.employer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lockermobile.data.local.dao.JobDao
import com.example.lockermobile.data.local.entity.JobEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PostJobViewModel @Inject constructor(
    private val jobDao: JobDao
) : ViewModel() {

    private val _state = MutableStateFlow(PostJobState())
    val state = _state.asStateFlow()

    fun onTitleChange(value: String) = _state.update { it.copy(title = value) }
    fun onCategoryChange(value: String) = _state.update { it.copy(category = value) }
    fun onTypeChange(value: String) = _state.update { it.copy(type = value) }
    fun onLocationChange(value: String) = _state.update { it.copy(location = value) }
    fun onSalaryChange(value: String) = _state.update { it.copy(salary = value) }
    fun onDescriptionChange(value: String) = _state.update { it.copy(description = value) }

    fun postJob() {
        val current = _state.value
        if (current.title.isBlank() || current.category.isBlank() || current.description.isBlank()) {
            _state.update { it.copy(error = "Please fill in all required fields") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val newJob = JobEntity(
                    id = UUID.randomUUID().toString(),
                    companyId = "1", // Mock company ID
                    title = current.title,
                    companyName = "My Company", // Mock company name
                    location = current.location,
                    salary = current.salary,
                    type = current.type,
                    logoUrl = "https://via.placeholder.com/150",
                    description = current.description,
                    postedTime = "Just now",
                    category = current.category
                )
                jobDao.insertJob(newJob)
                _state.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
