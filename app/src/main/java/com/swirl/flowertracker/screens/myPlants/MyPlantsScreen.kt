package com.swirl.flowertracker.screens.myPlants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.swirl.flowertracker.R
import com.swirl.flowertracker.permissions.CheckPermissions
import com.swirl.flowertracker.permissions.PermissionManager
import com.swirl.flowertracker.screens.common.ErrorDialog
import com.swirl.flowertracker.screens.myPlants.common.FlowerItem
import com.swirl.flowertracker.utils.openAppSettings
import com.swirl.flowertracker.viewmodel.FlowerViewModel
import kotlinx.coroutines.launch

@Composable
fun MyPlantsScreen(
    onFlowerDetailClick: (Int) -> Unit,
    onAddFlowerClick: () -> Unit,
    flowerViewModel: FlowerViewModel = hiltViewModel(),
    permissionManager: PermissionManager = hiltViewModel()
) {
    val context = LocalContext.current
    val flowers by flowerViewModel.allFlowers.observeAsState(emptyList())

    var showErrorDialog by remember { mutableStateOf(false) }
    val errorMessage = stringResource(R.string.error_message_failed_delete_flower)

    CheckPermissions(permissionManager)
    // Observe the permission status
    val isPermissionsGranted by permissionManager.permissionsGranted.observeAsState(initial = false)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        if (flowers.isEmpty()) {
            EmptyScreen(onAddFlowerClick = onAddFlowerClick)
        } else if (!isPermissionsGranted) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.permission_title),
                        fontWeight = FontWeight.Bold,
                        color = Color.Red,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = stringResource(R.string.permission_text),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                    Button(onClick = {
                        openAppSettings(context)
                    }) {
                        Text(text = stringResource(R.string.permission_settings))
                    }
                }
            }
        } else {
            Column {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(flowers.size) { index ->
                        FlowerItem(
                            flower = flowers[index],
                            onClick = {
                                onFlowerDetailClick(flowers[index].id)
                            },
                            onDelete = {
                                flowerViewModel.viewModelScope.launch {
                                    val deleteFailed = !flowerViewModel.deleteFlower(flowers[index])
                                    showErrorDialog = deleteFailed
                                }
                            }
                        )
                    }
                }
            }
        }

        ErrorDialog(
            showDialog = showErrorDialog,
            errorMessage = errorMessage,
            onDismiss = { showErrorDialog = false }
        )
    }
}