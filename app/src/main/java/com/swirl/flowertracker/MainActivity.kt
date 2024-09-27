package com.swirl.flowertracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.swirl.flowertracker.navigation.BottomNavigationBar
import com.swirl.flowertracker.navigation.Navigation
import com.swirl.flowertracker.ui.theme.FlowerTrackerTheme
import com.swirl.flowertracker.viewmodel.FlowerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val flowerViewModel: FlowerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FlowerTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    Scaffold(
                        bottomBar = { BottomNavigationBar(navController = navController) }
                    ) { innerPadding ->
                        Navigation(navController = navController, flowerViewModel = flowerViewModel, innerPadding = innerPadding)
                    }
                }
            }
        }
    }
}