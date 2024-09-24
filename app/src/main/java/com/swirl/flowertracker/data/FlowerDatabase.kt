package com.swirl.flowertracker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Flower::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class FlowerDatabase : RoomDatabase() {
    abstract fun flowerDao(): FlowerDao
}
