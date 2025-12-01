package com.practicum.playlistmaker.playlist.domain

import android.net.Uri
import com.practicum.playlistmaker.playlist.data.PathResolver
import com.practicum.playlistmaker.playlist.domain.model.CoverLibraryState
import com.practicum.playlistmaker.playlist.domain.model.PlaylistCover
import com.practicum.playlistmaker.playlist.domain.model.PlaylistsEvent
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class CoversInteractor(
    private val coverRepository: CoverRepository,
    private val fileRepository: FileRepository,
    private val tracksLibraryRepository: TracksLibraryRepository,
    private val pathResolver: PathResolver,
) {
    private val _coverEventFlow = MutableSharedFlow<PlaylistsEvent>()
    val coverEventFlow: SharedFlow<PlaylistsEvent> = _coverEventFlow.asSharedFlow()


    fun getTracksFlow(playlistId: Int): Flow<List<Track>> {
        return tracksLibraryRepository.getPlaylistTracks(playlistId)
    }

    fun getCoverFlow(playlistId: Int): Flow<PlaylistCover?> {
        return coverRepository.getCoverWithStatistics(playlistId)
    }

    fun getAllCoversFlow(): Flow<CoverLibraryState> {
        return coverRepository.getCoversWithStatistics().map { covers ->
            CoverLibraryState(
                items = covers
            )
        }
    }

    suspend fun makeCover(
        title: String,
        description: String,
        coverFullPath: Uri?
    ): Boolean {

        val fileName = pathResolver.getFilename(coverFullPath)
        if(fileName.isNotBlank())
            fileRepository.saveImage(coverFullPath, fileName)

        coverRepository.addCover(title, description, fileName)
        _coverEventFlow.emit(PlaylistsEvent.NewPlaylist(title))
        return true
    }

    suspend fun updateCover(
        playlistId: Int,
        title: String,
        description: String,
        coverFullPath: Uri?
    ): Boolean {
        val fileName = pathResolver.getFilename(coverFullPath)
        if(fileName.isNotBlank())
            fileRepository.saveImage(coverFullPath, fileName)

        coverRepository.updateCover(playlistId, title, description, fileName)
        return true
    }

    suspend fun deleteCover(playlistId: Int) {
        coverRepository.deleteCover(playlistId)
    }

    suspend fun addTrack(playlistId: Int, track: Track) {
        tracksLibraryRepository.addTrack(playlistId, track)
    }

    suspend fun deleteTrack(playlistId: Int, trackId: Int) {
        tracksLibraryRepository.deleteTrack(playlistId, trackId)
    }

    suspend fun getTracks(playlistId: Int): List<Track> {
        return tracksLibraryRepository.getPlaylistTracks(playlistId).first()
    }
}