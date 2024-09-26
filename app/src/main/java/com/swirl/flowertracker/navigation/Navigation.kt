package com.swirl.flowertracker.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.swirl.flowertracker.screens.addFlower.AddFlowerScreen
import com.swirl.flowertracker.screens.myPlants.MyPlantsScreen
import com.swirl.flowertracker.screens.search.SearchScreen
import com.swirl.flowertracker.viewmodel.FlowerViewModel

@Composable
fun Navigation(navController: NavHostController, flowerViewModel: FlowerViewModel, innerPadding: PaddingValues) {
    NavHost(navController = navController, startDestination = Screen.MyPlants.route, Modifier.padding(innerPadding)) {
        composable(route = Screen.MyPlants.route) {
            MyPlantsScreen(
                flowers = flowerViewModel.allFlowers.value.orEmpty(),
                onAddFlowerClick = {
                    navController.navigate(Screen.AddPlant.route)
                }
            )
        }
        composable(route = Screen.AddPlant.route) {
            AddFlowerScreen()
        }
        composable(route = Screen.Search.route) {
            SearchScreen()
        }
    }
}