package com.swirl.flowertracker.screens.myPlants

import android.widget.Toast
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.swirl.flowertracker.R
import com.swirl.flowertracker.data.model.Flower
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
    onFlowerUpdateSaved: () -> Unit
) {
    val context = LocalContext.current
    val permissionManager: PermissionManager = hiltViewModel()
    val flowerViewModel: FlowerViewModel = hiltViewModel()

    flowerViewModel.setFlowerId(flowerId)

    val flower by flowerViewModel.flower.collectAsState()
    // Store original values to compare later
    val originalFlowerDetails = remember { mutableStateOf(flower) }

    var imageUri by rememberSaveable { mutableStateOf<String?>(null) }
    var flowerName by rememberSaveable { mutableStateOf("") }
    var notes by rememberSaveable { mutableStateOf("") }
    var lastWatered by rememberSaveable { mutableStateOf("") }
    var waterInDays by rememberSaveable { mutableStateOf("") }
    var lastFertilized by rememberSaveable { mutableStateOf("") }
    var fertilizeInDays by rememberSaveable { mutableStateOf("") }

    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf(context.getString(R.string.error_message_failed_save_flower)) }

    CheckPermissions(permissionManager)
    // Observe the permission status
    val isPermissionsGranted by permissionManager.permissionsGranted.collectAsState()

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
            originalFlowerDetails.value = it
            imageUri = it.imageUri
            flowerName = it.name
            notes = it.notes ?: ""
            lastWatered = it.lastWatered?.toString() ?: ""
            waterInDays = it.waterInDays?.localDateToRemainingDays()?.toString() ?: ""
            lastFertilized = it.lastFertilized?.toString() ?: ""
            fertilizeInDays = it.fertilizeInDays?.localDateToRemainingDays()?.toString() ?: ""
        }
    }

    fun hasFlowerChanged(updatedFlower: Flower): Boolean {
        return updatedFlower.imageUri != originalFlowerDetails.value?.imageUri ||
                updatedFlower.name != originalFlowerDetails.value?.name ||
                updatedFlower.notes != originalFlowerDetails.value?.notes ||
                updatedFlower.lastWatered != originalFlowerDetails.value?.lastWatered ||
                updatedFlower.waterInDays != originalFlowerDetails.value?.waterInDays ||
                updatedFlower.lastFertilized != originalFlowerDetails.value?.lastFertilized ||
                updatedFlower.fertilizeInDays != originalFlowerDetails.value?.fertilizeInDays
    }

    val isSaveEnabled = flowerName.isNotBlank() && imageUri != null

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

            CustomTextField(stringResource(R.string.flower_name_label), flowerName) { flowerName = it }
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

            Button(
                onClick = {
                    val updatedFlower = flower.copy(
                        imageUri = imageUri,
                        name = flowerName,
                        notes = notes,
                        lastWatered = lastWatered.takeIf { it.isNotEmpty() }?.stringToDate(),
                        waterInDays = waterInDays.toIntOrNull()?.daysFromTodayToLocalDate(),
                        lastFertilized = lastFertilized.takeIf { it.isNotEmpty() }?.stringToDate(),
                        fertilizeInDays = fertilizeInDays.toIntOrNull()?.daysFromTodayToLocalDate()
                    )
                    if (hasFlowerChanged(updatedFlower)) {
                        flowerViewModel.viewModelScope.launch {
                            val saveSuccessful = flowerViewModel.updateFlower(updatedFlower)
                            if (saveSuccessful) {
                                onFlowerUpdateSaved()
                            } else {
                                errorMessage = context.getString(R.string.error_message_failed_save_flower)
                                showErrorDialog = true
                            }
                        }
                    } else {
                        Toast.makeText(context, context.getString(R.string.info_no_change), Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.padding(top = 16.dp),
                enabled = isSaveEnabled
            ) {
                Text(stringResource(R.string.change_save_button))
            }
        } ?: run {
            Text(text = stringResource(R.string.error_message_not_found), style = MaterialTheme.typography.bodyLarge)
        }
    }
}
