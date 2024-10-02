package com.swirl.flowertracker.screens.myPlants.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun CustomTextField(
    label: String,
    value: String,
    isNumeric: Boolean = false,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label + if (value.isEmpty()) " N/A" else "") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = if (isNumeric) {
            KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        } else {
            KeyboardOptions.Default
        }
    )
}