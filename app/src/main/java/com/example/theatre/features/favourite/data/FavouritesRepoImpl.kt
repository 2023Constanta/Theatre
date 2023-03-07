package com.example.theatre.features.favourite.data

import com.example.theatre.core.domain.model.common.performance.Performance
import com.example.theatre.features.favourite.data.room.dao.FavouriteDao
import com.example.theatre.features.favourite.domain.repo.FavouritesRepo
import com.example.theatre.features.favourite.toEntity
import com.example.theatre.features.favourite.toPerformance

class FavouritesRepoImpl(
    private val dao: FavouriteDao
) : FavouritesRepo {
    override suspend fun getById(id: Int): Performance =
        dao.getBy(id).toPerformance()

    override suspend fun getAll(): List<Performance> =
        dao.getAll().map { it.toPerformance() }

    override suspend fun delete(fav: Performance) =
        dao.delete(fav.toEntity())

    override suspend fun insert(fav: Performance) =
        dao.save(fav.toEntity())


}