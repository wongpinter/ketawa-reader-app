package com.wongpinter.ketawa.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.wongpinter.ketawa.domain.model.Home

data class HomeDto(
    @SerializedName("Data")
    val data: List<PostDto>,

    @SerializedName("fullURL")
    val url: String
)

fun HomeDto.toHome() =
    Home(
        url = url,
        data = data.map { it.toPost() }
    )