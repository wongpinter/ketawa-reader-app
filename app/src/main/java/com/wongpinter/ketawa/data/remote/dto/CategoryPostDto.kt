package com.wongpinter.ketawa.data.remote.dto

import com.wongpinter.ketawa.domain.model.CategoryPosts
import com.wongpinter.ketawa.domain.model.PostTitle
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryPostsDto(
    @SerialName("CatID") val categoryID: String = "",
    @SerialName("Category") val categoryName: String = "",
    @SerialName("DataCount") val dataCount: String = "0",
    @SerialName("Page") val page: Int = 1,
    @SerialName("PageCount") val pageCount: Int = 1,
    @SerialName("PerPage") val perPage: Int = 20,
    @SerialName("ListData") val posts: List<PostTitleDto> = emptyList(),
    @SerialName("fullURL") val postUrl: String = ""
)

fun CategoryPostsDto.toCategoryPosts() =
    CategoryPosts(
        id = categoryID,
        category = categoryName,
        dataCount = dataCount,
        page = page,
        pageCount = pageCount,
        perPage = perPage,
        posts = posts.map { it.toPostTitle() },
        postUrl = postUrl,
    )

@Serializable
data class PostTitleDto(
    @SerialName("DataID") val id: String = "",
    @SerialName("DataType") val type: String = "",
    @SerialName("Content") val content: String = ""
)

fun PostTitleDto.toPostTitle() =
    PostTitle(
        postId = id,
        content = content.trim(),
        postType = type
    )
