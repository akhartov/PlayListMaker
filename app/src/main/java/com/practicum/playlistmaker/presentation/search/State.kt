package com.practicum.playlistmaker.presentation.search

import com.practicum.playlistmaker.domain.models.Track

open class State(val data: Data) {

    enum class Value {
        Empty,
        History,
        InProgress,
        NotFound,
        Found,
        Error
    }

    data class Data(
        var value: Value = Value.Empty,
        var foundTracks: List<Track>? = listOf(),
    )

    companion object {
        val Empty = State(Data(Value.Empty))
        val InProgress = State(Data(Value.InProgress))
        val NotFound = State(Data(Value.NotFound))
        val Error = State(Data(Value.Error))
    }

    class Found(foundTracks: List<Track>? = null) : State(Data(Value.Found, foundTracks))
    class History(historyTracks: List<Track>? = null) : State(Data(Value.History, historyTracks))
}