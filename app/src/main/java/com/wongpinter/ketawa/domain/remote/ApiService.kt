package com.wongpinter.ketawa.domain.remote

import com.wongpinter.ketawa.domain.model.Categories
import com.wongpinter.ketawa.domain.model.CategoryPosts
import com.wongpinter.ketawa.domain.model.Home
import com.wongpinter.ketawa.domain.model.Post
import com.wongpinter.ketawa.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ApiService {
    suspend fun getHome(): Flow<Resource<Home>>
    suspend fun getCategories(): Flow<Resource<Categories>>
    suspend fun getCategoryPosts(categoryId: String, page: Int): Flow<Resource<CategoryPosts>>
    suspend fun getPost(postId: String): Flow<Resource<Post>>
}