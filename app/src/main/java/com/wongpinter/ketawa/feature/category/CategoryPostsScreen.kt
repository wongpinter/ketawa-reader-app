package com.wongpinter.ketawa.feature.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wongpinter.ketawa.domain.model.CategoryPosts
import com.wongpinter.ketawa.domain.model.PostTitle
import com.wongpinter.ketawa.feature.post.ErrorMessage
import com.wongpinter.ketawa.feature.post.LoadingIndicator
import com.wongpinter.ketawa.presentation.components.TopbarUiState
import com.wongpinter.ketawa.utils.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryPostsScreen(
    viewModel: CategoryPostViewModel = hiltViewModel(),
    updateTopBar: (TopbarUiState) -> Unit,
    topBarUiState: TopbarUiState,
    categoryId: String,
    onPostClick: (String) -> Unit,
    onCategoryClick: (String) -> Unit
) {
    var currentPage by remember { mutableIntStateOf(1) }
    val categoryPostsUiState by viewModel.categoryPostsUiState.collectAsState()

    LaunchedEffect(categoryId, currentPage) {
        viewModel.getCategoryPosts(categoryId, currentPage)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (val state = categoryPostsUiState) {
            is Resource.Loading -> LoadingIndicator()
            is Resource.Error -> ErrorMessage(state.message ?: "An error occurred")
            is Resource.Success -> state.data?.let {
                updateTopBar(topBarUiState.copy(subtitle = state.data.category))
                CategoryPostsContent(
                    categoryPosts = it,
                    onPostClick = onPostClick,
                    onCategoryClick = onCategoryClick,
                    onPageChange = { newPage -> currentPage = newPage }
                )
            }
        }
    }
}

@Composable
private fun CategoryPostsContent(
    categoryPosts: CategoryPosts,
    onPostClick: (String) -> Unit,
    onCategoryClick: (String) -> Unit,
    onPageChange: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Total Posts: ${categoryPosts.dataCount}",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(categoryPosts.posts) { post ->
                PostTitleItem(post, onPostClick, onCategoryClick)
                HorizontalDivider()
            }
        }
        Pagination(
            currentPage = categoryPosts.page,
            totalPages = categoryPosts.pageCount,
            onPageChange = onPageChange
        )
    }
}

@Composable
private fun PostTitleItem(
    post: PostTitle,
    onPostClick: (String) -> Unit,
    onCategoryClick: (String) -> Unit
) {
    var f = onPostClick
    if (post.postType == "CAT") {
        f = onCategoryClick
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { f(post.postId) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(MaterialTheme.colorScheme.onSecondary, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            val iconRes = if (post.postType == "CAT") {
                Icons.Rounded.Menu // Replace with actual icon
            } else {
                Icons.Rounded.Face // Replace with actual icon
            }
            Icon(
                imageVector = iconRes,
                contentDescription = null,
                modifier = Modifier.size(24.dp), // Adjust size as needed
                tint = Color.White // You can change this color if needed
            )
        }
        Text(
            text = post.content,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
private fun Pagination(
    currentPage: Int,
    totalPages: Int,
    onPageChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { onPageChange(currentPage - 1) },
            enabled = currentPage > 1
        ) {
            Text("Previous")
        }
        Text("Page $currentPage of $totalPages")
        Button(
            onClick = { onPageChange(currentPage + 1) },
            enabled = currentPage < totalPages
        ) {
            Text("Next")
        }
    }
}