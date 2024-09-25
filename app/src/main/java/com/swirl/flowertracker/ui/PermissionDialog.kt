package com.swirl.flowertracker.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.swirl.flowertracker.utils.openAppSettings

@Composable
fun PermissionDialog(onPermissionClose: (Boolean) -> Unit) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = { onPermissionClose(false) },
        title = { Text("Permission Required") },
        text = { Text("This app needs permission to access your camera and storage to manage images. Please grant permission in the settings.") },
        confirmButton = {
            TextButton(onClick = {
                openAppSettings(context)
                onPermissionClose(false) // Dismiss the dialog after opening settings
            }) {
                Text("Open Settings")
            }
        },
        dismissButton = {
            TextButton(onClick = { onPermissionClose(false) }) {
                Text("Cancel")
            }
        }
    )
}