package com.swirl.flowertracker.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FlowerDao {
    @Query("SELECT * FROM flower_table")
    fun getAllFlowers(): Flow<List<Flower>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(flower: Flower)

    @Delete
    suspend fun delete(flower: Flower)
}
