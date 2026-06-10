package com.example.lockermobile.data.repository

import com.example.lockermobile.data.local.dao.PostDao
import com.example.lockermobile.data.local.entity.PostEntity
import com.example.lockermobile.domain.model.Post
import com.example.lockermobile.domain.repository.CommunityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CommunityRepositoryImpl @Inject constructor(
    private val postDao: PostDao
) : CommunityRepository {

    override fun getPosts(): Flow<List<Post>> {
        return postDao.getAllPosts().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun likePost(postId: String): Flow<Unit> = flow {
        val post = postDao.getPostById(postId)
        if (post != null) {
            val updatedPost = post.copy(
                isLiked = !post.isLiked,
                likesCount = if (post.isLiked) post.likesCount - 1 else post.likesCount + 1
            )
            postDao.updatePost(updatedPost)
        }
        emit(Unit)
    }

    private fun PostEntity.toDomain(): Post {
        return Post(
            id = id,
            authorName = authorName,
            authorAvatar = authorAvatar,
            authorRole = authorRole,
            content = content,
            timestamp = timestamp,
            likesCount = likesCount,
            commentsCount = commentsCount,
            isLiked = isLiked
        )
    }
}
