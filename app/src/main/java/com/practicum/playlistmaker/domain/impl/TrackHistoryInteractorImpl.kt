package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.repository.HistoryRepository
import com.practicum.playlistmaker.domain.api.TrackHistoryInteractor
import com.practicum.playlistmaker.domain.models.Track

class TrackHistoryInteractorImpl(
    private val repo: HistoryRepository,
    private val changeListener: TrackHistoryInteractor.ChangeListener
) : TrackHistoryInteractor {
    init {
        changeListener.onChange(repo.getTracks())
    }

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
}