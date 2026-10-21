package com.wongpinter.ketawa.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.compose.rememberNavController

data class TopbarUiState(
    val title: String,
    val subtitle: String?,
    val backButton: Boolean,
) {
    fun setTitle(): @Composable () -> Unit {
        return {
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (!subtitle.isNullOrBlank()) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun NavigationScreen() {
    val navHostController = rememberNavController()
    var topBarUiState by remember {
        mutableStateOf(
            TopbarUiState(
                title = "Ketawa.com",
                subtitle = "Cerita Humor Indonesia",
                backButton = false
            )
        )
    }

    Scaffold(
        topBar = {
            NavTopBar(topBarUiState, navHostController)
        },
        bottomBar = {
            NavigationBottomBar(
                modifier = Modifier.fillMaxWidth(),
                navController = navHostController,
                items = listOf(
                    NavItem(route = "home", title = "Terbaru", icon = Icons.Rounded.Home),
                    NavItem(route = "categories", title = "Kategori", icon = Icons.Rounded.Category)
                )
            )
        },
        content = { paddingVal ->
            NavigatorHost(
                navHostController = navHostController,
                modifier = Modifier.padding(paddingVal),
                updateTopBar = { newTopBarState ->
                    topBarUiState = newTopBarState
                },
                topbarUiState = topBarUiState
            )
        }
    )
}
