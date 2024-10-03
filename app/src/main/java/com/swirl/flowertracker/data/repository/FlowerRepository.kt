package com.swirl.flowertracker.data.repository

import com.swirl.flowertracker.data.local.dao.FlowerDao
import com.swirl.flowertracker.data.model.Flower
import kotlinx.coroutines.flow.Flow

class FlowerRepository(private val flowerDao: FlowerDao) {

    fun getAllFlowers(): Flow<List<Flower>> {
        return flowerDao.getAllFlowers()
    }

    suspend fun insert(flower: Flower) {
        flowerDao.insert(flower)
    }

    suspend fun delete(flower: Flower) {
        flowerDao.delete(flower)
    }

    suspend fun updateFlower(flower: Flower) {
        flowerDao.updateFlower(flower)
    }

    fun getFlowerById(flowerId: Int): Flow<Flower?> {
        return flowerDao.getFlowerById(flowerId)
    }
}
