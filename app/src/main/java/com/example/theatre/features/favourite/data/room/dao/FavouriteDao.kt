package com.example.theatre.features.favourite.data.room.dao

import androidx.room.*
import com.example.theatre.features.favourite.data.room.entity.FavouritePerformanceEntity

@Dao
interface FavouriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(fav: FavouritePerformanceEntity)

    @Query("SELECT * FROM favourites")
    fun getAll(): List<FavouritePerformanceEntity>

    @Query("SELECT * FROM favourites WHERE id = :id")
    suspend fun getBy(id: Int): FavouritePerformanceEntity

    @Delete()
    fun delete(fav: FavouritePerformanceEntity)
}