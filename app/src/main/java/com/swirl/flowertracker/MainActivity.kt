package com.swirl.flowertracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.swirl.flowertracker.ui.AddFlowerScreen
import com.swirl.flowertracker.ui.StartScreen
import com.swirl.flowertracker.ui.theme.FlowerTrackerTheme
import com.swirl.flowertracker.viewmodel.FlowerViewModel
import com.swirl.flowertracker.viewmodel.FlowerViewModelFactory

class MainActivity : ComponentActivity() {

    private val flowerViewModel: FlowerViewModel by viewModels { FlowerViewModelFactory(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FlowerTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var isAddingFlower by remember { mutableStateOf(false) }

                    if (isAddingFlower) {
                        AddFlowerScreen(onBackClick = { isAddingFlower = false })
                    } else {
                        val flowers by flowerViewModel.allFlowers.observeAsState(emptyList())
                        StartScreen(
                            flowers = flowers,
                            onAddFlowerClick = { isAddingFlower = true }
                        )
                    }
                }
            }
        }
    }
}