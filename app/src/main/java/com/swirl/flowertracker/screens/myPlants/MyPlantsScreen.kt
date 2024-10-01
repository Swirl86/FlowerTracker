package com.swirl.flowertracker.screens.myPlants

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.swirl.flowertracker.R
import com.swirl.flowertracker.data.model.Flower
import com.swirl.flowertracker.viewmodel.FlowerViewModel

@Composable
fun MyPlantsScreen(
    flowerViewModel: FlowerViewModel,
    onAddFlowerClick: () -> Unit
) {
    // TODO if flowers is not empty check permission for READ_EXTERNAL_STORAGE to show image(s)
    val flowers by flowerViewModel.allFlowers.observeAsState(emptyList())

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
            Text(text = stringResource(R.string.add_new_flower))
        }
    }
}
