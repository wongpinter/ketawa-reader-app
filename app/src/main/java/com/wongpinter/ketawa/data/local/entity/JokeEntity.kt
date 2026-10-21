package com.wongpinter.ketawa.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wongpinter.ketawa.domain.model.Post

@Entity(tableName = "jokes")
data class JokeEntity(
    @PrimaryKey
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
    val postUrl: String?,
    val isBookmarked: Boolean = false,
    val downloadedAt: Long = System.currentTimeMillis()
)

fun JokeEntity.toPost(): Post = Post(
    id = id,
    title = title,
    previousId = previousId,
    nextId = nextId,
    categoryId = categoryId,
    categoryName = categoryName,
    postDate = postDate,
    note = note,
    sender = sender,
    content = content,
    postUrl = postUrl
)

fun Post.toEntity(isBookmarked: Boolean = false): JokeEntity = JokeEntity(
    id = id,
    title = title,
    previousId = previousId,
    nextId = nextId,
    categoryId = categoryId,
    categoryName = categoryName,
    postDate = postDate,
    note = note,
    sender = sender,
    content = content,
    postUrl = postUrl,
    isBookmarked = isBookmarked
)
