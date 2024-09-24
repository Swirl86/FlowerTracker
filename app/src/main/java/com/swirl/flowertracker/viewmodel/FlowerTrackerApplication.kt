package com.swirl.flowertracker.viewmodel

import android.app.Application
import androidx.room.Room
import com.swirl.flowertracker.data.FlowerDatabase
import com.swirl.flowertracker.data.FlowerRepository


class FlowerTrackerApplication : Application() {
    private val database by lazy {
        Room.databaseBuilder(
            this, FlowerDatabase::class.java, "flower_database"
        ).build()
    }

    val repository by lazy { FlowerRepository(database.flowerDao()) }
}
