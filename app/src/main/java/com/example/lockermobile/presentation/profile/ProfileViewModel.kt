package com.example.lockermobile.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lockermobile.domain.model.Experience
import com.example.lockermobile.domain.model.User
import com.example.lockermobile.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        getUserProfile()
    }

    private fun getUserProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            repository.getCurrentUser().collect { user ->
                _state.update { it.copy(
                    user = user,
                    isLoading = false
                ) }
            }
        }
    }

    fun onBioUpdate(newBio: String) {
        val currentUser = _state.value.user ?: return
        viewModelScope.launch {
            val updatedUser = currentUser.copy(bio = newBio)
            repository.updateProfile(updatedUser)
        }
    }

    fun onSkillAdd(skill: String) {
        val currentUser = _state.value.user ?: return
        if (skill.isBlank() || currentUser.skills.contains(skill)) return
        
        viewModelScope.launch {
            val updatedSkills = currentUser.skills + skill
            repository.updateProfile(currentUser.copy(skills = updatedSkills))
        }
    }

    fun onSkillRemove(skill: String) {
        val currentUser = _state.value.user ?: return
        viewModelScope.launch {
            val updatedSkills = currentUser.skills - skill
            repository.updateProfile(currentUser.copy(skills = updatedSkills))
        }
    }

    fun onExperienceAdd(experience: Experience) {
        val currentUser = _state.value.user ?: return
        viewModelScope.launch {
            val updatedExperience = currentUser.experience + experience
            repository.updateProfile(currentUser.copy(experience = updatedExperience))
        }
    }

    fun onExperienceDelete(experience: Experience) {
        val currentUser = _state.value.user ?: return
        viewModelScope.launch {
            val updatedExperience = currentUser.experience - experience
            repository.updateProfile(currentUser.copy(experience = updatedExperience))
        }
    }

    fun onProfilePictureSelect(imageBytes: ByteArray) {
        val currentUser = _state.value.user ?: return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = repository.uploadProfilePicture(currentUser.email, imageBytes)
            result.onSuccess { url ->
                repository.updateProfile(currentUser.copy(profilePicture = url))
            }.onFailure {
                // Handle error
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    fun logout(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.logout().collect {
                onLogoutSuccess()
            }
        }
    }
}
