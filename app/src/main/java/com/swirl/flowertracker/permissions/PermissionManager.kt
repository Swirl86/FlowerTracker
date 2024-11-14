package com.swirl.flowertracker.permissions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PermissionManager @Inject constructor() : ViewModel() {
    private val _permissionsGranted = MutableStateFlow(false)
    val permissionsGranted: StateFlow<Boolean> get() = _permissionsGranted

    var showPermissionDialog by mutableStateOf(false)

    fun onPermissionsGranted() {
        _permissionsGranted.value = true
        showPermissionDialog = false
    }

    fun onPermissionsDenied() {
        _permissionsGranted.value = false
        showPermissionDialog = true
    }

    fun onPermissionClose() {
        showPermissionDialog = false
    }
}

