package com.swirl.flowertracker.screens.myPlants

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.swirl.flowertracker.data.model.Flower
import com.swirl.flowertracker.screens.common.FlowerImage

@Composable
fun FlowerItem(flower: Flower) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FlowerImage(
            flower.imageUri,
            modifier = Modifier
            .size(64.dp)
            .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(text = flower.name, style = MaterialTheme.typography.titleMedium)
            Text(text = "Last watered: ${flower.lastWatered ?: "N/A"}")
        }
    }
}
