package com.wongpinter.ketawa.presentation.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.wongpinter.ketawa.presentation.ui.theme.onPrimaryLight
import com.wongpinter.ketawa.presentation.ui.theme.onSecondaryContainerLight
import com.wongpinter.ketawa.presentation.ui.theme.surfaceContainerLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavTopBar(topbarUiState: TopbarUiState, navController: NavController) {
    TopAppBar(
        title = topbarUiState.setTitle(),
        windowInsets = WindowInsets(bottom = 2.dp, top = 8.dp),
        modifier = Modifier.height(65.dp),
        navigationIcon = {
            if (topbarUiState.backButton) {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        },
        colors = topAppBarColors(
            containerColor = onSecondaryContainerLight,
            scrolledContainerColor = surfaceContainerLight,
            navigationIconContentColor = onPrimaryLight,
            titleContentColor = onPrimaryLight,
            actionIconContentColor = onPrimaryLight
        )
    )
}