package com.wongpinter.ketawa.feature.category

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.SentimentSatisfiedAlt
import androidx.compose.material.icons.rounded.UnfoldMore
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wongpinter.ketawa.R
import com.wongpinter.ketawa.domain.model.CategoryPosts
import com.wongpinter.ketawa.domain.model.PostTitle
import com.wongpinter.ketawa.feature.post.ErrorMessage
import com.wongpinter.ketawa.feature.post.LoadingIndicator
import com.wongpinter.ketawa.presentation.components.TopbarUiState
import com.wongpinter.ketawa.utils.Resource

@Composable
fun CategoryPostsScreen(
    viewModel: CategoryPostViewModel = hiltViewModel(),
    updateTopBar: (TopbarUiState) -> Unit,
    topBarUiState: TopbarUiState,
    categoryId: String,
    onPostClick: (String) -> Unit,
    onCategoryClick: (String) -> Unit
) {
    val context = LocalContext.current
    var currentPage by remember { mutableIntStateOf(1) }
    val categoryPostsUiState by viewModel.categoryPostsUiState.collectAsState()
    val downloadState by viewModel.downloadState.collectAsState()
    val readPosts by viewModel.readPosts.collectAsState()

    LaunchedEffect(categoryId, currentPage) {
        viewModel.getCategoryPosts(categoryId, currentPage)
    }

    LaunchedEffect(downloadState) {
        when (val d = downloadState) {
            is Resource.Success -> {
                val count = d.data ?: 0
                Toast.makeText(context, context.getString(R.string.download_success, count), Toast.LENGTH_LONG).show()
                viewModel.resetDownloadState()
            }
            is Resource.Error -> {
                Toast.makeText(context, d.message ?: "Gagal mengunduh", Toast.LENGTH_SHORT).show()
                viewModel.resetDownloadState()
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (val state = categoryPostsUiState) {
            is Resource.Loading -> LoadingIndicator()
            is Resource.Error -> ErrorMessage(state.message ?: stringResource(R.string.error_loading_jokes))
            is Resource.Success -> state.data?.let {
                updateTopBar(
                    topBarUiState.copy(
                        titleRes = R.string.nav_categories,
                        titleString = null,
                        subtitleString = state.data.category,
                        subtitleRes = null
                    )
                )
                CategoryPostsContent(
                    categoryPosts = it,
                    readPosts = readPosts,
                    isDownloading = downloadState is Resource.Loading,
                    onDownloadClick = { viewModel.downloadCategory(categoryId) },
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
    readPosts: Set<String>,
    isDownloading: Boolean,
    onDownloadClick: () -> Unit,
    onPostClick: (String) -> Unit,
    onCategoryClick: (String) -> Unit,
    onPageChange: (Int) -> Unit
) {
    val context = LocalContext.current
    var showGoToPageDialog by remember { mutableStateOf(false) }

    if (showGoToPageDialog) {
        var pageInput by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showGoToPageDialog = false },
            title = {
                Text(
                    text = stringResource(R.string.go_to_page),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column {
                    Text(
                        text = stringResource(R.string.enter_page_number, categoryPosts.pageCount),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = pageInput,
                        onValueChange = { pageInput = it.filter { c -> c.isDigit() } },
                        placeholder = { Text(categoryPosts.page.toString()) },
                        label = { Text(stringResource(R.string.page_number)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = androidx.compose.ui.text.input.ImeAction.Done),
                        keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                            onDone = {
                                val target = pageInput.toIntOrNull() ?: categoryPosts.page
                                if (target in 1..categoryPosts.pageCount) {
                                    onPageChange(target)
                                    showGoToPageDialog = false
                                } else {
                                    Toast.makeText(context, context.getString(R.string.invalid_page, categoryPosts.pageCount), Toast.LENGTH_SHORT).show()
                                }
                            }
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val target = pageInput.toIntOrNull() ?: categoryPosts.page
                        if (target in 1..categoryPosts.pageCount) {
                            onPageChange(target)
                            showGoToPageDialog = false
                        } else {
                            Toast.makeText(context, context.getString(R.string.invalid_page, categoryPosts.pageCount), Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text(stringResource(R.string.go))
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showGoToPageDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${categoryPosts.category}  •  ${categoryPosts.dataCount} lelucon",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.5.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.clip(RoundedCornerShape(8.dp)).clickable(enabled = !isDownloading) { onDownloadClick() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (isDownloading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(12.dp),
                                strokeWidth = 1.5.dp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Rounded.Download,
                                contentDescription = stringResource(R.string.download_offline),
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        Text(
                            text = if (isDownloading) stringResource(R.string.downloading_offline) else stringResource(R.string.download_offline),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 11.sp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = categoryPosts.posts,
                key = { it.postId }
            ) { post ->
                CompactPostTitleItem(
                    post = post,
                    isRead = readPosts.contains(post.postId),
                    onPostClick = onPostClick,
                    onCategoryClick = onCategoryClick
                )
            }
        }

        Surface(
            tonalElevation = 3.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { onPageChange(categoryPosts.page - 1) },
                    enabled = categoryPosts.page > 1,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(R.string.previous), style = MaterialTheme.typography.labelMedium)
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { showGoToPageDialog = true }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "${categoryPosts.page} / ${categoryPosts.pageCount}",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, fontSize = 13.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Icon(
                            imageVector = Icons.Rounded.UnfoldMore,
                            contentDescription = stringResource(R.string.go_to_page),
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                FilledTonalButton(
                    onClick = { onPageChange(categoryPosts.page + 1) },
                    enabled = categoryPosts.page < categoryPosts.pageCount,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Text(stringResource(R.string.next), style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CompactPostTitleItem(
    post: PostTitle,
    isRead: Boolean = false,
    onPostClick: (String) -> Unit,
    onCategoryClick: (String) -> Unit
) {
    val isCat = post.postType == "CAT"
    val clickAction = if (isCat) onCategoryClick else onPostClick

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { clickAction(post.postId) },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isRead) MaterialTheme.colorScheme.surface.copy(alpha = 0.85f) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isRead) 0.5.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 11.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = if (isCat) Icons.Rounded.Folder else Icons.Rounded.SentimentSatisfiedAlt,
                    contentDescription = null,
                    tint = if (isCat) MaterialTheme.colorScheme.primary else (if (isRead) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.secondary),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = post.content,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = if (isRead) FontWeight.Normal else FontWeight.Medium,
                        fontSize = 14.sp
                    ),
                    color = if (isRead) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                )
            }

            if (isRead && !isCat) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.padding(end = 6.dp)
                ) {
                    Text(
                        text = "✓ " + stringResource(R.string.already_read),
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
