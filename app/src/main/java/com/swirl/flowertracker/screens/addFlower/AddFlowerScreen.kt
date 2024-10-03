package com.swirl.flowertracker.screens.addFlower

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.rememberAsyncImagePainter
import com.swirl.flowertracker.R
import com.swirl.flowertracker.data.model.Flower
import com.swirl.flowertracker.permissions.CheckPermissions
import com.swirl.flowertracker.permissions.PermissionManager
import com.swirl.flowertracker.screens.addFlower.common.CustomOutlinedTextField
import com.swirl.flowertracker.screens.addFlower.common.DatePickerButton
import com.swirl.flowertracker.screens.common.ErrorDialog
import com.swirl.flowertracker.utils.saveBitmapToInternalStorage
import com.swirl.flowertracker.utils.saveImageToInternalStorage
import com.swirl.flowertracker.utils.stringToDate
import com.swirl.flowertracker.viewmodel.FlowerViewModel
import kotlinx.coroutines.launch

@Composable
fun AddFlowerScreen(
    onFlowerSaved: () -> Unit,
    flowerViewModel: FlowerViewModel = hiltViewModel(),
    permissionManager: PermissionManager = hiltViewModel()
) {
    val context = LocalContext.current

    var imageUri by rememberSaveable { mutableStateOf<String?>(null) }
    var flowerName by rememberSaveable { mutableStateOf("") }
    var notes by rememberSaveable { mutableStateOf("") }
    var lastWateredDate by rememberSaveable { mutableStateOf("") }
    var nextWatering by rememberSaveable { mutableStateOf("") }
    var lastFertilizedDate by rememberSaveable { mutableStateOf("") }
    var nextFertilizing by rememberSaveable {  mutableStateOf("") }

    var showImagePickerDialog by remember { mutableStateOf(false) }

    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf(context.getString(R.string.error_message_failed_save_flower)) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap == null) {
            return@rememberLauncherForActivityResult
        }

        val fileName = "flower_image_${System.currentTimeMillis()}.jpg"
        val savedImagePath = saveBitmapToInternalStorage(context, bitmap, fileName)

        if (savedImagePath != null) {
            imageUri = savedImagePath
        } else {
            errorMessage = context.getString(R.string.error_message_failed_save_image)
            showErrorDialog = true
        }
    }

    val selectImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                val fileName = "flower_image_${System.currentTimeMillis()}.jpg"
                val savedImagePath = saveImageToInternalStorage(context, uri, fileName)

                if (savedImagePath != null) {
                    imageUri = savedImagePath
                } else {
                    errorMessage = context.getString(R.string.error_message_failed_save_image)
                    showErrorDialog = true
                }
            } else {
                errorMessage = context.getString(R.string.error_message_failed_select_image)
                showErrorDialog = true
            }
        }
    )

    CheckPermissions(permissionManager)
    // Observe the permission status
    val isPermissionsGranted by permissionManager.permissionsGranted.observeAsState(initial = false)

    val isSaveEnabled = flowerName.isNotBlank() && imageUri != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Image placeholder
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
                        showImagePickerDialog = true
                    } else {
                        permissionManager.onPermissionsDenied()
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            val painter: Painter = if (imageUri != null) {
                rememberAsyncImagePainter(imageUri)
            } else {
                painterResource(id = R.drawable.ic_transparent)
            }

            Image(
                painter = painter,
                contentDescription = stringResource(R.string.desc_flower_image),
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            // Default if no image chosen
            if (imageUri == null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Icon(
                        imageVector = Icons.Filled.CameraAlt,
                        contentDescription = stringResource(R.string.desc_add_image),
                        tint = Color.Gray,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.add_flower_image),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        if (showImagePickerDialog) {
            ImagePickerDialog(
                onDismiss = { showImagePickerDialog = false },
                onCameraClick = {
                    cameraLauncher.launch()
                    showImagePickerDialog = false
                },
                onGalleryClick = {
                    selectImageLauncher.launch("image/*")
                    showImagePickerDialog = false
                }
            )
        }

        // Input fields for flower details
        CustomOutlinedTextField(
            value = flowerName,
            onValueChange = { flowerName = it },
            labelText = stringResource(R.string.flower_name_label)
        )

        CustomOutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            labelText = stringResource(R.string.flower_notes_label),
            isEnabled = isSaveEnabled
        )

        DatePickerButton(
            dateValue = lastWateredDate,
            onDateSelected = { lastWateredDate = it },
            label = stringResource(R.string.flower_last_watered_label),
            isEnabled = isSaveEnabled
        )

        CustomOutlinedTextField(
            value = nextWatering,
            onValueChange = { nextWatering = it },
            labelText = stringResource(R.string.flower_next_watered_label),
            isEnabled = isSaveEnabled,
            isNumeric = true
        )

        DatePickerButton(
            dateValue = lastFertilizedDate,
            onDateSelected = { lastFertilizedDate = it },
            label = stringResource(R.string.flower_last_fertilized_label),
            isEnabled = isSaveEnabled
        )

        CustomOutlinedTextField(
            value = nextFertilizing,
            onValueChange = { nextFertilizing = it },
            labelText = stringResource(R.string.flower_next_fertilized_label),
            isEnabled = isSaveEnabled,
            isNumeric = true
        )

        Button(
            onClick = {
                val newFlower = Flower(
                    name = flowerName,
                    imageUri = imageUri,
                    notes = notes,
                    lastWatered = lastWateredDate.takeIf { it.isNotEmpty() }?.stringToDate(),
                    waterInDays = nextWatering.toIntOrNull(),
                    lastFertilized = lastFertilizedDate.takeIf { it.isNotEmpty() }?.stringToDate(),
                    fertilizeInDays = nextFertilizing.toIntOrNull()
                )

                flowerViewModel.viewModelScope.launch {
                    val saveSuccessful = flowerViewModel.addFlower(newFlower)
                    if (saveSuccessful) {
                        onFlowerSaved()
                    } else {
                        errorMessage = context.getString(R.string.error_message_failed_save_flower)
                        showErrorDialog = true
                    }
                }
            },
            enabled = isSaveEnabled
        ) {
            Text(text = stringResource(R.string.save_button))
        }

        Spacer(modifier = Modifier.height(8.dp))

        ErrorDialog(
            showDialog = showErrorDialog,
            errorMessage = errorMessage,
            onDismiss = { showErrorDialog = false }
        )
    }
}