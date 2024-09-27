package com.swirl.flowertracker.screens.addFlower

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.rememberAsyncImagePainter
import com.swirl.flowertracker.R
import com.swirl.flowertracker.data.model.Flower
import com.swirl.flowertracker.permissions.CheckPermissions
import com.swirl.flowertracker.permissions.PermissionDialog
import com.swirl.flowertracker.utils.stringToDate
import com.swirl.flowertracker.viewmodel.FlowerViewModel
import kotlinx.coroutines.launch

@Composable
fun AddFlowerScreen(
    flowerViewModel: FlowerViewModel = hiltViewModel(),
    onFlowerSaved: () -> Unit
) {
    var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var flowerName by rememberSaveable { mutableStateOf("") }
    var notes by rememberSaveable { mutableStateOf("") }
    var lastWateredDate by rememberSaveable { mutableStateOf("") }
    var nextWateredDate by rememberSaveable { mutableStateOf("") }
    var lastFertilizedDate by rememberSaveable { mutableStateOf("") }
    var nextFertilizedDate by rememberSaveable { mutableStateOf("") }

    var permissionsGranted by remember { mutableStateOf(false) }
    var showPermissionDialog by remember { mutableStateOf(false) }
    var showImagePickerDialog by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }

    // Launchers for camera and image selection
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        // TODO Handle camera image result
        if (bitmap != null) {
            // TODO Convert bitmap to a usable format, such as Uri if needed
            // imageUri = conversionFunction(bitmap)
        }
    }

    val selectImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { imageUri = it }
    }

    // Check permissions for camera and storage
    CheckPermissions(
        onPermissionsGranted = {
            permissionsGranted = true
            showPermissionDialog = false
        },
        onPermissionsDenied = {
            permissionsGranted = false
            showPermissionDialog = true
        }
    )

    // Listen for clicks to manage permissions
    if (showPermissionDialog) {
        PermissionDialog(
            onPermissionClose = {
                showPermissionDialog = false
            }
        )
    }

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
                    if (permissionsGranted) {
                        showImagePickerDialog = true
                    } else {
                        showPermissionDialog = true
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
                contentDescription = "Flower Image",
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
                        contentDescription = "Add image",
                        tint = Color.Gray,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tap to add image",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        // Show the ImagePickerDialog when needed
        ImagePickerDialog(
            showDialog = showImagePickerDialog,
            onDismiss = { showImagePickerDialog = false },
            onCameraClick = {
                cameraLauncher.launch(null)
                showImagePickerDialog = false
            },
            onGalleryClick = {
                selectImageLauncher.launch("image/*")
                showImagePickerDialog = false
            }
        )

        // Input fields for flower details
        OutlinedTextField(
            value = flowerName,
            onValueChange = { flowerName = it },
            label = { Text("Flower Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Notes") },
            modifier = Modifier.fillMaxWidth(),
            enabled = isSaveEnabled
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = lastWateredDate,
            onValueChange = { lastWateredDate = it },
            label = { Text("Last Watered Date") },
            modifier = Modifier.fillMaxWidth(),
            enabled = isSaveEnabled
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = nextWateredDate,
            onValueChange = { nextWateredDate = it },
            label = { Text("Next Watered Date") },
            modifier = Modifier.fillMaxWidth(),
            enabled = isSaveEnabled
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = lastFertilizedDate,
            onValueChange = { lastFertilizedDate = it },
            label = { Text("Last Fertilized Date") },
            modifier = Modifier.fillMaxWidth(),
            enabled = isSaveEnabled
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = nextFertilizedDate,
            onValueChange = { nextFertilizedDate = it },
            label = { Text("Next Fertilized Date") },
            modifier = Modifier.fillMaxWidth(),
            enabled = isSaveEnabled
        )
        Spacer(modifier = Modifier.height(16.dp))

        // TODO not done WIP
        Button(
            onClick = {
                val newFlower = Flower(
                    name = flowerName,
                    imageUri = imageUri?.toString() ?: "",
                    notes = notes,
                    lastWatered = lastWateredDate.takeIf { it.isNotEmpty() }?.stringToDate(),
                    waterAlarmDate = nextWateredDate.takeIf { it.isNotEmpty() }?.stringToDate(),
                    lastFertilized = lastFertilizedDate.takeIf { it.isNotEmpty() }?.stringToDate(),
                    fertilizeAlarmDate = nextFertilizedDate.takeIf { it.isNotEmpty() }?.stringToDate()
                )

                flowerViewModel.viewModelScope.launch {
                    val saveSuccessful = flowerViewModel.addFlower(newFlower)
                    if (saveSuccessful) {
                        onFlowerSaved()
                    } else {
                        errorMessage = "Failed to save flower. Please try again."
                        showErrorDialog = true
                    }
                }
            },
            enabled = isSaveEnabled
        ) {
            Text(text = "Save Flower")
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (showErrorDialog) {
            AlertDialog(
                onDismissRequest = { showErrorDialog = false },
                title = { Text(text = "Error") },
                text = { Text(text = errorMessage) },
                confirmButton = {
                    Button(onClick = { showErrorDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}