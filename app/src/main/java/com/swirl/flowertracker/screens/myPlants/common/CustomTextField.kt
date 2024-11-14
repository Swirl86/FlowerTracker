package com.swirl.flowertracker.screens.myPlants.common

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.swirl.flowertracker.utils.clearFocusOnKeyboardDismiss
import com.swirl.flowertracker.utils.customTextFieldColors

@Composable
fun CustomTextField(
    label: String,
    value: String,
    isNumeric: Boolean = false,
    onValueChange: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label + if (!isFocused && value.isEmpty()) " N/A" else "",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = if (isFocused) MaterialTheme.colorScheme.primary
                    else if (value.isEmpty()) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    else MaterialTheme.colorScheme.primary
                )
            )
        },
        modifier = Modifier
            .clearFocusOnKeyboardDismiss()
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(onPress = {
                    interactionSource.tryEmit(PressInteraction.Press(it))
                })
            },
        keyboardOptions = if (isNumeric) {
            KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        } else {
            KeyboardOptions.Default
        },
        shape = RoundedCornerShape(8.dp),
        interactionSource = interactionSource,
        colors = customTextFieldColors()
    )
    Spacer(modifier = Modifier.height(8.dp))
}