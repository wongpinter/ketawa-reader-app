package com.wongpinter.ketawa.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.compose.rememberNavController
import com.wongpinter.ketawa.R


data class TopbarUiState(
    val title: String,
    val subtitle: String?,
    val backButton: Boolean,
) {
    fun setTitle(): @Composable () -> Unit {
        return {
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                )
                if (subtitle != null) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = subtitle,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        }
    }
}

@Composable
fun NavigationScreen() {
    val navHostController = rememberNavController()
    var topBarVisible by remember { mutableStateOf(true) }
    var bottomBarVisible by remember { mutableStateOf(true) }
    var topBarUiState by remember {
        mutableStateOf(
            TopbarUiState(
                title = "Ketawa.com",
                subtitle = "Home",
                backButton = false
            )
        )
    }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val deltaY = available.y
                if (deltaY < 0) {
                    // Swiping up
                    topBarVisible = false
                    bottomBarVisible = false
                } else if (deltaY > 0) {
                    // Swiping down
                    topBarVisible = true
                    bottomBarVisible = true
                }
                return super.onPreScroll(available, source)
            }
        }
    }

    Scaffold(
        topBar = {
            NavTopBar(topBarUiState, navHostController)
        },
        bottomBar = {
            AnimatedVisibility(visible = bottomBarVisible) {
                NavigationBottomBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .nestedScroll(nestedScrollConnection),
                    navController = navHostController,
                    items = listOf(
                        NavItem(route = "home", icon = R.drawable.ic_home),
                        NavItem(route = "categories", icon = R.drawable.ic_category)
                    )
                )
            }
        },
        content = { paddingVal ->
            NavigatorHost(
                navHostController = navHostController,
                modifier = Modifier
                    .padding(paddingVal)
                    .nestedScroll(nestedScrollConnection),
                updateTopBar = { newTopBarState ->
                    topBarUiState = newTopBarState
                },
                topbarUiState = topBarUiState
            )
        }
    )
}