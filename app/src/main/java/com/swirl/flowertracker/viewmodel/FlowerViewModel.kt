package com.swirl.flowertracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swirl.flowertracker.data.model.Flower
import com.swirl.flowertracker.data.repository.FlowerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlowerViewModel @Inject constructor(
    private val repository: FlowerRepository
) : ViewModel() {
    val allFlowers = repository.allFlowers

    // TODO not done WIP
    suspend fun addFlower(flower: Flower) : Boolean {
        var saveSuccessful = false
        viewModelScope.launch {
            saveSuccessful = try {
                repository.insert(flower)
                true
            } catch (e: Exception) {
                false
            }
        }
        return saveSuccessful
    }

    fun deleteFlower(flower: Flower) {
        viewModelScope.launch {
            repository.delete(flower)
        }
    }
}
