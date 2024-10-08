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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.swirl.flowertracker.R
import com.swirl.flowertracker.permissions.CheckPermissions
import com.swirl.flowertracker.permissions.PermissionManager
import com.swirl.flowertracker.screens.common.FlowerImage
import com.swirl.flowertracker.screens.common.customImagePicker
import com.swirl.flowertracker.screens.myPlants.common.CustomTextField
import com.swirl.flowertracker.screens.myPlants.common.IconDatePicker
import com.swirl.flowertracker.utils.daysFromTodayToLocalDate
import com.swirl.flowertracker.utils.localDateToRemainingDays
import com.swirl.flowertracker.utils.stringToDate
import com.swirl.flowertracker.viewmodel.FlowerViewModel
import kotlinx.coroutines.launch

@Composable
fun FlowerDetailsScreen(
    flowerId: Int?,
    flowerViewModel: FlowerViewModel = hiltViewModel(),
    permissionManager: PermissionManager = hiltViewModel()
) {
    val context = LocalContext.current

    flowerViewModel.setFlowerId(flowerId)

    val flower by flowerViewModel.flower.collectAsState()

    var imageUri by rememberSaveable { mutableStateOf<String?>(null) }
    var name by rememberSaveable { mutableStateOf("") }
    var notes by rememberSaveable { mutableStateOf("") }
    var lastWatered by rememberSaveable { mutableStateOf("") }
    var waterInDays by rememberSaveable { mutableStateOf("") }
    var lastFertilized by rememberSaveable { mutableStateOf("") }
    var fertilizeInDays by rememberSaveable { mutableStateOf("") }

    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf(context.getString(R.string.error_message_failed_save_flower)) }

    CheckPermissions(permissionManager)
    // Observe the permission status
    val isPermissionsGranted by permissionManager.permissionsGranted.observeAsState(initial = false)

    val (_, openImagePicker) = customImagePicker(
        context,
        onImageUriSelected = { uri -> imageUri = uri.toString() },
        onError = { error ->
            showErrorDialog = true
            errorMessage = error
        }
    )

    LaunchedEffect(flower) {
        flower?.let {
            imageUri = it.imageUri
            name = it.name
            notes = it.notes ?: ""
            lastWatered = it.lastWatered?.toString() ?: ""
            waterInDays = it.waterInDays?.localDateToRemainingDays()?.toString() ?: ""
            lastFertilized = it.lastFertilized?.toString() ?: ""
            fertilizeInDays = it.fertilizeInDays?.localDateToRemainingDays()?.toString() ?: ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
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
                        if (isPermissionsGranted) {
                            openImagePicker()
                        } else {
                            permissionManager.onPermissionsDenied()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                FlowerImage(
                    modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp)),
                    imageUri = imageUri,
                    contentScale = ContentScale.Crop
                )
            }

            CustomTextField(stringResource(R.string.flower_name_label), name) { name = it }
            CustomTextField(stringResource(R.string.flower_notes_label), notes) { notes = it }
            IconDatePicker(
                label = stringResource(R.string.flower_last_watered_label),
                dateValue = lastWatered,
                onDateSelected = { selectedDate ->
                    lastWatered = selectedDate
                }
            )
            CustomTextField(stringResource(R.string.flower_next_watered_label), waterInDays, true) { waterInDays = it }
            IconDatePicker(
                label = stringResource(R.string.flower_last_fertilized_label),
                dateValue = lastFertilized,
                onDateSelected = { selectedDate ->
                    lastFertilized = selectedDate
                }
            )
            CustomTextField(stringResource(R.string.flower_next_fertilized_label), fertilizeInDays, true) { fertilizeInDays = it }


            // TODO add more logic checks e.g. img and name must exist
            Button(
                onClick = {
                    val updatedFlower = flower.copy(
                        imageUri = imageUri,
                        name = name,
                        notes = notes,
                        lastWatered = lastWatered.takeIf { it.isNotEmpty() }?.stringToDate(),
                        waterInDays = waterInDays.toIntOrNull()?.daysFromTodayToLocalDate(),
                        lastFertilized = lastFertilized.takeIf { it.isNotEmpty() }?.stringToDate(),
                        fertilizeInDays = fertilizeInDays.toIntOrNull()?.daysFromTodayToLocalDate()
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
