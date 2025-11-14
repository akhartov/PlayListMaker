package com.practicum.playlistmaker.playlist.di

import com.practicum.playlistmaker.data.convertors.PlaylistMapper
import com.practicum.playlistmaker.playlist.data.FileRepositoryImpl
import com.practicum.playlistmaker.playlist.data.PlaylistInteractorImpl
import com.practicum.playlistmaker.playlist.data.PlaylistRepositoryImpl
import com.practicum.playlistmaker.playlist.domain.FileRepository
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.PlaylistRepository
import com.practicum.playlistmaker.playlist.ui.PlaylistEditorViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistViewModelModule = module {
    viewModel { (playlistId: Int, trackId: Int) ->
        PlaylistEditorViewModel(playlistId, trackId, get())
    }

    single<PlaylistRepository> { PlaylistRepositoryImpl(get()) }

    factory<PlaylistInteractor> { PlaylistInteractorImpl(get(), get()) }
    factory { PlaylistMapper() }
    single<FileRepository> { FileRepositoryImpl(get()) }
}