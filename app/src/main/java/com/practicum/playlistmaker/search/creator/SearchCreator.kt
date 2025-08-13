package com.practicum.playlistmaker.search.creator

import android.app.Application
import com.practicum.playlistmaker.search.data.network.ItunesNetworkClient
import com.practicum.playlistmaker.search.data.repository.HistoryRepositoryImpl
import com.practicum.playlistmaker.search.domain.model.SearchTracksUseCase
import com.practicum.playlistmaker.search.domain.model.TrackHistoryInteractor
import com.practicum.playlistmaker.search.domain.impl.SearchTracksUseCaseImpl
import com.practicum.playlistmaker.search.domain.impl.TrackHistoryInteractorImpl
import com.practicum.playlistmaker.search.domain.repository.HistoryRepository

object SearchCreator {
    private lateinit var application: Application

    fun initApplication(application: Application) {
        SearchCreator.application = application
    }

    fun provideSearchTracksUseCase(): SearchTracksUseCase {
        return SearchTracksUseCaseImpl(ItunesNetworkClient())
    }

    private fun getHistoryRepository(): HistoryRepository {
        return HistoryRepositoryImpl(application.applicationContext)
    }

    fun getHistoryInteractor(): TrackHistoryInteractor {
        return TrackHistoryInteractorImpl(getHistoryRepository())
    }
}