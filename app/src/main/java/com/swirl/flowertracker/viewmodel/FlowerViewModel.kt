package com.swirl.flowertracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.swirl.flowertracker.data.model.Flower
import com.swirl.flowertracker.data.repository.FlowerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FlowerViewModel @Inject constructor(
    private val repository: FlowerRepository
) : ViewModel() {

    val allFlowers: LiveData<List<Flower>> = repository.getAllFlowers().asLiveData()

    suspend fun addFlower(flower: Flower): Boolean {
        return try {
            repository.insert(flower)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteFlower(flower: Flower): Boolean {
        return try {
            repository.delete(flower)
            true
        } catch (e: Exception) {
            false
        }
    }
}
