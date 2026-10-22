package com.wongpinter.ketawa.domain.repository

import com.wongpinter.ketawa.domain.model.Categories
import com.wongpinter.ketawa.domain.model.CategoryPosts
import com.wongpinter.ketawa.domain.model.Home
import com.wongpinter.ketawa.domain.model.Post
import com.wongpinter.ketawa.utils.Resource
import kotlinx.coroutines.flow.Flow

interface JokeRepository {
    fun getHome(): Flow<Resource<Home>>
    fun getPost(postId: String): Flow<Resource<Post>>
    fun getCategories(): Flow<Resource<Categories>>
    fun getCategoryPosts(categoryId: String, page: Int): Flow<Resource<CategoryPosts>>
    fun getOfflineJokes(): Flow<List<Post>>
    fun getOfflineJokeCount(): Flow<Int>
    suspend fun downloadCategoryJokes(categoryId: String, maxPages: Int = 3): Flow<Resource<Int>>
    suspend fun prefetchJoke(postId: String): Post?
}
