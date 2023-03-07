package com.example.theatre.features.spectacles.presentation.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.theatre.core.presentation.ext.viewModelCall
import com.example.theatre.core.presentation.model.ContentResultState
import com.example.theatre.features.spectacles.domain.usecases.GetPerformanceUseCase

/**
 * View model для хранения списка событий
 *
 * @author Tamerlan Mamukhov
 */

class SpectaclesListViewModel(
    private val getPerformanceUseCase: GetPerformanceUseCase
) : ViewModel() {
    private val _spectacleLoaded = MutableLiveData<ContentResultState>(ContentResultState.Loading)
    val spectacleLoaded get() = _spectacleLoaded

    private var _spectacleSearched = MutableLiveData<ContentResultState>(ContentResultState.Loading)
    val spectacleSearched get() = _spectacleSearched

    fun loadPerformances() {
        if (_spectacleLoaded.value is ContentResultState.Loading) {
            getPerformances()
        }
    }

    fun getPerformances() = viewModelCall(
        call = { getPerformanceUseCase.getPerformance() },
        contentResultState = _spectacleLoaded
    )

    fun getPerformancesByQuery(query: String) {
        val fullQuery = SearchQueryConstructor.create(query)
        viewModelCall(
            call = { getPerformanceUseCase.getPerformancesByQuery(fullQuery) },
            contentResultState = _spectacleSearched
        )
    }

}