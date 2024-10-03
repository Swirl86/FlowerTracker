package com.swirl.flowertracker.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.swirl.flowertracker.screens.addFlower.AddFlowerScreen
import com.swirl.flowertracker.screens.myPlants.FlowerDetailsScreen
import com.swirl.flowertracker.screens.myPlants.MyPlantsScreen
import com.swirl.flowertracker.screens.search.SearchScreen
import com.swirl.flowertracker.viewmodel.FlowerViewModel

@Composable
fun Navigation(navController: NavHostController, flowerViewModel: FlowerViewModel, innerPadding: PaddingValues) {
    NavHost(navController = navController, startDestination = Screen.MyPlants.route, Modifier.padding(innerPadding)) {
        composable(route = Screen.MyPlants.route) {
            MyPlantsScreen(
                onAddFlowerClick = {
                    navController.navigate(Screen.AddPlant.route)
                },
                onFlowerDetailClick = { flowerId ->
                    navController.navigate("flowerDetails/$flowerId")
                }
            )
        }
        composable("flowerDetails/{flowerId}") { backStackEntry ->
            val flowerId = backStackEntry.arguments?.getString("flowerId")?.toInt()
            FlowerDetailsScreen(flowerId = flowerId)
        }
        composable(route = Screen.AddPlant.route) {
            AddFlowerScreen(
                onFlowerSaved = {
                    navController.navigate(Screen.MyPlants.route) {
                        popUpTo(Screen.MyPlants.route) { inclusive = true }
                    }
                }
            )
        }
        composable(route = Screen.Search.route) {
            SearchScreen()
        }
    }
}