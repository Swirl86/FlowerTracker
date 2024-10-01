package com.swirl.flowertracker.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val icon: ImageVector, var title: String) {
    data object MyPlants : Screen("my_plants", Icons.Filled.Home, "My Plants")
    data object AddPlant : Screen("add_plant", Icons.Filled.Add, "Add New")
    data object Search : Screen("search", Icons.Filled.Search, "Search")
}