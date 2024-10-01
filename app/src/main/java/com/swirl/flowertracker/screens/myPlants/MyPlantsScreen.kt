package com.swirl.flowertracker.screens.myPlants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.swirl.flowertracker.R
import com.swirl.flowertracker.permissions.CheckPermissions
import com.swirl.flowertracker.permissions.PermissionDialog
import com.swirl.flowertracker.screens.myPlants.common.FlowerItem
import com.swirl.flowertracker.utils.openAppSettings
import com.swirl.flowertracker.viewmodel.FlowerViewModel

@Composable
fun MyPlantsScreen(
    flowerViewModel: FlowerViewModel,
    onAddFlowerClick: () -> Unit
) {
    val context = LocalContext.current
    val flowers by flowerViewModel.allFlowers.observeAsState(emptyList())
    var permissionsGranted by remember { mutableStateOf(false) }
    var showPermissionDialog by remember { mutableStateOf(false) }

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

    if (showPermissionDialog) {
        PermissionDialog(
            onPermissionClose = {
                showPermissionDialog = false
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        if (flowers.isEmpty()) {
            EmptyScreen(onAddFlowerClick = onAddFlowerClick)
        } else if (!permissionsGranted) {
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
                        FlowerItem(flower = flowers[index])
                    }
                }
                Button(onClick = onAddFlowerClick, modifier = Modifier.fillMaxWidth()) {
                    Text(text = stringResource(R.string.add_new_flower))
                }
            }
        }
    }
}