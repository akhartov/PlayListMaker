package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.repository.HistoryRepository
import com.practicum.playlistmaker.search.domain.model.TrackHistoryInteractor
import com.practicum.playlistmaker.search.domain.model.Track

class TrackHistoryInteractorImpl(
    private val repo: HistoryRepository
) : TrackHistoryInteractor {

    override fun addTrack(track: Track) {
        var tracks = repo.getTracks().toMutableList()
        tracks.add(0, track)
        tracks = tracks.toSet().toMutableList()
        if (tracks.size > MAX_TRACKS)
            tracks.remove(tracks[tracks.lastIndex])

        repo.saveTracks(tracks)
    }

    override fun clear() {
        repo.clearTracks()
    }

    override fun isEmpty(): Boolean {
        return repo.getTracks().isEmpty()
    }

    override fun getTracks(consumer: TrackHistoryInteractor.Consumer) {
        consumer.consume(repo.getTracks())
    }

    companion object {
        const val MAX_TRACKS = 10
    }
}