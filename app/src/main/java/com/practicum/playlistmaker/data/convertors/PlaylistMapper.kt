package com.practicum.playlistmaker.data.convertors

import com.practicum.playlistmaker.playlist.data.PlaylistEntry
import com.practicum.playlistmaker.playlist.domain.PlaylistCover

class PlaylistMapper {
    fun map(playlistCover: PlaylistCover): PlaylistEntry {
        return PlaylistEntry(
            id = 0,
            title = playlistCover.title,
            description = playlistCover.description,
            coverFilename = playlistCover.imagePath
        )
    }
}
