package com.swirl.flowertracker.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.swirl.flowertracker.data.model.Flower

@Dao
interface FlowerDao {

    @Query("SELECT * FROM flower_table")
    fun getAllFlowers(): LiveData<List<Flower>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(flower: Flower)

    @Delete
    suspend fun delete(flower: Flower)
}
