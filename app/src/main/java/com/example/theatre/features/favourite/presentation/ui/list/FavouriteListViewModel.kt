package com.example.theatre.features.favourite.presentation.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.theatre.core.presentation.ext.viewModelCall
import com.example.theatre.core.presentation.model.ContentResultState
import com.example.theatre.features.favourite.domain.usecase.FavouritesUseCase

class FavouriteListViewModel(
    private val useCase: FavouritesUseCase
) : ViewModel() {

    private var _favouriteContent = MutableLiveData<ContentResultState>()
    val favouriteContent get() = _favouriteContent

    fun getAll() = viewModelCall(
        call = { useCase.getAll() },
        contentResultState = _favouriteContent
    )

}