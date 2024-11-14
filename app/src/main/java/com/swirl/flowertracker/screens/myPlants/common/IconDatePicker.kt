package com.swirl.flowertracker.screens.myPlants.common

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.swirl.flowertracker.R
import com.swirl.flowertracker.utils.customTextFieldColors
import java.util.Calendar
import java.util.Locale

@Composable
fun IconDatePicker (
    label: String,
    dateValue: String,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        R.style.CustomDatePickerDialog,
        { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = String.format(Locale.getDefault(), "%02d %02d %04d", selectedDay, selectedMonth + 1, selectedYear)
            onDateSelected(formattedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    TextField(
        value = dateValue,
        onValueChange = {},
        label = {
            Text(
                text = label + if (dateValue.isEmpty()) " N/A" else "",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = if (dateValue.isEmpty()) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    else MaterialTheme.colorScheme.primary
                )
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = { datePickerDialog.show() }),
        shape = RoundedCornerShape(8.dp),
        readOnly = true,
        enabled = false,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = "Select Date",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        colors = customTextFieldColors(),
        textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface)
    )
}