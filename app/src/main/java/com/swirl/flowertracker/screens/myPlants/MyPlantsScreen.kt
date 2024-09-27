package com.swirl.flowertracker.screens.myPlants

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.swirl.flowertracker.data.model.Flower

@Composable
fun MyPlantsScreen(
    flowers: List<Flower>,
    onAddFlowerClick: () -> Unit
) {

    Log.i("TAG-1", flowers.toString())
    Column(modifier = Modifier.fillMaxSize()) {
        if (flowers.isEmpty()) {
            EmptyScreen(onAddFlowerClick = onAddFlowerClick)
        } else {
            FlowerListScreen(flowers, onAddFlowerClick)
        }
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
