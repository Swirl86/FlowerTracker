package com.swirl.flowertracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "flower_table")
data class Flower(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val imageUri: String?,
    val notes: String?,
    val lastWatered: LocalDate?,
    val waterAlarmDate: LocalDate?,
    val lastFertilized: LocalDate?,
    val fertilizeAlarmDate: LocalDate?
)