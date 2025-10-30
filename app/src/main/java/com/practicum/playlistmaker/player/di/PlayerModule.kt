package com.practicum.playlistmaker.player.di

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.data.network.AudioPlayerImpl
import com.practicum.playlistmaker.player.domain.api.AudioPlayer
import com.practicum.playlistmaker.player.ui.PlayerViewModel
import com.practicum.playlistmaker.search.domain.model.Track
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerModule = module {
    single<AudioPlayer> { AudioPlayerImpl(get()) }
    single { MediaPlayer() }
}

val playerViewModelModule = module {
    viewModel { (track: Track?) ->
        PlayerViewModel(track, get(), get())
    }
}