package com.practicum.playlistmaker.playlist.data

import android.net.Uri
import com.practicum.playlistmaker.data.convertors.PlaylistMapper
import com.practicum.playlistmaker.playlist.domain.FileRepository
import com.practicum.playlistmaker.playlist.domain.LibraryRepository
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.PlaylistRepository
import com.practicum.playlistmaker.playlist.domain.PlaylistsEvent
import com.practicum.playlistmaker.playlist.domain.PlaylistsState
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class PlaylistInteractorImpl(
    val playlistRepository: PlaylistRepository,
    val libraryRepository: LibraryRepository,
    val fileRepository: FileRepository,
    val playlistMapper: PlaylistMapper
) : PlaylistInteractor {

    override suspend fun update() {
        emitPlaylists()
    }

    override suspend fun createCover(
        title: String,
        description: String,
        coverFullPath: Uri?
    ): Boolean {
        val fileName = PathResolver.getFilename(coverFullPath)
        fileName?.let { fileRepository.saveImage(coverFullPath, it) }

        playlistRepository.addCover(title, description, fileName)
        _playlistEventFlow.emit(PlaylistsEvent.NewPlaylist(title))
        emitPlaylists()
        return true
    }

    private val _playlistsFlow = MutableSharedFlow<PlaylistsState>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val playlistsFlow: SharedFlow<PlaylistsState> = _playlistsFlow.asSharedFlow()

    private val _playlistEventFlow = MutableSharedFlow<PlaylistsEvent>()
    override val playlistEventFlow: SharedFlow<PlaylistsEvent> = _playlistEventFlow.asSharedFlow()

    override suspend fun addTrackToPlaylist(playlistId: Int, track: Track) {
        libraryRepository.addTrack(playlistId, track)
        emitPlaylists()
    }

    private suspend fun emitPlaylists() {
        playlistRepository.getPlaylists().collect { items ->
            _playlistsFlow.emit(PlaylistsState(playlistMapper.map(items)))
        }
    }
}