package com.practicum.playlistmaker.playlist.data

import com.practicum.playlistmaker.data.convertors.TrackMapper
import com.practicum.playlistmaker.playlist.data.db.CoverTrackDao
import com.practicum.playlistmaker.playlist.data.db.LibraryTrackEntity
import com.practicum.playlistmaker.playlist.domain.TracksLibraryRepository
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksLibraryRepositoryImpl(
    private val coverTrackDao: CoverTrackDao,
    private val trackMapper: TrackMapper
): TracksLibraryRepository {
    override suspend fun addTrack(playlistId: Int, track: Track) {
        coverTrackDao.insertTrack(LibraryTrackEntity(
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
        coverTrackDao.deleteTrackById(playlistId, trackId)
    }

    override suspend fun getTracksIds(playlistId: Int): List<Int> {
        return coverTrackDao.getTracksIds(playlistId)
    }

    override suspend fun getTracksLength(playlistId: Int): Long {
        return coverTrackDao.getTracksLength(playlistId) ?: 0
    }

    override suspend fun getTracksCount(playlistId: Int): Long {
        return coverTrackDao.getTracksCount(playlistId) ?: 0
    }

    override fun getPlaylistTracks(playlistId: Int): Flow<List<Track>> {
        return coverTrackDao.getTracks(playlistId).map { entities ->
            entities.map { trackMapper.map(it) }
        }
    }
}