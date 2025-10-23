package com.practicum.playlistmaker.search.domain.model


import kotlinx.coroutines.flow.Flow

interface SearchTracksUseCase {
    fun search(expression: String): Flow<FoundTracksResponse>
}