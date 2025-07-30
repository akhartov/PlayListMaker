package com.practicum.playlistmaker.presentation

import android.content.Context
import com.practicum.playlistmaker.data.repository.HistoryRepositoryImpl
import com.practicum.playlistmaker.data.repository.TracksRepositoryImpl
import com.practicum.playlistmaker.data.network.ItunesNetworkClient
import com.practicum.playlistmaker.domain.repository.HistoryRepository
import com.practicum.playlistmaker.domain.api.TrackHistoryInteractor
import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.domain.repository.TracksRepository
import com.practicum.playlistmaker.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.domain.impl.TrackHistoryInteractorImpl
import com.practicum.playlistmaker.presentation.tracks.TrackAdapter

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(ItunesNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getHistoryRepository(context: Context): HistoryRepository {
        return HistoryRepositoryImpl(context)
    }

    fun getHistoryInteractor(context: Context, trackHistoryAdapter: TrackAdapter): TrackHistoryInteractor {
        return TrackHistoryInteractorImpl(getHistoryRepository(context), trackHistoryAdapter)
    }
}