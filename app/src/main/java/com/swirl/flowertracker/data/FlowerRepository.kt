package com.swirl.flowertracker.data

import kotlinx.coroutines.flow.Flow

class FlowerRepository(private val flowerDao: FlowerDao) {

    val allFlowers: Flow<List<Flower>> = flowerDao.getAllFlowers()

    suspend fun insert(flower: Flower) {
        flowerDao.insert(flower)
    }

    suspend fun delete(flower: Flower) {
        flowerDao.delete(flower)
    }
}
