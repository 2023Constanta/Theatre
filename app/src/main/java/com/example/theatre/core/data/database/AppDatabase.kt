package com.example.theatre.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.theatre.features.favourite.data.room.StringListConverter
import com.example.theatre.features.favourite.data.room.dao.FavouriteDao
import com.example.theatre.features.favourite.data.room.entity.FavouritePerformanceEntity

@Database(entities = [FavouritePerformanceEntity::class], version = 1, exportSchema = true)
@TypeConverters(StringListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getFavouriteDao(): FavouriteDao
}