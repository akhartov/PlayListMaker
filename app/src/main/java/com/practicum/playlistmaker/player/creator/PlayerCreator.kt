package com.practicum.playlistmaker.player.creator

import android.app.Application
import com.practicum.playlistmaker.player.data.impl.TrackDisplayInteractorImpl
import com.practicum.playlistmaker.player.data.providers.MediaPlayerProvider
import com.practicum.playlistmaker.player.domain.model.TrackDisplayInteractor
import com.practicum.playlistmaker.player.domain.providers.AudioPlayerProvider

object PlayerCreator {
    private lateinit var application: Application

    fun initApplication(application: Application) {
        PlayerCreator.application = application
    }

    fun getAudioPlayerProvider(): AudioPlayerProvider {
        return MediaPlayerProvider()
    }

    fun getTrackDisplayInteractor(): TrackDisplayInteractor {
        return TrackDisplayInteractorImpl(application.applicationContext)
    }
}