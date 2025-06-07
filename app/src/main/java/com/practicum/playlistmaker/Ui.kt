package com.practicum.playlistmaker

open class Ui(val data: Data) {

    enum class State {
        Empty,
        History,
        InProgress,
        NotFound,
        Found,
        Error
    }

    data class Data(
        var state: State = State.Empty,
        var searchRequest: String? = null,
        var foundTracks: List<Track>? = listOf(),
    )

    companion object {
        val Empty = Ui(Data(State.Empty))
        val InProgress = Ui(Data(State.InProgress))
        val NotFound = Ui(Data(State.NotFound))
        val Error = Ui(Data(State.Error))
    }

    class Found(request: String? = null, foundTracks: List<Track>? = null) : Ui(Data(State.Found, request, foundTracks))
    class History(historyTracks: List<Track>? = null) : Ui(Data(State.History, null, historyTracks))
}