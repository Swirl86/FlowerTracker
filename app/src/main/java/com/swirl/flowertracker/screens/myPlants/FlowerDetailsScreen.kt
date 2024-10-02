package com.swirl.flowertracker.screens.myPlants

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.swirl.flowertracker.data.model.Flower
import com.swirl.flowertracker.screens.common.FlowerImage
import com.swirl.flowertracker.screens.myPlants.common.CustomTextField
import com.swirl.flowertracker.utils.stringToDate
import com.swirl.flowertracker.viewmodel.FlowerViewModel
import kotlinx.coroutines.launch

@Composable
fun FlowerDetailsScreen(flowerId: Int?, flowerViewModel: FlowerViewModel) {
    flowerViewModel.setFlowerId(flowerId)

    val flower by flowerViewModel.flower.collectAsState()

    var imageUri by rememberSaveable { mutableStateOf<String?>(null) }
    var name by rememberSaveable { mutableStateOf("") }
    var notes by rememberSaveable { mutableStateOf("") }
    var lastWatered by rememberSaveable { mutableStateOf("") }
    var waterInDays by rememberSaveable { mutableStateOf("") }
    var lastFertilized by rememberSaveable { mutableStateOf("") }
    var fertilizeInDays by rememberSaveable { mutableStateOf("") }


    val scrollState = rememberScrollState()

    LaunchedEffect(flower) {
        flower?.let {
            imageUri = it.imageUri
            name = it.name
            notes = it.notes ?: ""
            lastWatered = it.lastWatered?.toString() ?: ""
            waterInDays = it.waterInDays?.toString() ?: ""
            lastFertilized = it.lastFertilized?.toString() ?: ""
            fertilizeInDays = it.fertilizeInDays?.toString() ?: ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        flower?.let { flower ->
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        width = 2.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        // TODO implement permission check and change img logic
                        /*if (permissionsGranted) {
                            showImagePickerDialog = true
                        } else {
                            showPermissionDialog = true
                        }*/
                    },
                contentAlignment = Alignment.Center
            ) {
                FlowerImage(imageUri = flower.imageUri)
            }

            CustomTextField("Name", name) { name = it }
            CustomTextField("Notes", notes) { notes = it }
            CustomTextField("Last Watered", lastWatered) { lastWatered = it }
            CustomTextField("Water in X Days", waterInDays, true) { waterInDays = it }
            CustomTextField("Last Fertilized", lastFertilized) { lastFertilized = it }
            CustomTextField("Fertilize in X Days", fertilizeInDays, true) { fertilizeInDays = it }


            // TODO add more logic checks e.g. img and name must exist
            Button(
                onClick = {
                    val updatedFlower = flower.copy(
                        imageUri = imageUri,
                        name = name,
                        notes = notes,
                        lastWatered = lastWatered.takeIf { it.isNotEmpty() }?.stringToDate(),
                        waterInDays = waterInDays.toIntOrNull(),
                        lastFertilized = lastFertilized.takeIf { it.isNotEmpty() }?.stringToDate(),
                        fertilizeInDays = fertilizeInDays.toIntOrNull()
                    )
                    flowerViewModel.viewModelScope.launch {
                        flowerViewModel.updateFlower(updatedFlower)
                    }
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Save Changes")
            }
        } ?: run {
            Text(text = "Flower not found", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
