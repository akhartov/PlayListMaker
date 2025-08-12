package com.practicum.playlistmaker.search.creator

import android.app.Application
import com.practicum.playlistmaker.search.data.network.ItunesNetworkClient
import com.practicum.playlistmaker.search.data.repository.HistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.repository.TracksRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.SearchTracksUseCase
import com.practicum.playlistmaker.search.domain.api.TrackHistoryInteractor
import com.practicum.playlistmaker.search.domain.impl.SearchTracksUseCaseImpl
import com.practicum.playlistmaker.search.domain.impl.TrackHistoryInteractorImpl
import com.practicum.playlistmaker.search.domain.repository.HistoryRepository
import com.practicum.playlistmaker.search.domain.repository.TracksRepository

object SearchCreator {
    private lateinit var application: Application

    fun initApplication(application: Application) {
        SearchCreator.application = application
    }

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(ItunesNetworkClient())
    }

    fun provideSearchTracksUseCase(): SearchTracksUseCase {
        return SearchTracksUseCaseImpl(getTracksRepository())
    }

    private fun getHistoryRepository(): HistoryRepository {
        return HistoryRepositoryImpl(application.applicationContext)
    }

    fun getHistoryInteractor(historyChangeListener: TrackHistoryInteractor.ChangeListener): TrackHistoryInteractor {
        return TrackHistoryInteractorImpl(getHistoryRepository(), historyChangeListener)
    }
}