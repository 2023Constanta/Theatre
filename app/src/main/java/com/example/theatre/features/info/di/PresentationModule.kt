package com.example.theatre.features.info.di

import com.example.theatre.features.info.presentation.ui.detail.person.PersonViewModel
import com.example.theatre.features.info.presentation.ui.detail.theatre.TheatreViewModel
import com.example.theatre.features.info.presentation.ui.list.person.PersonsListViewModel
import com.example.theatre.features.info.presentation.ui.list.theatre.TheatresListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val infoPresentationModule = module {
    viewModel { TheatresListViewModel(get()) }
    viewModel { TheatreViewModel(get()) }
    viewModel { PersonsListViewModel(get()) }
    viewModel { PersonViewModel(get()) }
}