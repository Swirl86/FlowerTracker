package com.swirl.flowertracker.data

import androidx.lifecycle.LiveData

class FlowerRepository(private val flowerDao: FlowerDao) {

    val allFlowers: LiveData<List<Flower>> = flowerDao.getAllFlowers()

    suspend fun insert(flower: Flower) {
        flowerDao.insert(flower)
    }

    suspend fun delete(flower: Flower) {
        flowerDao.delete(flower)
    }
}
