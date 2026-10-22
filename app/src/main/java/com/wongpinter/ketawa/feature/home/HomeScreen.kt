package com.wongpinter.ketawa.feature.home

import android.content.Intent
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wongpinter.ketawa.domain.model.Home
import com.wongpinter.ketawa.domain.model.LastReadInfo
import com.wongpinter.ketawa.domain.model.Post
import com.wongpinter.ketawa.feature.post.ErrorMessage
import com.wongpinter.ketawa.feature.post.LoadingIndicator
import com.wongpinter.ketawa.utils.Resource

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onPostClick: (String) -> Unit = {}
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val readPosts by viewModel.readPosts.collectAsState()
    val hideReadJokes by viewModel.hideReadJokes.collectAsState()
    val lastReadPost by viewModel.lastReadPost.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = homeUiState) {
            is Resource.Loading -> LoadingIndicator()
            is Resource.Error -> ErrorMessage(message = state.message ?: androidx.compose.ui.res.stringResource(com.wongpinter.ketawa.R.string.error_loading_jokes))
            is Resource.Success -> state.data?.let {
                HomeContent(
                    home = it,
                    readPosts = readPosts,
                    hideReadJokes = hideReadJokes,
                    lastReadPost = lastReadPost,
                    onPostClick = onPostClick
                )
            }
        }
    }
}

@Composable
fun HomeContent(
    home: Home,
    readPosts: Set<String>,
    hideReadJokes: Boolean,
    lastReadPost: LastReadInfo?,
    modifier: Modifier = Modifier,
    onPostClick: (String) -> Unit = {}
) {
    val visiblePosts = if (hideReadJokes) {
        home.data.filterNot { readPosts.contains(it.id) }
    } else {
        home.data
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (lastReadPost != null) {
            item(key = "resume_card") {
                ResumeReadingCard(
                    lastRead = lastReadPost,
                    onClick = { onPostClick(lastReadPost.id) }
                )
            }
        }

        if (visiblePosts.isEmpty()) {
            item(key = "empty_read") {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = androidx.compose.ui.res.stringResource(com.wongpinter.ketawa.R.string.all_jokes_read),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            items(
                items = visiblePosts,
                key = { it.id }
            ) { post ->
                CompactJokeCard(
                    post = post,
                    isRead = readPosts.contains(post.id),
                    onClick = { onPostClick(post.id) }
                )
            }
        }
    }
}

@Composable
fun ResumeReadingCard(
    lastRead: LastReadInfo,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Rounded.PlayArrow,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = androidx.compose.ui.res.stringResource(com.wongpinter.ketawa.R.string.resume_reading).uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.8.sp,
                            fontSize = 10.sp
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = lastRead.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${lastRead.category}  •  " + androidx.compose.ui.res.stringResource(com.wongpinter.ketawa.R.string.tap_to_continue),
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun CompactJokeCard(
    post: Post,
    isRead: Boolean = false,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isRead) MaterialTheme.colorScheme.surface.copy(alpha = 0.85f) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isRead) 0.5.dp else 1.5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = post.categoryName.ifBlank { "Humor" },
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    if (isRead) {
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "✓  " + androidx.compose.ui.res.stringResource(com.wongpinter.ketawa.R.string.already_read),
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp, fontWeight = FontWeight.Medium),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                }

                IconButton(
                    onClick = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "${post.title}\n\n${post.content}\n\n(${context.getString(com.wongpinter.ketawa.R.string.source_credit)})"
                            )
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, context.getString(com.wongpinter.ketawa.R.string.share)))
                    },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Share,
                        contentDescription = androidx.compose.ui.res.stringResource(com.wongpinter.ketawa.R.string.share),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = post.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 13.5.sp,
                    lineHeight = 20.sp
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val author = post.sender.ifBlank { androidx.compose.ui.res.stringResource(com.wongpinter.ketawa.R.string.anonymous) }
                Text(
                    text = androidx.compose.ui.res.stringResource(com.wongpinter.ketawa.R.string.by_author, author),
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                    color = MaterialTheme.colorScheme.outline
                )

                if (post.postDate.isNotBlank()) {
                    Text(
                        text = post.postDate.take(16),
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}
