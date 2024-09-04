package com.wongpinter.ketawa.data.remote.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.wongpinter.ketawa.domain.model.Home

data class HomeDto(
    @JsonProperty(value = "Data")
    val data: List<PostDto>,

    @JsonProperty(value = "fullURL")
    val url: String
)

fun HomeDto.toHome() =
    Home(
        url = url,
        data = data.map { it.toPost() }
    )