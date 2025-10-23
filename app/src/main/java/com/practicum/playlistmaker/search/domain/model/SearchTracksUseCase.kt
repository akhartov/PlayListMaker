package com.practicum.playlistmaker.search.domain.model


import com.practicum.playlistmaker.search.ui.Resource
import kotlinx.coroutines.flow.Flow

interface SearchTracksUseCase {
    fun search(expression: String): Flow<Resource<List<Track>>>
}