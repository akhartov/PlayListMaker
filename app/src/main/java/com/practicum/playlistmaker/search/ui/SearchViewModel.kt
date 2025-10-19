package com.practicum.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.player.domain.model.TrackDisplayInteractor
import com.practicum.playlistmaker.search.domain.model.SearchTracksUseCase
import com.practicum.playlistmaker.search.domain.model.TrackHistoryInteractor
import com.practicum.playlistmaker.ui.debounce

class SearchViewModel(
    private val historyInteractor: TrackHistoryInteractor,
    private val searchTracksUseCase: SearchTracksUseCase,
    private val trackDisplayInteractor: TrackDisplayInteractor
) : ViewModel() {
    private val searchStateLiveData = MutableLiveData(SearchState.Empty as SearchState)
    fun getSearchStateLiveData(): LiveData<SearchState> = searchStateLiveData

    private var latestSearchText = ""
    private var foundTracks = emptyList<Track>()

    fun showHistory() {
        historyInteractor.getTracks(object : TrackHistoryInteractor.Consumer {
            override fun consume(tracks: List<Track>) {
                if (tracks.isEmpty())
                    renderState(SearchState.Empty)
                else
                    renderState(SearchState.History(tracks))
            }
        })
    }

    fun openTrack(track: Track) {
        historyInteractor.addTrack(track)
        if (foundTracks.isEmpty())
            showHistory()
    }

    fun clearHistory() {
        historyInteractor.clear()
        renderState(SearchState.Empty)
    }

    private val tracksSearchDebounce = debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, false) { changedText ->
        searchRequest(changedText)
    }

    fun searchDebounce(changedText: String) {
        if(latestSearchText != changedText) {
            latestSearchText = changedText
            tracksSearchDebounce(changedText)
        }
    }

    private fun renderState(state: SearchState) {
        searchStateLiveData.postValue(state)
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isEmpty()) {
            showHistory()
            return
        }

        renderState(SearchState.InProgress)
        searchTracksUseCase.search(newSearchText, object :
            SearchTracksUseCase.TracksConsumer {
            override fun consume(tracks: List<Track>) {
                foundTracks = tracks
                if (tracks.isEmpty()) {
                    renderState(SearchState.NotFound)
                } else {
                    renderState(SearchState.Found(tracks))
                }
            }

            override fun fail(e: Exception) {
                foundTracks = emptyList()
                renderState(SearchState.Error(e.message ?: ""))
            }
        })
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}