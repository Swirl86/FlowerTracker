package com.swirl.flowertracker.screens.common

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.swirl.flowertracker.R
import com.swirl.flowertracker.screens.addFlower.ImagePickerDialog
import com.swirl.flowertracker.utils.saveBitmapToInternalStorage
import com.swirl.flowertracker.utils.saveImageToInternalStorage

@Composable
fun customImagePicker(
    context: Context,
    onImageUriSelected: (Uri) -> Unit,
    onError: (String) -> Unit
): Pair<Boolean, () -> Unit> {  // Return a pair
    var showImagePickerDialog by remember { mutableStateOf(false) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            val fileName = "flower_image_${System.currentTimeMillis()}.jpg"
            val savedImagePath = saveBitmapToInternalStorage(context, bitmap, fileName)

            if (savedImagePath != null) {
                onImageUriSelected(Uri.parse(savedImagePath))
            } else {
                onError(context.getString(R.string.error_message_failed_save_image))
            }
        } else {
            onError(context.getString(R.string.error_message_failed_save_image))
        }
    }

    val selectImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                val fileName = "flower_image_${System.currentTimeMillis()}.jpg"
                val savedImagePath = saveImageToInternalStorage(context, uri, fileName)

                if (savedImagePath != null) {
                    onImageUriSelected(Uri.parse(savedImagePath))
                } else {
                    onError(context.getString(R.string.error_message_failed_save_image))
                }
            } else {
                onError(context.getString(R.string.error_message_failed_select_image))
            }
        }
    )

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

    // Function to open the image picker dialog
    val openImagePicker: () -> Unit = {
        showImagePickerDialog = true
    }

    // Return both the dialog state and the function
    return Pair(showImagePickerDialog, openImagePicker)
}