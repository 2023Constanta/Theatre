package com.example.theatre.features.favourite.presentation.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.theatre.core.domain.model.common.performance.Performance
import com.example.theatre.core.presentation.ext.viewModelCall
import com.example.theatre.core.presentation.model.ContentResultState
import com.example.theatre.features.favourite.domain.usecase.FavouritesUseCase

class FavouriteViewModel(
    private val useCase: FavouritesUseCase
) : ViewModel() {

    private var _favouriteContent = MutableLiveData<ContentResultState>()
    val favouriteContent get() = _favouriteContent

    fun getFavouritesById(id: Int) = viewModelCall(
        call = { useCase.getById(id) },
        contentResultState = _favouriteContent
    )

    fun deleteFav(performance: Performance) =
        viewModelCall(call = { useCase.delete(performance) })


    fun saveFav(performance: Performance) =
        viewModelCall(call = { useCase.insert(performance) })
}