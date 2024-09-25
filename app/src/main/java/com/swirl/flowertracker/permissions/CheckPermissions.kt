package com.swirl.flowertracker.permissions

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CheckPermissions(
    onPermissionsGranted: () -> Unit,
    onPermissionsDenied: (Boolean) -> Unit // Pass whether to show settings
) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val storagePermissionState = rememberPermissionState(Manifest.permission.READ_EXTERNAL_STORAGE)
    var askedForPermission by remember { mutableStateOf(false) }

    // Unified result handler for both permissions
    val permissionResultHandler: (Boolean) -> Unit = { isGranted ->
        if (isGranted) {
            if (cameraPermissionState.status.isGranted && storagePermissionState.status.isGranted) {
                onPermissionsGranted()
            }
        } else {
            onPermissionsDenied(true)
        }
    }

    val cameraPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            permissionResultHandler(isGranted)
        }
    )

    val storagePermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            permissionResultHandler(isGranted)
        }
    )

    // Initial permission check
    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionResultLauncher.launch(Manifest.permission.CAMERA)
        } else if (!storagePermissionState.status.isGranted) {
            storagePermissionResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            onPermissionsGranted()
        }
    }
}