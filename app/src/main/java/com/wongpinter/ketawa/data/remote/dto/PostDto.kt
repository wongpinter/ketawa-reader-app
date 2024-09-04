package com.wongpinter.ketawa.data.remote.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.wongpinter.ketawa.domain.model.Post
import java.util.Date

data class PostDto(
    @JsonProperty("DataID") val id: String,
    @JsonProperty("DataTitle") val title: String,
    @JsonProperty("PrevID") val previousId: String?,
    @JsonProperty("NextID") val nextId: String?,
    @JsonProperty("CategId") val categoryId: String,
    @JsonProperty("CategName") val categoryName: String,
    @JsonProperty("DataDate") val date: Date,
    @JsonProperty("DataNotes") val note: String?,
    @JsonProperty("DataSender") val sender: String,
    @JsonProperty("Content") val content: String,
    @JsonProperty("fullURL") val postUrl: String?
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