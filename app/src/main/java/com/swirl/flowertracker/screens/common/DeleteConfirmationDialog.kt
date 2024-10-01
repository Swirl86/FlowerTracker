package com.swirl.flowertracker.screens.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.swirl.flowertracker.R


@Composable
fun DeleteConfirmationDialog(
    showDialog: Boolean,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(stringResource(R.string.delete_flower_title)) },
            text = { Text(stringResource(R.string.delete_flower_text)) },
            confirmButton = {
                TextButton(onClick = {
                    onDelete()
                }) {
                    Text(stringResource(R.string.yes_button))
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(stringResource(R.string.no_button))
                }
            }
        )
    }
}

