package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.repository.HistoryRepository
import com.practicum.playlistmaker.search.domain.api.TrackHistoryInteractor
import com.practicum.playlistmaker.domain.models.Track

class TrackHistoryInteractorImpl(
    private val repo: HistoryRepository,
    private val changeListener: TrackHistoryInteractor.ChangeListener
) : TrackHistoryInteractor {

    override fun addTrack(track: Track) {
        var tracks = repo.getTracks().toMutableList()
        tracks.add(0, track)
        tracks = tracks.toSet().toMutableList()
        if (tracks.size > 10)
            tracks.remove(tracks[tracks.size - 1])

        repo.saveTracks(tracks)
        changeListener.onChange(tracks)
    }

    override fun clear() {
        repo.clearTracks()
        changeListener.onClear()
    }

    override fun isEmpty(): Boolean {
        return repo.getTracks().isEmpty()
    }

    override fun getTracks(): List<Track> {
        return repo.getTracks()
    }
}