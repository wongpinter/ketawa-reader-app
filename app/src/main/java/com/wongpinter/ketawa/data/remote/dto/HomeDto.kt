package com.wongpinter.ketawa.data.remote.dto

import com.wongpinter.ketawa.domain.model.Home
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeDto(
    @SerialName("Data")
    val data: List<PostDto> = emptyList(),

    @SerialName("fullURL")
    val url: String = ""
)

fun HomeDto.toHome() =
    Home(
        url = url,
        data = data.map { it.toPost() }
    )
