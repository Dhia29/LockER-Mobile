package com.example.lockermobile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: String,
    val authorName: String,
    val authorAvatar: String,
    val authorRole: String,
    val content: String,
    val timestamp: String,
    val likesCount: Int,
    val commentsCount: Int,
    val isLiked: Boolean = false
)
