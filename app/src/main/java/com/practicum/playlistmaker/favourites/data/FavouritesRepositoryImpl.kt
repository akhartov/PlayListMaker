package com.practicum.playlistmaker.favourites.data

import com.practicum.playlistmaker.data.convertors.TrackMapper
import com.practicum.playlistmaker.favourites.data.db.FavouriteTrackDao
import com.practicum.playlistmaker.favourites.data.db.FavouriteTrackEntity
import com.practicum.playlistmaker.favourites.domain.FavouritesRepository
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FavouritesRepositoryImpl(
    private val favouriteTrackDao: FavouriteTrackDao,
    private val mapper: TrackMapper
) : FavouritesRepository {
    override suspend fun getAllTracks(): Flow<List<Track>> {
        return favouriteTrackDao.getTracks().map { tracks -> convertFromMovieEntity(tracks) }
    }

    override suspend fun getAllTracksIds(): Flow<List<Int>> = flow {
        favouriteTrackDao.getAllTracksIds().let { emit(it) }
    }

    override suspend fun addTrack(track: Track) {
        favouriteTrackDao.insertTrack(mapper.map(track, System.currentTimeMillis()))
    }

    override suspend fun deleteTrack(trackId: Int) {
        favouriteTrackDao.deleteTrackById(trackId)
    }

    private fun convertFromMovieEntity(tracks: List<FavouriteTrackEntity>): List<Track> {
        return tracks.map { track -> mapper.map(track) }
    }
}