package com.wongpinter.ketawa.domain.model

import java.util.Date

data class Post(
    val id: String,
    val title: String,
    val previousId: String?,
    val nextId: String?,
    val categoryId: String,
    val categoryName: String,
    val postDate: Date,
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