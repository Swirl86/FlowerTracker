package com.swirl.flowertracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swirl.flowertracker.data.model.Flower
import com.swirl.flowertracker.data.repository.FlowerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FlowerViewModel @Inject constructor(
    private val repository: FlowerRepository
) : ViewModel() {

    private val _flowerId = MutableStateFlow<Int?>(null)
    private val _flowers = MutableStateFlow<List<Flower>>(emptyList())
    val flowers: StateFlow<List<Flower>> = _flowers

    init {
        viewModelScope.launch {
            _flowers.value = withContext(Dispatchers.IO) {
                repository.getAllFlowers()
            }
        }
    }

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

    suspend fun updateFlower(flower: Flower): Boolean {
        return try {
            repository.updateFlower(flower)
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
