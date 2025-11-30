package com.practicum.playlistmaker.playlist.data

import com.practicum.playlistmaker.data.convertors.TrackMapper
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.playlist.data.db.LibraryTrackEntity
import com.practicum.playlistmaker.playlist.domain.TracksLibraryRepository
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksLibraryRepositoryImpl(
    private val database: AppDatabase,
    private val trackMapper: TrackMapper
): TracksLibraryRepository {
    override suspend fun addTrack(playlistId: Int, track: Track) {
        database.libraryTrackDao().insertTrack(LibraryTrackEntity(
            id = 0,
            playlistId = playlistId,
            insertTime = System.currentTimeMillis(),
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            trackYear = track.trackYear,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl
        ))
    }

    override suspend fun deleteTrack(playlistId: Int, trackId: Int) {
        database.libraryTrackDao().deleteTrackById(playlistId, trackId)
    }

    override suspend fun getTracksIds(playlistId: Int): List<Int> {
        return database.libraryTrackDao().getTracksIds(playlistId)
    }

    override suspend fun getTracksLength(playlistId: Int): Long {
        return database.libraryTrackDao().getTracksLength(playlistId) ?: 0
    }

    override suspend fun getTracksCount(playlistId: Int): Long {
        return database.libraryTrackDao().getTracksCount(playlistId) ?: 0
    }

    override suspend fun getPlaylistTracks(playlistId: Int): Flow<List<Track>> {
        return database.libraryTrackDao().getTracks(playlistId).map { entities ->
            entities.map { trackMapper.map(it) }
        }
    }
}