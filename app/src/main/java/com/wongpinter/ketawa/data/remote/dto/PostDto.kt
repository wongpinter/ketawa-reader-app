package com.wongpinter.ketawa.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.wongpinter.ketawa.domain.model.Post
import java.util.Date

data class PostDto(
    @SerializedName("DataID") val id: String,
    @SerializedName("DataTitle") val title: String,
    @SerializedName("PrevID") val previousId: String?,
    @SerializedName("NextID") val nextId: String?,
    @SerializedName("CategId") val categoryId: String,
    @SerializedName("CategName") val categoryName: String,
    @SerializedName("DataDate") val date: Date,
    @SerializedName("DataNotes") val note: String?,
    @SerializedName("DataSender") val sender: String,
    @SerializedName("Content") val content: String,
    @SerializedName("fullURL") val postUrl: String?
)

fun PostDto.toPost() =
    Post(
        id = id,
        title = title,
        previousId = previousId,
        nextId = nextId,
        categoryId = categoryId,
        categoryName = categoryName,
        postDate = date,
        note = note,
        sender = sender,
        content = content,
        postUrl = postUrl,
    )