package com.wongpinter.ketawa.feature.post

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wongpinter.ketawa.domain.model.Post
import com.wongpinter.ketawa.presentation.components.TopbarUiState
import com.wongpinter.ketawa.utils.Resource
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun PostScreen(
    postId: String,
    viewModel: PostViewModel = hiltViewModel(),
    updateTopBar: (TopbarUiState) -> Unit,
    topBarUiState: TopbarUiState
) {
    val postUiState by viewModel.postUiState.collectAsState()

    LaunchedEffect(postId) {
        viewModel.getPost(postId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = postUiState) {
            is Resource.Loading -> LoadingIndicator()
            is Resource.Error -> ErrorMessage(state.message ?: "An error occurred")
            is Resource.Success -> state.data?.let {
                updateTopBar(topBarUiState.copy(subtitle = state.data.title))
                PostContent(
                    post = it,
                    onPreviousClick = viewModel::getPreviousPost,
                    onNextClick = viewModel::getNextPost
                )
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorMessage(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = message, color = Color.Red)
    }
}

@Composable
private fun PostContent(
    post: Post,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Category: ${post.categoryName}",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            val textDate = SimpleDateFormat(
                "MMM dd, yyyy",
                Locale.getDefault()
            ).format(post.postDate)

            Text(
                text = "By ${post.sender} on $textDate",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        item {
            val content = post.content.replace("<br />", "\n")
            Text(
                text = content.trim(),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onPreviousClick,
                    enabled = post.previousId != null
                ) {
                    Text("Previous")
                }
                Button(
                    onClick = onNextClick,
                    enabled = post.nextId != null
                ) {
                    Text("Next")
                }
            }
        }
    }
}
