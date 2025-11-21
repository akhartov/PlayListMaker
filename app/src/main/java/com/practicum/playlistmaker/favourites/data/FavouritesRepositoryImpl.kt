package com.practicum.playlistmaker.favourites.data

import com.practicum.playlistmaker.data.convertors.TrackMapper
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.favourites.data.db.FavouriteTrackEntity
import com.practicum.playlistmaker.favourites.domain.FavouritesRepository
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FavouritesRepositoryImpl(
    private val database: AppDatabase,
    private val mapper: TrackMapper
) : FavouritesRepository {
    override suspend fun getAllTracks(): Flow<List<Track>> {
        return database.favouriteTrackDao().getTracks().map { tracks -> convertFromMovieEntity(tracks) }
    }

    override suspend fun getAllTracksIds(): Flow<List<Int>> = flow {
        database.favouriteTrackDao().getAllTracksIds().let { emit(it) }
    }

    override suspend fun addTrack(track: Track) {
        database.favouriteTrackDao().insertTrack(mapper.map(track, System.currentTimeMillis()))
    }

    override suspend fun deleteTrack(trackId: Int) {
        database.favouriteTrackDao().deleteTrackById(trackId)
    }

    private fun convertFromMovieEntity(tracks: List<FavouriteTrackEntity>): List<Track> {
        return tracks.map { track -> mapper.map(track) }
    }
}