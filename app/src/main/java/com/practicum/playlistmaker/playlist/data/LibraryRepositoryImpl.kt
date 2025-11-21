package com.practicum.playlistmaker.playlist.data

import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.playlist.data.db.LibraryTrackEntity
import com.practicum.playlistmaker.playlist.domain.LibraryRepository
import com.practicum.playlistmaker.search.domain.model.Track

class LibraryRepositoryImpl(
    private val database: AppDatabase
): LibraryRepository {
    override suspend fun addTrack(playlistId: Int, track: Track) {
        database.libraryTrackDao().insertTrack(LibraryTrackEntity(
            id = 0,
            playlistId = playlistId,
            insertTime = System.currentTimeMillis(),
            trackName = track.trackName,
            artistName = track.artistName,
            length = track.length,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            trackYear = track.trackYear,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl
        ))
    }

    override suspend fun getTracksIds(playlistId: Int): List<Int> {
        return database.libraryTrackDao().getTracksIds(playlistId)
    }
}