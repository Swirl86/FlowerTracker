package com.swirl.flowertracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.swirl.flowertracker.data.Converters
import com.swirl.flowertracker.data.local.dao.FlowerDao
import com.swirl.flowertracker.data.model.Flower

@Database(entities = [Flower::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class FlowerDatabase : RoomDatabase() {
    abstract fun flowerDao(): FlowerDao
}