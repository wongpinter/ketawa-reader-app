package com.wongpinter.ketawa.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.wongpinter.ketawa.R

data class TopbarUiState(
    val titleRes: Int? = null,
    val titleString: String? = null,
    val subtitleRes: Int? = null,
    val subtitleString: String? = null,
    val backButton: Boolean = false,
    val showLogo: Boolean = false
) {
    fun setTitle(): @Composable () -> Unit {
        return {
            val displayTitle = titleRes?.let { stringResource(it) } ?: titleString ?: ""
            val displaySubtitle = subtitleRes?.let { stringResource(it) } ?: subtitleString
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (showLogo) {
                    Image(
                        painter = painterResource(R.drawable.ic_ketawa_logo),
                        contentDescription = null,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Column(verticalArrangement = Arrangement.Center) {
                    Text(
                        text = displayTitle,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (!displaySubtitle.isNullOrBlank()) {
                        Text(
                            text = displaySubtitle,
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
}

@Composable
fun NavigationScreen(
    currentLanguage: String = "id",
    onToggleLanguage: () -> Unit = {}
) {
    val navHostController = rememberNavController()
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isReadingMode = currentRoute?.startsWith("post/") == true

    var showBottomBarInReadMode by remember { mutableStateOf(false) }

    LaunchedEffect(currentRoute) {
        if (isReadingMode) {
            showBottomBarInReadMode = false
        }
    }

    val isBottomBarVisible = !isReadingMode || showBottomBarInReadMode

    var topBarUiState by remember {
        mutableStateOf(
            TopbarUiState(
                titleRes = R.string.app_brand,
                subtitleRes = R.string.app_subtitle,
                backButton = false
            )
        )
    }

    Scaffold(
        topBar = {
            NavTopBar(
                topbarUiState = topBarUiState,
                navController = navHostController,
                currentLanguage = currentLanguage,
                onToggleLanguage = onToggleLanguage
            )
        },
        bottomBar = {
            AnimatedVisibility(
                visible = isBottomBarVisible,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                NavigationBottomBar(
                    modifier = Modifier.fillMaxWidth(),
                    navController = navHostController,
                    items = listOf(
                        NavItem(route = "home", title = stringResource(R.string.nav_latest), icon = Icons.Rounded.Home),
                        NavItem(route = "categories", title = stringResource(R.string.nav_categories), icon = Icons.Rounded.Category),
                        NavItem(route = "settings", title = stringResource(R.string.nav_settings), icon = Icons.Rounded.Settings)
                    )
                )
            }
        },
        content = { paddingVal ->
            NavigatorHost(
                navHostController = navHostController,
                modifier = Modifier.padding(paddingVal),
                updateTopBar = { newTopBarState ->
                    topBarUiState = newTopBarState
                },
                topbarUiState = topBarUiState,
                onToggleBottomBar = {
                    if (isReadingMode) {
                        showBottomBarInReadMode = !showBottomBarInReadMode
                    }
                }
            )
        }
    )
}
