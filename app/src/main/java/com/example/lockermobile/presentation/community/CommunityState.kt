package com.example.lockermobile.presentation.community

import com.example.lockermobile.domain.model.Post

data class CommunityState(
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
