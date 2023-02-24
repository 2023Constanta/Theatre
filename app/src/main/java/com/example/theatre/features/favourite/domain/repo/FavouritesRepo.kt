package com.example.theatre.features.favourite.domain.repo

import com.example.theatre.core.domain.model.common.performance.Performance

interface FavouritesRepo {
    suspend fun getById(id: Int): Performance
    suspend fun getAll(): List<Performance>
    suspend fun delete(fav: Performance)
    suspend fun insert(fav: Performance)
}