package com.wongpinter.ketawa.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wongpinter.ketawa.domain.model.Home
import com.wongpinter.ketawa.domain.model.Post
import com.wongpinter.ketawa.feature.post.ErrorMessage
import com.wongpinter.ketawa.feature.post.LoadingIndicator
import com.wongpinter.ketawa.utils.Resource

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
) {
    val homeUiState by viewModel.homeUiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = homeUiState) {
            is Resource.Loading -> LoadingIndicator()
            is Resource.Error -> ErrorMessage(message = state.message ?: "An error occurred")
            is Resource.Success -> state.data?.let {
                HomeContent(
                    home = it
                )
            }
        }
    }
}

@Composable
fun HomeContent(
    home: Home,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(home.data) { post ->
            PostItem(post = post)
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(modifier = Modifier.padding(vertical = 15.dp), thickness = 2.dp)
        }
    }
}

@Composable
fun PostItem(
    post: Post,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(text = post.title, style = MaterialTheme.typography.titleLarge)
        Text(text = "Category: ${post.categoryName}", style = MaterialTheme.typography.titleSmall)
        Text(text = "Date: ${post.postDate}", style = MaterialTheme.typography.titleSmall)
        Text(text = "Sender: ${post.sender}", style = MaterialTheme.typography.titleSmall)
        Spacer(modifier = Modifier.height(15.dp))
        Text(text = post.content, style = MaterialTheme.typography.bodyLarge)
    }
}
