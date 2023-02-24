package com.example.theatre.features.favourite.domain.usecase

import com.example.theatre.core.domain.model.ResultState
import com.example.theatre.core.domain.model.common.performance.Performance
import com.example.theatre.core.domain.model.safeCall
import com.example.theatre.features.favourite.domain.repo.FavouritesRepo

class FavouritesUseCase(
    private val dao: FavouritesRepo
) {

    suspend fun getById(id: Int): ResultState<Performance> = safeCall { dao.getById(id) }

    suspend fun getAll(): ResultState<List<Performance>> = safeCall { dao.getAll() }

    suspend fun delete(fav: Performance) = dao.delete(fav)

    suspend fun insert(fav: Performance) = dao.insert(fav)

}