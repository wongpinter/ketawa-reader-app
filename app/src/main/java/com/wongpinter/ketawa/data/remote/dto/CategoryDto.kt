package com.wongpinter.ketawa.data.remote.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.wongpinter.ketawa.domain.model.Categories
import com.wongpinter.ketawa.domain.model.Category

data class CategoriesDto(
    @JsonProperty("Category")
    val category: List<CategoryDto>
)

fun CategoriesDto.toCategories() =
    Categories(
        categories = category.map { it.toCategory() }
    )

data class CategoryDto(
    @JsonProperty("CatID")
    val id: String,
    @JsonProperty("CatSub")
    val subCategory: String?,
    @JsonProperty("Content")
    val content: String
)

fun CategoryDto.toCategory() =
    Category(
        id = id,
        subCategory = subCategory,
        content = content
    )