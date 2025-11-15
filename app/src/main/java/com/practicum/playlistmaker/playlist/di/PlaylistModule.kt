package com.practicum.playlistmaker.playlist.di

import com.practicum.playlistmaker.data.convertors.PlaylistMapper
import com.practicum.playlistmaker.playlist.data.FileRepositoryImpl
import com.practicum.playlistmaker.playlist.data.LibraryRepositoryImpl
import com.practicum.playlistmaker.playlist.data.PlaylistInteractorImpl
import com.practicum.playlistmaker.playlist.data.PlaylistRepositoryImpl
import com.practicum.playlistmaker.playlist.data.WordDeclension
import com.practicum.playlistmaker.playlist.domain.FileRepository
import com.practicum.playlistmaker.playlist.domain.LibraryRepository
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.PlaylistRepository
import com.practicum.playlistmaker.playlist.ui.PlaylistEditorViewModel
import com.practicum.playlistmaker.playlist.ui.PlaylistsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistViewModelModule = module {
    viewModel { PlaylistEditorViewModel(get()) }
    viewModel { PlaylistsViewModel(get()) }

    single<PlaylistRepository> { PlaylistRepositoryImpl(get()) }
    single<LibraryRepository> { LibraryRepositoryImpl(get()) }
    single { WordDeclension(androidContext()) }

    single<PlaylistInteractor> { PlaylistInteractorImpl(get(), get(), get(), get()) }
    factory { PlaylistMapper() }
    single<FileRepository> { FileRepositoryImpl(get()) }
}