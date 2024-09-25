package com.swirl.flowertracker.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.swirl.flowertracker.R

@Composable
fun AddFlowerScreen(onBackClick: () -> Unit) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var flowerName by remember { mutableStateOf(TextFieldValue("")) }
    var notes by remember { mutableStateOf(TextFieldValue("")) }
    var lastWateredDate by remember { mutableStateOf(TextFieldValue("")) }
    var nextWateredDate by remember { mutableStateOf(TextFieldValue("")) }
    var lastFertilizedDate by remember { mutableStateOf(TextFieldValue("")) }
    var nextFertilizedDate by remember { mutableStateOf(TextFieldValue("")) }
    var permissionsGranted by remember { mutableStateOf(false) }
    var showPermissionDialog by remember { mutableStateOf(false) }
    var showImagePickerDialog by remember { mutableStateOf(false) }

    // Launchers for camera and image selection
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        // TODO Handle camera image result
        if (bitmap != null) {
            // TODO Convert bitmap to a usable format, such as Uri if needed
            // imageUri = conversionFunction(bitmap)
        }
    }

    val selectImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // TODO Handle image result
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
        PermissionDialog (
            onPermissionClose = {
                showPermissionDialog = false
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Image placeholder
        Box(
            modifier = Modifier
                .size(128.dp)
                .padding(bottom = 16.dp)
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
                rememberAsyncImagePainter(imageUri) // Load image from URI
            } else {
                painterResource(id = R.drawable.placeholder) // Placeholder image
            }

            Image(
                painter = painter,
                contentDescription = "Flower Image",
                modifier = Modifier.fillMaxSize()
            )

            Text("Tap to add image", fontSize = 14.sp)
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
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = lastWateredDate,
            onValueChange = { lastWateredDate = it },
            label = { Text("Last Watered Date") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = nextWateredDate,
            onValueChange = { nextWateredDate = it },
            label = { Text("Next Watered Date") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = lastFertilizedDate,
            onValueChange = { lastFertilizedDate = it },
            label = { Text("Last Fertilized Date") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = nextFertilizedDate,
            onValueChange = { nextFertilizedDate = it },
            label = { Text("Next Fertilized Date") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // TODO: Handle saving the flower object
        }) {
            Text(text = "Save Flower")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onBackClick) {
            Text(text = "Back")
        }
    }
}