package com.swirl.flowertracker.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.swirl.flowertracker.ui.theme.DarkBackground

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White
    ) {
        val screens = listOf(Screen.MyPlants, Screen.AddPlant, Screen.Search)

        screens.forEach { screen ->
            val isSelected = currentRoute == screen.route
            BottomNavigationItem(
                icon = { Icon(screen.icon, contentDescription = null, tint = Color.White) },
                label = { Text(screen.title, color = Color.White) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        // Clear the back stack to prevent back navigation
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                    }
                },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White,
                modifier = Modifier
                    .background(if (isSelected) DarkBackground else Color.Transparent)
                    .padding(8.dp)
            )
        }
    }
}