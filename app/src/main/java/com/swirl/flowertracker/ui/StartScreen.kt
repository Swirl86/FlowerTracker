package com.swirl.flowertracker.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.swirl.flowertracker.data.Flower

@Composable
fun StartScreen(flowers: List<Flower>, onAddFlowerClick: () -> Unit) {
    if (flowers.isEmpty()) {
        EmptyScreen(onAddFlowerClick = onAddFlowerClick)
    } else {
        FlowerListScreen(flowers, onAddFlowerClick)
    }
}


@Composable
fun FlowerListScreen(flowers: List<Flower>, onAddFlowerClick: () -> Unit) {
    Column {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(flowers.size) { index ->
                FlowerItem(flower = flowers[index])
            }
        }
        Button(onClick = onAddFlowerClick, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Add New Flower")
        }
    }
}

