package com.wongpinter.ketawa.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.wongpinter.ketawa.feature.category.CategoriesScreen
import com.wongpinter.ketawa.feature.category.CategoriesViewModel
import com.wongpinter.ketawa.feature.category.CategoryPostViewModel
import com.wongpinter.ketawa.feature.category.CategoryPostsScreen
import com.wongpinter.ketawa.feature.home.HomeScreen
import com.wongpinter.ketawa.feature.home.HomeViewModel
import com.wongpinter.ketawa.feature.post.PostScreen
import com.wongpinter.ketawa.feature.post.PostViewModel

@Composable
fun NavigatorHost(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    updateTopBar: (TopbarUiState) -> Unit,
    topbarUiState: TopbarUiState,
    homeViewModel: HomeViewModel = hiltViewModel(),
    categoriesViewModel: CategoriesViewModel = hiltViewModel(),
    categoryPostViewModel: CategoryPostViewModel = hiltViewModel(),
    postViewModel: PostViewModel = hiltViewModel()
) {
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = "home"
    ) {
        composable(route = "home") {
            updateTopBar(
                TopbarUiState(
                    backButton = false,
                    title = "Ketawa.com",
                    subtitle = "Latest Posts"
                )
            )

            HomeScreen(viewModel = homeViewModel)
        }

        composable(route = "categories") {
            updateTopBar(
                TopbarUiState(
                    backButton = false,
                    title = "Ketawa.com",
                    subtitle = "Categories"
                )
            )

            CategoriesScreen(viewModel = categoriesViewModel, navController = navHostController)
        }

        composable(route = "categoryPosts/{categoryId}") { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: return@composable

            updateTopBar(
                TopbarUiState(
                    backButton = true,
                    title = "Category Posts",
                    subtitle = ""
                )
            )

            CategoryPostsScreen(
                viewModel = categoryPostViewModel,
                updateTopBar = updateTopBar,
                topBarUiState = topbarUiState,
                categoryId = categoryId,
                onPostClick = { postId ->
                    navHostController.navigate("post/$postId")
                },
                onCategoryClick = { postId ->
                    navHostController.navigate("categoryPosts/$postId")
                }
            )
        }

        composable(route = "post/{postId}") { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId") ?: return@composable

            updateTopBar(
                TopbarUiState(
                    backButton = true,
                    title = "Post",
                    subtitle = ""
                )
            )

            PostScreen(
                postId = postId,
                viewModel = postViewModel,
                updateTopBar = updateTopBar,
                topBarUiState = topbarUiState,
            )
        }
    }
}
