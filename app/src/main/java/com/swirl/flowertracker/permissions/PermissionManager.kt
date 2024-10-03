package com.swirl.flowertracker.permissions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PermissionManager @Inject constructor() : ViewModel() {
    private var _permissionsGranted = MutableLiveData(false)
    val permissionsGranted: LiveData<Boolean> get() = _permissionsGranted

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

