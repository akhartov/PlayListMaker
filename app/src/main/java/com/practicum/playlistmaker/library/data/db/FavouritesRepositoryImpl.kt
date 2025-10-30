package com.practicum.playlistmaker.library.data.db

import com.practicum.playlistmaker.data.convertors.TrackMapper
import com.practicum.playlistmaker.library.data.TrackEntity
import com.practicum.playlistmaker.library.db.FavouritesRepository
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FavouritesRepositoryImpl(
    private val database: AppDatabase,
    private val mapper: TrackMapper
) : FavouritesRepository {
    override suspend fun getAllTracks(): Flow<List<Track>> {
        return database.trackDao().getTracks().map { tracks -> convertFromMovieEntity(tracks) }
    }

    override suspend fun getAllTracksIds(): Flow<List<Int>> = flow {
        database.trackDao().getAllTracksIds().let { emit(it) }
    }

    override suspend fun addTrack(track: Track) {
        database.trackDao().insertTrack(mapper.map(track, System.currentTimeMillis()))
    }

    override suspend fun deleteTrack(trackId: Int) {
        database.trackDao().deleteTrackById(trackId)
    }

    private fun convertFromMovieEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> mapper.map(track) }
    }
}