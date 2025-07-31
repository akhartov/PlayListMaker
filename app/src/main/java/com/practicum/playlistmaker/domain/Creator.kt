package com.practicum.playlistmaker.domain

import android.app.Application
import com.practicum.playlistmaker.data.repository.HistoryRepositoryImpl
import com.practicum.playlistmaker.data.repository.TracksRepositoryImpl
import com.practicum.playlistmaker.data.network.ItunesNetworkClient
import com.practicum.playlistmaker.data.repository.MediaPlayerProvider
import com.practicum.playlistmaker.domain.repository.HistoryRepository
import com.practicum.playlistmaker.domain.api.TrackHistoryInteractor
import com.practicum.playlistmaker.domain.api.SearchTracksUseCase
import com.practicum.playlistmaker.domain.repository.TracksRepository
import com.practicum.playlistmaker.domain.impl.SearchTracksUseCaseImpl
import com.practicum.playlistmaker.domain.impl.TrackHistoryInteractorImpl
import com.practicum.playlistmaker.domain.providers.AudioPlayerProvider

object Creator {
    private lateinit var application: Application

    fun initApplication(application: Application) {
        Creator.application = application
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

    fun getAudioPlayerProvider(): AudioPlayerProvider {
        return MediaPlayerProvider()
    }
}