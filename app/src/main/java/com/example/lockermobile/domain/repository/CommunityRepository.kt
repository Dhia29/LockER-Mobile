package com.example.lockermobile.domain.repository

import com.example.lockermobile.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface CommunityRepository {
    fun getPosts(): Flow<List<Post>>
    fun likePost(postId: String): Flow<Unit>
}
