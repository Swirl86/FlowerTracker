package com.swirl.flowertracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.swirl.flowertracker.ui.StartScreen
import com.swirl.flowertracker.ui.theme.FlowerTrackerTheme
import com.swirl.flowertracker.viewmodel.FlowerViewModel
import com.swirl.flowertracker.viewmodel.FlowerViewModelFactory

class MainActivity : ComponentActivity() {

    private val flowerViewModel: FlowerViewModel by viewModels {
        FlowerViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlowerTrackerTheme {
                Surface {
                    val flowerList by flowerViewModel.allFlowers.collectAsState(initial = emptyList())
                    StartScreen(flowerList, onAddFlowerClick = { /* handle add flower */ })
                }
            }
        }
    }
}
