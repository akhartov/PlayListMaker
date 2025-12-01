package com.practicum.playlistmaker.favourites.domain

import com.practicum.playlistmaker.player.ui.UserTrackState
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull

class FavouritesInteractorImpl(
    private val favouritesRepository: FavouritesRepository
) : FavouritesInteractor {

    private val _trackFlow: MutableStateFlow<UserTrackState?> = MutableStateFlow(null)

    override suspend fun changeFavourite(track: Track) {
        favouritesRepository.getAllTracksIds().collect { ids ->
            val itWasFavourite = (ids.indexOf(track.trackId) >= 0)
            if (itWasFavourite)
                favouritesRepository.deleteTrack(track.trackId)
            else
                favouritesRepository.addTrack(track)
            _trackFlow.value = UserTrackState(!itWasFavourite)
        }
    }

    override suspend fun getFavouriteTracks(): Flow<List<Track>> {
        return favouritesRepository.getAllTracks()
    }

    override suspend fun getFavouriteTracksIds(): Flow<List<Int>> {
        return favouritesRepository.getAllTracksIds()
    }

    override suspend fun beginMonitoring(trackId: Int) {
        favouritesRepository.getAllTracksIds().collect { ids ->
            _trackFlow.value = UserTrackState(isFavourite = (ids.indexOf(trackId) >= 0))
        }
    }

    override suspend fun flowTrackChanges(): Flow<UserTrackState> {
        return _trackFlow.asStateFlow().filterNotNull()
    }
}