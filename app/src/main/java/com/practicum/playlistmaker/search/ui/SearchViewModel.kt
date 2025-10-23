package com.practicum.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.domain.model.SearchTracksUseCase
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.domain.model.TrackHistoryInteractor
import com.practicum.playlistmaker.ui.debounce
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class SearchViewModel(
    private val historyInteractor: TrackHistoryInteractor,
    private val searchTracksUseCase: SearchTracksUseCase
) : ViewModel() {
    private val searchStateLiveData = MutableLiveData(SearchState.Empty as SearchState)
    fun getSearchStateLiveData(): LiveData<SearchState> = searchStateLiveData

    private var latestSearchText = ""
    private var foundTracks = emptyList<Track>()
    private var isLastSearchFailed = false

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

    private val tracksSearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, false) { changedText ->
            searchRequest(changedText)
        }

    fun searchDebounce(changedText: String) {
        if (latestSearchText != changedText || isLastSearchFailed) {
            latestSearchText = changedText
            tracksSearchDebounce(changedText)
        }
    }

    private fun renderState(state: SearchState) {
        searchStateLiveData.postValue(state)
    }

    private fun searchRequest(newSearchText: String) {
        isLastSearchFailed = false
        if (newSearchText.isEmpty()) {
            showHistory()
            return
        }

        renderState(SearchState.InProgress)
        viewModelScope.launch {
            searchTracksUseCase
                .search(newSearchText)
                .catch { error ->
                    isLastSearchFailed = true
                    foundTracks = emptyList()
                    renderState(SearchState.Error(error.message ?: ""))
                }
                .collect { result ->
                    foundTracks = result.tracks
                    if (foundTracks.isEmpty())
                        renderState(SearchState.NotFound)
                    else
                        renderState(SearchState.Found(foundTracks))
                }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}