package com.wongpinter.ketawa.domain.repository

import com.wongpinter.ketawa.domain.model.Categories
import com.wongpinter.ketawa.domain.model.CategoryPosts
import com.wongpinter.ketawa.domain.model.Home
import com.wongpinter.ketawa.domain.model.Post
import com.wongpinter.ketawa.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ApiRepository {
    suspend fun getHome(): Flow<Resource<Home>>
    suspend fun getPost(postId: String): Flow<Resource<Post>>
    suspend fun getCategories(): Flow<Resource<Categories>>
    suspend fun getCategoryPosts(categoryId: String): Flow<Resource<CategoryPosts>>
}