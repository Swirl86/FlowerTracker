package com.swirl.flowertracker.permissions

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

    // Create launcher for requesting camera permission
    val cameraPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            Log.i("TAG-1 cameraPermissionResultLauncher", isGranted.toString())
            if (isGranted) {
                onPermissionsGranted()
            } else {
                onPermissionsDenied(true)
            }
        }
    )

    // Create launcher for requesting storage permission
    val storagePermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            Log.i("TAG-1 storagePermissionResultLauncher", isGranted.toString())
            if (isGranted) {
                onPermissionsGranted()
            } else {
                onPermissionsDenied(true)
            }
        }
    )

    // Initial permission check
    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionResultLauncher.launch(Manifest.permission.CAMERA)
        }

        if (!storagePermissionState.status.isGranted) {
            storagePermissionResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }
}