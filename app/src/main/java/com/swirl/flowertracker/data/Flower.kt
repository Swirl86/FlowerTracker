package com.swirl.flowertracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "flower_table")
data class Flower(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val imageUri: String,
    val notes: String?,
    val lastWatered: Date?,
    val waterAlarmDate: Date?,
    val lastFertilized: Date?,
    val fertilizeAlarmDate: Date?
)
