package com.swirl.flowertracker.screens.myPlants.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swirl.flowertracker.R
import com.swirl.flowertracker.data.model.Flower
import com.swirl.flowertracker.screens.common.DeleteConfirmationDialog
import com.swirl.flowertracker.screens.common.FlowerImage

@Composable
fun FlowerItem(flower: Flower, onClick: () -> Unit, onDelete: () -> Unit) {

    var showDeleteConfirmation by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                FlowerImage(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    imageUri = flower.imageUri,
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = flower.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Display watering and fertilizing alarms if set
                    flower.waterInDays?.let { //TODO impl design
                        val daysUntilWater = stringResource(
                            id = R.string.flower_item_water,
                            it
                        )

                        Text(
                            text = daysUntilWater,
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                            color = Color.Blue
                        )
                    }

                    flower.fertilizeInDays?.let { //TODO impl design
                        val daysUntilFertilize = stringResource(
                            id = R.string.flower_item_fertilize,
                            it
                        )
                        Text(
                            text = daysUntilFertilize,
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                            color = Color.Green
                        )
                    }

                    val notesDisplay = flower.notes?.take(30) + if ((flower.notes?.length ?: 0) > 30) "..." else ""
                    Text(
                        text = notesDisplay,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            IconButton(
                onClick = { showDeleteConfirmation = true },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.desc_delete_flower),
                    tint = Color.Red
                )
            }

            DeleteConfirmationDialog(
                showDialog = showDeleteConfirmation,
                onDelete = {
                    showDeleteConfirmation = false
                    onDelete()
                },
                onDismiss = { showDeleteConfirmation = false }
            )
        }
    }
}