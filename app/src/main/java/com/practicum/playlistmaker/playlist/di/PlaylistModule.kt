package com.practicum.playlistmaker.playlist.di

import com.practicum.playlistmaker.data.convertors.PlaylistMapper
import com.practicum.playlistmaker.playlist.data.CoverRepositoryImpl
import com.practicum.playlistmaker.playlist.data.FileRepositoryImpl
import com.practicum.playlistmaker.playlist.data.PathResolver
import com.practicum.playlistmaker.playlist.data.TracksLibraryRepositoryImpl
import com.practicum.playlistmaker.playlist.data.WordDeclension
import com.practicum.playlistmaker.playlist.domain.CoverRepository
import com.practicum.playlistmaker.playlist.domain.CoversInteractor
import com.practicum.playlistmaker.playlist.domain.FileRepository
import com.practicum.playlistmaker.playlist.domain.PlaylistMessagingCache
import com.practicum.playlistmaker.playlist.domain.TracksLibraryRepository
import com.practicum.playlistmaker.playlist.domain.impl.PlaylistMessagingCacheImpl
import com.practicum.playlistmaker.playlist.ui.editor.PlaylistEditorViewModel
import com.practicum.playlistmaker.playlist.ui.grid.PlaylistsViewModel
import com.practicum.playlistmaker.playlist.ui.maker.PlaylistMakerViewModel
import com.practicum.playlistmaker.playlist.ui.view.PlaylistViewerViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistViewModelModule = module {
    viewModel { (playlistId: Int) -> PlaylistEditorViewModel(playlistId, get()) }
    viewModel { PlaylistMakerViewModel(get()) }
    viewModel { PlaylistsViewModel(get()) }
    viewModel { (playlistId: Int) -> PlaylistViewerViewModel(playlistId, get(), get(), get()) }

    single { PathResolver() }
    single<CoverRepository> { CoverRepositoryImpl(get(), get(), get()) }
    single<TracksLibraryRepository> { TracksLibraryRepositoryImpl(get(), get()) }
    single { WordDeclension(androidContext()) }
    factory<PlaylistMessagingCache> { PlaylistMessagingCacheImpl(get()) }
    single { CoversInteractor(get(), get(), get(), get()) }
    factory { PlaylistMapper(get(), get(), get()) }
    single<FileRepository> { FileRepositoryImpl(get()) }
}