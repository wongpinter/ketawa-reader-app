package com.wongpinter.ketawa.presentation.components

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.wongpinter.ketawa.presentation.ui.theme.onSecondaryContainerLight
import com.wongpinter.ketawa.presentation.ui.theme.secondaryContainerLight
import com.wongpinter.ketawa.presentation.ui.theme.secondaryDarkHighContrast
import com.wongpinter.ketawa.presentation.ui.theme.secondaryLightHighContrast

data class NavItem(
    val route: String,
    val icon: ImageVector
)

@Composable
fun NavigationBottomBar(
    navController: NavController,
    items: List<NavItem>,
    modifier: Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomAppBar(
        modifier = modifier,
        containerColor = onSecondaryContainerLight
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(item.route) { inclusive = true } // Adjust this as needed
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.route)
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = secondaryContainerLight,
                    unselectedIconColor = secondaryDarkHighContrast,
                    indicatorColor = secondaryLightHighContrast
                )
            )
        }
    }
}