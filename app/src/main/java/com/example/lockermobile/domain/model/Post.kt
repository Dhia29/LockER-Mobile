package com.example.lockermobile.domain.model

data class Post(
    val id: String,
    val authorName: String,
    val authorAvatar: String,
    val authorRole: String,
    val content: String,
    val timestamp: String,
    val likesCount: Int,
    val commentsCount: Int,
    val isLiked: Boolean = false
)
