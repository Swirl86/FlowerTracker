package com.swirl.flowertracker.di

import android.content.Context
import androidx.room.Room
import com.swirl.flowertracker.data.local.FlowerDatabase
import com.swirl.flowertracker.data.local.dao.FlowerDao
import com.swirl.flowertracker.data.repository.FlowerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFlowerDatabase(@ApplicationContext context: Context): FlowerDatabase {
        return Room.databaseBuilder(
            context,
            FlowerDatabase::class.java,
            "flower_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFlowerDao(db: FlowerDatabase) = db.flowerDao()

    @Provides
    @Singleton
    fun provideFlowerRepository(flowerDao: FlowerDao) = FlowerRepository(flowerDao = flowerDao)
}