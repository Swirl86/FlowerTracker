package com.swirl.flowertracker.screens.myPlants.common

import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Water
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swirl.flowertracker.R
import com.swirl.flowertracker.data.model.Flower
import com.swirl.flowertracker.screens.common.DeleteConfirmationDialog
import com.swirl.flowertracker.screens.common.FlowerImage
import com.swirl.flowertracker.utils.localDateToRemainingDays

@Composable
fun FlowerItem(flower: Flower, onClick: () -> Unit, onDelete: () -> Unit) {
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        )
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                FlowerImage(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(2.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                    imageUri = flower.imageUri,
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp)
                ) {
                    Text(
                        text = flower.name,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.primary
                    )

                    flower.waterInDays?.localDateToRemainingDays()?.let { daysUntilWater ->
                        val daysText = stringResource(id = R.string.flower_item_water, daysUntilWater)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Water,
                                contentDescription = null,
                                tint = Color.Blue,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = daysText,
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                                color = Color.Blue
                            )
                        }
                    }

                    flower.fertilizeInDays?.localDateToRemainingDays()?.let { daysUntilFertilize ->
                        val daysText = stringResource(id = R.string.flower_item_fertilize, daysUntilFertilize)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Spa,
                                contentDescription = null,
                                tint = colorResource(R.color.darkOrange),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = daysText,
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                                color = colorResource(R.color.darkOrange)
                            )
                        }
                    }

                    val notesDisplay = flower.notes?.take(40) + if ((flower.notes?.length ?: 0) > 40) "..." else ""
                    Text(
                        text = notesDisplay,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            IconButton(
                onClick = { showDeleteConfirmation = true },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(28.dp)
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.desc_delete_flower),
                    tint = MaterialTheme.colorScheme.error
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
