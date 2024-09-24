package com.swirl.flowertracker.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.swirl.flowertracker.data.FlowerDatabase
import com.swirl.flowertracker.data.FlowerRepository

class FlowerViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = (app as FlowerTrackerApplication).repository
        if (modelClass.isAssignableFrom(FlowerViewModel::class.java)) {
            return FlowerViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

