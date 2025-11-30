package com.practicum.playlistmaker.playlist.domain.impl

import com.practicum.playlistmaker.playlist.data.WordDeclension
import com.practicum.playlistmaker.playlist.domain.PlaylistMessagingCache
import com.practicum.playlistmaker.playlist.domain.model.PlaylistCover
import com.practicum.playlistmaker.search.domain.model.Track

class PlaylistMessagingCacheImpl(
    private val wordDeclension: WordDeclension,
) : PlaylistMessagingCache {
    private var cover: PlaylistCover? = null
    private var tracks: List<Track>? = null

    override fun hasTracks(): Boolean {
        return tracks?.isNotEmpty() ?: false
    }

    override fun makeMessage(): String {
        var message = ""
        cover?.let {
            message = it.title + "\n" +
                    it.description + "\n"
        }

        tracks?.let {
            wordDeclension.getTrackString(it.size) + "\n"
            it.forEachIndexed { index, track ->
                message += "${index + 1} .${track.artistName} - ${track.trackName} (${track.lengthText})\n"
            }
        }

        return message
    }


    override fun update(newCover: PlaylistCover) {
        cover = newCover
    }

    override fun update(newTracks: List<Track>) {
        tracks = newTracks
    }

}