package com.wongpinter.ketawa.data.repository

import com.wongpinter.ketawa.data.local.dao.JokeDao
import com.wongpinter.ketawa.data.local.entity.toEntity
import com.wongpinter.ketawa.data.local.entity.toPost
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
import com.wongpinter.ketawa.domain.repository.JokeRepository
import com.wongpinter.ketawa.utils.Resource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JokeRepositoryImpl @Inject constructor(
    private val client: HttpClient,
    private val jokeDao: JokeDao
) : JokeRepository {

    override fun getHome(): Flow<Resource<Home>> = flow {
        emit(Resource.Loading())
        try {
            val remoteHome = client.get("https://api.ketawa.com/json-index").body<HomeDto>().toHome()
            // Auto-cache latest jokes into Room
            jokeDao.insertJokes(remoteHome.data.map { it.toEntity() })
            emit(Resource.Success(remoteHome))
        } catch (e: Exception) {
            // Offline fallback: load from Room
            val cachedEntities = jokeDao.getAllJokes().firstOrNull() ?: emptyList()
            if (cachedEntities.isNotEmpty()) {
                val cachedHome = Home(
                    url = "https://www.ketawa.com/",
                    data = cachedEntities.map { it.toPost() }
                )
                emit(Resource.Success(cachedHome))
            } else {
                emit(Resource.Error("Mode offline: Tidak ada cerita tersimpan"))
            }
        }
    }

    override fun getPost(postId: String): Flow<Resource<Post>> = flow {
        emit(Resource.Loading())
        // Emit from Room DB first if present
        val cached = jokeDao.getJokeByIdOnce(postId)
        if (cached != null) {
            emit(Resource.Success(cached.toPost()))
        }

        try {
            val remotePost = client.get("/json-detail-$postId").body<PostDto>().toPost()
            jokeDao.insertJoke(remotePost.toEntity())
            emit(Resource.Success(remotePost))
        } catch (e: Exception) {
            if (cached == null) {
                emit(Resource.Error("Gagal memuat: cerita belum diunduh"))
            }
        }
    }

    override fun getCategories(): Flow<Resource<Categories>> = flow {
        emit(Resource.Loading())
        try {
            val categories = client.get("/json-cat-list").body<CategoriesDto>().toCategories()
            emit(Resource.Success(categories))
        } catch (e: Exception) {
            emit(Resource.Error("Gagal memuat kategori: ${e.message}"))
        }
    }

    override fun getCategoryPosts(categoryId: String, page: Int): Flow<Resource<CategoryPosts>> = flow {
        emit(Resource.Loading())
        try {
            val result = client.get("/json-cat-$categoryId-$page").body<CategoryPostsDto>().toCategoryPosts()
            emit(Resource.Success(result))
        } catch (e: Exception) {
            // Fallback to offline cached jokes under this category
            val cachedInCat = jokeDao.getJokesByCategory(categoryId).firstOrNull() ?: emptyList()
            if (cachedInCat.isNotEmpty()) {
                emit(
                    Resource.Success(
                        CategoryPosts(
                            id = categoryId,
                            category = cachedInCat.first().categoryName,
                            dataCount = cachedInCat.size.toString(),
                            page = 1,
                            pageCount = 1,
                            perPage = cachedInCat.size,
                            posts = cachedInCat.map {
                                com.wongpinter.ketawa.domain.model.PostTitle(
                                    content = it.title,
                                    postId = it.id,
                                    postType = "DETAIL"
                                )
                            },
                            postUrl = ""
                        )
                    )
                )
            } else {
                emit(Resource.Error("Mode offline: Kategori belum diunduh"))
            }
        }
    }

    override fun getOfflineJokes(): Flow<List<Post>> {
        return jokeDao.getAllJokes().map { list -> list.map { it.toPost() } }
    }

    override fun getOfflineJokeCount(): Flow<Int> {
        return jokeDao.getJokeCount()
    }

    override suspend fun downloadCategoryJokes(categoryId: String, maxPages: Int): Flow<Resource<Int>> = flow {
        emit(Resource.Loading())
        var downloadedCount = 0
        try {
            for (p in 1..maxPages) {
                val catPosts = client.get("/json-cat-$categoryId-$p").body<CategoryPostsDto>().toCategoryPosts()
                val detailItems = catPosts.posts.filter { it.postType == "DETAIL" }

                for (item in detailItems) {
                    try {
                        val detail = client.get("/json-detail-${item.postId}").body<PostDto>().toPost()
                        jokeDao.insertJoke(detail.toEntity())
                        downloadedCount++
                    } catch (_: Exception) {
                    }
                }
                if (p >= catPosts.pageCount) break
            }
            emit(Resource.Success(downloadedCount))
        } catch (e: Exception) {
            if (downloadedCount > 0) {
                emit(Resource.Success(downloadedCount))
            } else {
                emit(Resource.Error("Gagal mengunduh: ${e.message}"))
            }
        }
    }
}
