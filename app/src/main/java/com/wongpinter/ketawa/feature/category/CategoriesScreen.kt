package com.wongpinter.ketawa.feature.category

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.wongpinter.ketawa.R
import com.wongpinter.ketawa.domain.model.Categories
import com.wongpinter.ketawa.domain.model.Category
import com.wongpinter.ketawa.feature.post.ErrorMessage
import com.wongpinter.ketawa.feature.post.LoadingIndicator
import com.wongpinter.ketawa.presentation.ui.theme.Typography
import com.wongpinter.ketawa.utils.Resource

@Composable
fun CategoriesScreen(
    navController: NavHostController,
    viewModel: CategoriesViewModel = hiltViewModel(),
) {
    val categoriesUiState by viewModel.categoriesUiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = categoriesUiState) {
            is Resource.Loading -> LoadingIndicator()
            is Resource.Error -> ErrorMessage(state.message ?: "An error occurred")
            is Resource.Success -> state.data?.let {
                CategoriesContent(
                    categories = it,
                    onCategoryClick = { categoryId ->
                        navController.navigate("categoryPosts/$categoryId")
                    }
                )
            }
        }
    }
}

@Composable
private fun CategoriesContent(
    categories: Categories,
    onCategoryClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(categories.categories) { category ->
            CategoryItem(
                category = category,
                onClick = { onCategoryClick(category.id) }
            )
            HorizontalDivider()
        }
    }
}

@Composable
private fun CategoryItem(
    category: Category,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_category), // Replace with actual icon
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .padding(end = 16.dp)
        )
        Text(
            text = category.content,
            style = Typography.bodyLarge,
            color = Color.Black
        )
    }
}
