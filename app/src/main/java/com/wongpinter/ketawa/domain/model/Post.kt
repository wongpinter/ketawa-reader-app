package com.wongpinter.ketawa.domain.model

data class Post(
    val id: String,
    val title: String,
    val previousId: String?,
    val nextId: String?,
    val categoryId: String,
    val categoryName: String,
    val postDate: String,
    val note: String?,
    val sender: String,
    val content: String,
    val postUrl: String?
)

data class PostTitle(
    val content: String,
    val postId: String,
    val postType: String
)
