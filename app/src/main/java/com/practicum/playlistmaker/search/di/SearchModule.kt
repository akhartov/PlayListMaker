package com.practicum.playlistmaker.search.di

import android.content.Context
import com.google.gson.Gson
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.network.ItunesApiService
import com.practicum.playlistmaker.search.data.network.ItunesNetworkClient
import com.practicum.playlistmaker.search.data.repository.HistoryRepositoryImpl
import com.practicum.playlistmaker.search.domain.impl.SearchTracksUseCaseImpl
import com.practicum.playlistmaker.search.domain.impl.TrackHistoryInteractorImpl
import com.practicum.playlistmaker.search.domain.model.SearchTracksUseCase
import com.practicum.playlistmaker.search.domain.model.TrackHistoryInteractor
import com.practicum.playlistmaker.search.domain.repository.HistoryRepository
import com.practicum.playlistmaker.search.ui.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val searchModule = module {
    single {
        androidContext().getSharedPreferences("SEARCH", Context.MODE_PRIVATE)
    }

    single { Gson() }

    single<ItunesApiService> {
        Retrofit.Builder()
            .baseUrl(ItunesApiService.HOST_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesApiService::class.java)
    }

    factory<NetworkClient> { ItunesNetworkClient(get()) }

    factory<SearchTracksUseCase> { SearchTracksUseCaseImpl(get()) }

    single<HistoryRepository> { HistoryRepositoryImpl(get(), get()) }

    factory<TrackHistoryInteractor> { TrackHistoryInteractorImpl(get()) }
}

val searchViewModelModule = module {
    viewModel {
        SearchViewModel(get(), get(), get())
    }
}