package com.wongpinter.ketawa.data.remote

import com.wongpinter.ketawa.data.remote.dto.CategoriesDto
import com.wongpinter.ketawa.data.remote.dto.CategoryPostsDto
import com.wongpinter.ketawa.data.remote.dto.HomeDto
import com.wongpinter.ketawa.data.remote.dto.PostDto
import com.wongpinter.ketawa.data.remote.dto.toCategories
import com.wongpinter.ketawa.data.remote.dto.toCategoryPosts
import com.wongpinter.ketawa.data.remote.dto.toHome
import com.wongpinter.ketawa.data.remote.dto.toPost
import com.wongpinter.ketawa.domain.model.Categories
import com.wongpinter.ketawa.domain.model.CategoryPosts
import com.wongpinter.ketawa.domain.model.Home
import com.wongpinter.ketawa.domain.model.Post
import com.wongpinter.ketawa.domain.remote.ApiService
import com.wongpinter.ketawa.utils.Resource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.get
import io.ktor.util.InternalAPI
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ApiServiceImpl @Inject constructor(
    private val client: HttpClient
) : ApiService {
    override suspend fun getHome(): Flow<Resource<Home>> = safeApiCall {
        client.get("/json-index").body<HomeDto>().toHome()
    }

    override suspend fun getCategories(): Flow<Resource<Categories>> = safeApiCall {
        client.get("/json-cat-list").body<CategoriesDto>().toCategories()
    }

    override suspend fun getCategoryPosts(
        categoryId: String,
        page: Int
    ): Flow<Resource<CategoryPosts>> = safeApiCall {
        client.get("json-cat-${categoryId}-${page}").body<CategoryPostsDto>().toCategoryPosts()
    }

    override suspend fun getPost(postId: String): Flow<Resource<Post>> = safeApiCall {
        client.get("json-detail-${postId}").body<PostDto>().toPost()
    }
}

@OptIn(InternalAPI::class)
private suspend fun <T> safeApiCall(apiCall: suspend () -> T): Flow<Resource<T>> = flow {
    emit(Resource.Loading())
    try {
        val result = apiCall()
        emit(Resource.Success(result))
    } catch (e: IOException) {
        emit(Resource.Error("Network Error: ${e.localizedMessage}"))
    } catch (e: ClientRequestException) {
        emit(Resource.Error("HTTP Error: ${e.response.status} - ${e.response.content}"))
    } catch (e: Exception) {
        emit(Resource.Error("Unexpected Error: ${e.localizedMessage}"))
    }
}