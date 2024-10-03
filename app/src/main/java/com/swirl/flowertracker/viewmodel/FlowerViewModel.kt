package com.swirl.flowertracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.swirl.flowertracker.data.model.Flower
import com.swirl.flowertracker.data.repository.FlowerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FlowerViewModel @Inject constructor(
    private val repository: FlowerRepository
) : ViewModel() {

    val allFlowers: LiveData<List<Flower>> = repository.getAllFlowers().asLiveData()

    private val _flowerId = MutableStateFlow<Int?>(null)

    val flower: StateFlow<Flower?> = _flowerId
        .filterNotNull() // Ignore null values
        .flatMapLatest { id ->
            repository.getFlowerById(id)
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)


    fun setFlowerId(flowerId: Int?) {
        _flowerId.value = flowerId
    }

    suspend fun addFlower(flower: Flower): Boolean {
        return try {
            repository.insert(flower)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun updateFlower(flower: Flower) {
        repository.updateFlower(flower)
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
