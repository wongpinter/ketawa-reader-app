package com.wongpinter.ketawa.domain.model

data class Categories(
    val categories: List<Category>
)

data class Category(
    val id: String,
    val subCategory: Any? = null,
    val content: String
)

data class CategoryPosts(
    val id: String,
    val category: String,
    val dataCount: String,
    val posts: List<PostTitle>,
    val page: Int,
    val pageCount: Int,
    val perPage: Int,
    val postUrl: String
)