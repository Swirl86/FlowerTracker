package com.swirl.flowertracker.data.repository

import androidx.lifecycle.LiveData
import com.swirl.flowertracker.data.local.dao.FlowerDao
import com.swirl.flowertracker.data.model.Flower

class FlowerRepository(private val flowerDao: FlowerDao) {

    val allFlowers: LiveData<List<Flower>> = flowerDao.getAllFlowers()

    suspend fun insert(flower: Flower) {
        flowerDao.insert(flower)
    }

    suspend fun delete(flower: Flower) {
        flowerDao.delete(flower)
    }
}
