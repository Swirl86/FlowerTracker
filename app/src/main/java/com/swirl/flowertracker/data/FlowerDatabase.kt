package com.swirl.flowertracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Flower::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class FlowerDatabase : RoomDatabase() {
    abstract fun flowerDao(): FlowerDao

    companion object {
        @Volatile
        private var INSTANCE: FlowerDatabase? = null

        fun getDatabase(context: Context): FlowerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FlowerDatabase::class.java,
                    "flower_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}
