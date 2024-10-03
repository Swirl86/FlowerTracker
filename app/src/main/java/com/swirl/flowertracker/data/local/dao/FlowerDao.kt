package com.swirl.flowertracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.swirl.flowertracker.data.model.Flower
import kotlinx.coroutines.flow.Flow

@Dao
interface FlowerDao {

    @Query("SELECT * FROM flower_table")
    fun getAllFlowers(): Flow<List<Flower>>

    @Query("SELECT * FROM flower_table WHERE id = :flowerId LIMIT 1")
    fun getFlowerById(flowerId: Int): Flow<Flower?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(flower: Flower)

    @Delete
    suspend fun delete(flower: Flower)

    @Update
    suspend fun updateFlower(flower: Flower)
}
