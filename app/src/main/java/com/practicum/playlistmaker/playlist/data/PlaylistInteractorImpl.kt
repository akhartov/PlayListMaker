package com.practicum.playlistmaker.playlist.data

import android.net.Uri
import com.practicum.playlistmaker.playlist.data.db.PlaylistEntity
import com.practicum.playlistmaker.playlist.domain.FileRepository
import com.practicum.playlistmaker.playlist.domain.LibraryRepository
import com.practicum.playlistmaker.playlist.domain.PlaylistCover
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.PlaylistRepository
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class PlaylistInteractorImpl(
    val playlistRepository: PlaylistRepository,
    val libraryRepository: LibraryRepository,
    val fileRepository: FileRepository,
    val wordDeclension: WordDeclension
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
        emitPlaylists()
        return true
    }

    private val _playlistsFlow = MutableSharedFlow<List<PlaylistCover>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override val playlistsFlow: SharedFlow<List<PlaylistCover>> = _playlistsFlow.asSharedFlow()

    override suspend fun addTrackToPlaylist(playlistId: Int, track: Track) {
        libraryRepository.addTrack(playlistId, track)
        emitPlaylists()
    }

    private suspend fun emitPlaylists() {
        playlistRepository.getPlaylists().collect { items ->
            _playlistsFlow.emit(map(items))
        }
    }

    private suspend fun map(entry: PlaylistEntity?): PlaylistCover {
        return entry?.let {
            val tracksCount = libraryRepository.getTracksIds(it.id).size
            PlaylistCover(
                id = it.id,
                title = it.title,
                description = it.description,
                tracksInfo = "${tracksCount} ${wordDeclension.getTrackString(tracksCount)}",
                imagePath = fileRepository.getImagePath(it.coverFilename) ?: ""
            )
        } ?: PlaylistCover(0, "", "", "", "")
    }

    private suspend fun map(entries: List<PlaylistEntity>): List<PlaylistCover> {
        return entries.map { map(it) }
    }
}