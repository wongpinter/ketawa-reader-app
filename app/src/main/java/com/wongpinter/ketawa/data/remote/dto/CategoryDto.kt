package com.wongpinter.ketawa.data.remote.dto

import com.wongpinter.ketawa.domain.model.Categories
import com.wongpinter.ketawa.domain.model.Category
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoriesDto(
    @SerialName("Category")
    val category: List<CategoryDto> = emptyList()
)

fun CategoriesDto.toCategories() =
    Categories(
        categories = category.map { it.toCategory() }
    )

@Serializable
data class CategoryDto(
    @SerialName("CatID")
    val id: String = "",
    @SerialName("CatSub")
    val subCategory: String? = null,
    @SerialName("Content")
    val content: String = ""
)

fun CategoryDto.toCategory() =
    Category(
        id = id,
        subCategory = subCategory,
        content = content.trim()
    )
