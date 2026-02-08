package com.practicum.playlistmaker.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.domain.model.SearchTracksUseCase
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.domain.model.TrackHistoryInteractor
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SearchViewModel(
    private val historyInteractor: TrackHistoryInteractor,
    private val searchTracksUseCase: SearchTracksUseCase
) : ViewModel() {
    private val searchStateLiveData = MutableStateFlow(SearchState.Empty as SearchState)
    fun getSearchStateFlow(): StateFlow<SearchState> = searchStateLiveData.asStateFlow()

    private var _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()
    private var foundTracks = emptyList<Track>()
    private var isLastSearchFailed = false

    init {
        searchText.debounce(SEARCH_DEBOUNCE_DELAY)
            .distinctUntilChanged()
            .onEach { text -> searchRequest(text) }
            .launchIn(viewModelScope)
    }

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

    fun searchDebounce(changedText: String) {
        _searchText.value = changedText
    }

    private fun renderState(state: SearchState) {
        searchStateLiveData.value = state
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
                    renderState(SearchState.Found(foundTracks))
                }
        }
    }

    fun editSearchRequestFocused() {
        if (searchText.value.isEmpty())
            showHistory()
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}