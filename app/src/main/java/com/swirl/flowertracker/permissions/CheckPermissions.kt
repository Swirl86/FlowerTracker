package com.swirl.flowertracker.permissions

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CheckPermissions(
    onPermissionsGranted: () -> Unit,
    onPermissionsDenied: () -> Unit
) {
    // TODO check logic
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val storagePermissionState = rememberPermissionState(Manifest.permission.READ_EXTERNAL_STORAGE)

    // Unified result handler for both permissions
    val permissionResultHandler: (Boolean) -> Unit = { isGranted ->
        if (isGranted && cameraPermissionState.status.isGranted && storagePermissionState.status.isGranted) {
            onPermissionsGranted()
        } else {
            onPermissionsDenied()
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
    LaunchedEffect(cameraPermissionState.status, storagePermissionState.status) {
        when {
            //Both permissions are granted
            cameraPermissionState.status.isGranted && storagePermissionState.status.isGranted -> {
                onPermissionsGranted()
            }

            // Camera or storage has been permanently denied
            (!cameraPermissionState.status.isGranted && !cameraPermissionState.status.shouldShowRationale) ||
                    (!storagePermissionState.status.isGranted && !storagePermissionState.status.shouldShowRationale) -> {
                onPermissionsDenied()
            }

            // Camera / storage rights are not granted but can be requested again
            !cameraPermissionState.status.isGranted -> {
                cameraPermissionResultLauncher.launch(Manifest.permission.CAMERA)
            }

            !storagePermissionState.status.isGranted -> {
                storagePermissionResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }
}