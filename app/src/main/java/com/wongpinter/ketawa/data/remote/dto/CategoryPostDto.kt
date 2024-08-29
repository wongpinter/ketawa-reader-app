package com.wongpinter.ketawa.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.wongpinter.ketawa.domain.model.CategoryPosts
import com.wongpinter.ketawa.domain.model.PostTitle

data class CategoryPostsDto(
    @SerializedName("CatID") val categoryID: String,
    @SerializedName("Category") val categoryName: String,
    @SerializedName("DataCount") val dataCount: String,
    @SerializedName("Page") val page: Int,
    @SerializedName("PageCount") val pageCount: Int,
    @SerializedName("PerPage") val perPage: Int,
    @SerializedName("ListData") val posts: List<PostTitleDto>,
    @SerializedName("fullURL") val postUrl: String
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

data class PostTitleDto(
    @SerializedName("DataID") val id: String,
    @SerializedName("DataType") val type: String,
    @SerializedName("Content") val content: String
)

fun PostTitleDto.toPostTitle() =
    PostTitle(
        postId = id,
        content = content,
        postType = type
    )