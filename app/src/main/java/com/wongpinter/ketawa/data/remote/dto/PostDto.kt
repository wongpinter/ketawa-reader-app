package com.wongpinter.ketawa.data.remote.dto

import com.wongpinter.ketawa.domain.model.Post
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostDto(
    @SerialName("DataID") val id: String = "",
    @SerialName("DataTitle") val title: String = "",
    @SerialName("PrevID") val previousId: String? = null,
    @SerialName("NextID") val nextId: String? = null,
    @SerialName("CategId") val categoryId: String = "",
    @SerialName("CategName") val categoryName: String = "",
    @SerialName("DataDate") val date: String? = null,
    @SerialName("DataNotes") val note: String? = null,
    @SerialName("DataSender") val sender: String = "",
    @SerialName("Content") val content: String = "",
    @SerialName("fullURL") val postUrl: String? = null
)

fun PostDto.toPost() =
    Post(
        id = id,
        title = sanitizeHtml(title),
        previousId = previousId,
        nextId = nextId,
        categoryId = categoryId,
        categoryName = categoryName,
        postDate = date ?: "",
        note = note,
        sender = sender,
        content = sanitizeContent(content),
        postUrl = postUrl,
    )

private fun sanitizeHtml(text: String): String {
    return text
        .replace("&quot;", "\"")
        .replace("&amp;", "&")
        .replace("&lt;", "<")
        .replace("&gt;", ">")
        .replace("&#039;", "'")
        .replace("&eacute;", "é")
        .trim()
}

private fun sanitizeContent(text: String): String {
    return sanitizeHtml(text)
        .replace(Regex("(?i)<br\\s*/?>"), "\n")
        .replace(Regex("(?i)<p>"), "")
        .replace(Regex("(?i)</p>"), "\n\n")
        .replace("\r\n", "\n")
        .replace("\r", "\n")
        .replace(Regex("\n{3,}"), "\n\n")
        .trim()
}
