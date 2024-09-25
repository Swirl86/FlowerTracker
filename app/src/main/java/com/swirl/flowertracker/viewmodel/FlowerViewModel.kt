package com.swirl.flowertracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swirl.flowertracker.data.Flower
import com.swirl.flowertracker.data.FlowerRepository
import kotlinx.coroutines.launch

class FlowerViewModel(private val repository: FlowerRepository) : ViewModel() {
    val allFlowers = repository.allFlowers

    fun addFlower(flower: Flower) {
        viewModelScope.launch {
            repository.insert(flower)
        }
    }

    fun deleteFlower(flower: Flower) {
        viewModelScope.launch {
            repository.delete(flower)
        }
    }
}
