package com.practicum.playlistmaker.data.convertors

import com.practicum.playlistmaker.playlist.data.WordDeclension
import com.practicum.playlistmaker.playlist.data.db.PlaylistEntity
import com.practicum.playlistmaker.playlist.domain.FileRepository
import com.practicum.playlistmaker.playlist.domain.LibraryRepository
import com.practicum.playlistmaker.playlist.domain.PlaylistCover

class PlaylistMapper(
    val libraryRepository: LibraryRepository,
    val fileRepository: FileRepository,
    val wordDeclension: WordDeclension
) {
    suspend fun map(entry: PlaylistEntity?): PlaylistCover {
        return entry?.let {
            val tracksCount = libraryRepository.getTracksIds(it.id).size
            PlaylistCover(
                id = it.id,
                title = it.title,
                description = it.description,
                tracksInfo = "${tracksCount} ${wordDeclension.getTrackString(tracksCount)}",
                imagePath = fileRepository.getImagePath(it.coverFilename) ?: ""
            )
        } ?: PlaylistCover(0, "", "", "", "")
    }

    suspend fun map(entries: List<PlaylistEntity>): List<PlaylistCover> {
        return entries.map { map(it) }
    }
}
