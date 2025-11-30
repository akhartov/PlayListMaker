package com.practicum.playlistmaker.data.convertors

import com.practicum.playlistmaker.playlist.data.WordDeclension
import com.practicum.playlistmaker.playlist.data.db.CoverEntity
import com.practicum.playlistmaker.playlist.data.dto.CoverStatisticsDto
import com.practicum.playlistmaker.playlist.domain.FileRepository
import com.practicum.playlistmaker.playlist.domain.TracksLibraryRepository
import com.practicum.playlistmaker.playlist.domain.model.PlaylistCover
import java.util.concurrent.TimeUnit

class PlaylistMapper(
    private val libraryRepository: TracksLibraryRepository,
    private val fileRepository: FileRepository,
    private val wordDeclension: WordDeclension
) {
    suspend fun map(entry: CoverEntity): PlaylistCover {
        return entry.let {
            val tracksCountText = wordDeclension.getTrackString(libraryRepository.getTracksCount(it.id).toInt())
            val tracksMinutesText = wordDeclension.getMinutesString(
                TimeUnit.MILLISECONDS.toMinutes(
                    libraryRepository.getTracksLength(it.id)
                ).toInt()
            )

            PlaylistCover(
                id = it.id,
                title = it.title,
                description = it.description,
                tracksCountText = tracksCountText,
                tracksMinutesText = tracksMinutesText,
                imagePath = fileRepository.getImagePath(it.coverFilename) ?: ""
            )
        }
    }

    fun map(entity: CoverStatisticsDto) : PlaylistCover {
        return PlaylistCover(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            tracksCountText = wordDeclension.getTrackString(entity.tracksCount),
            tracksMinutesText = wordDeclension.getMinutesString(
                TimeUnit.MILLISECONDS.toMinutes(
                    entity.tracksDurationMilliseconds
                ).toInt()
            ),
            imagePath = fileRepository.getImagePath(entity.coverFilename) ?: ""
        )
    }

    suspend fun map(entries: List<CoverEntity>): List<PlaylistCover> {
        return entries.map { map(it) }
    }
}
