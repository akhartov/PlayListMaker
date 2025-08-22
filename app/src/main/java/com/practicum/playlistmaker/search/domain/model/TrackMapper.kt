package com.practicum.playlistmaker.search.domain.model

import com.practicum.playlistmaker.search.data.dto.TrackDto
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.format.DateTimeParseException
import java.util.Locale

object TrackMapper {
    fun mapToTrack(dto: TrackDto): Track {
        return Track(
            trackId = dto.trackId,
            trackName = dto.trackName,
            artistName = dto.artistName,
            length = millisToMMSS(dto.trackTimeMillis),
            artworkUrl100 = dto.artworkUrl100,
            coverArtwork = dto.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"),
            collectionName = dto.collectionName ?: "",
            trackYear = getYearFromUtcDateTimeString(dto.releaseDate ?: ""),
            primaryGenreName = dto.primaryGenreName ?: "",
            country = dto.country ?: "",
            previewUrl = dto.previewUrl ?: ""
        )
    }

    fun mapToTrack(dto: List<TrackDto>): List<Track> {
        return dto.map { mapToTrack(it) }
    }

    fun millisToMMSS(timeMillis: Int): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(timeMillis)
    }

    private fun getYearFromUtcDateTimeString(dateTimeString: String): String {
        return try {
            OffsetDateTime.parse(dateTimeString).year.toString()
        } catch (e: DateTimeParseException) {
            ""
        }
    }
}