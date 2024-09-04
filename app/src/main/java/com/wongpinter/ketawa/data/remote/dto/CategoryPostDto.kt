package com.wongpinter.ketawa.data.remote.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.wongpinter.ketawa.domain.model.CategoryPosts
import com.wongpinter.ketawa.domain.model.PostTitle

data class CategoryPostsDto(
    @JsonProperty("CatID") val categoryID: String,
    @JsonProperty("Category") val categoryName: String,
    @JsonProperty("DataCount") val dataCount: String,
    @JsonProperty("Page") val page: Int,
    @JsonProperty("PageCount") val pageCount: Int,
    @JsonProperty("PerPage") val perPage: Int,
    @JsonProperty("ListData") val posts: List<PostTitleDto>,
    @JsonProperty("fullURL") val postUrl: String
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
    @JsonProperty("DataID") val id: String,
    @JsonProperty("DataType") val type: String,
    @JsonProperty("Content") val content: String
)

fun PostTitleDto.toPostTitle() =
    PostTitle(
        postId = id,
        content = content,
        postType = type
    )