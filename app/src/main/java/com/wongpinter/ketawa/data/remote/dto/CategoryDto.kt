package com.wongpinter.ketawa.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.wongpinter.ketawa.domain.model.Categories
import com.wongpinter.ketawa.domain.model.Category

data class CategoriesDto(
    @SerializedName("Category")
    val category: List<CategoryDto>
)

fun CategoriesDto.toCategories() =
    Categories(
        categories = category.map { it.toCategory() }
    )

data class CategoryDto(
    @SerializedName("CatID")
    val id: String,
    @SerializedName("CatSub")
    val subCategory: String?,
    @SerializedName("Content")
    val content: String
)

fun CategoryDto.toCategory() =
    Category(
        id = id,
        subCategory = subCategory,
        content = content
    )