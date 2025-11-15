package com.practicum.playlistmaker.data.convertors

import com.practicum.playlistmaker.playlist.data.db.PlaylistEntity
import com.practicum.playlistmaker.playlist.domain.PlaylistCover

class PlaylistMapper {
    fun map(playlistCover: PlaylistCover): PlaylistEntity {
        return PlaylistEntity(
            id = 0,
            title = playlistCover.title,
            description = playlistCover.description,
            coverFilename = playlistCover.imagePath
        )
    }
}
