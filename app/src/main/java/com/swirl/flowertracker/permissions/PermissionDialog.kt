package com.swirl.flowertracker.permissions

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.swirl.flowertracker.R
import com.swirl.flowertracker.utils.openAppSettings

@Composable
fun PermissionDialog(onPermissionClose: (Boolean) -> Unit) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = { onPermissionClose(false) },
        title = { Text(stringResource(R.string.permission_title)) },
        text = { Text(stringResource(R.string.permission_text)) },
        confirmButton = {
            TextButton(onClick = {
                openAppSettings(context)
                onPermissionClose(false) // Dismiss the dialog after opening settings
            }) {
                Text(stringResource(R.string.permission_settings))
            }
        },
        dismissButton = {
            TextButton(onClick = { onPermissionClose(false) }) {
                Text(stringResource(R.string.cancel_button))
            }
        }
    )
}