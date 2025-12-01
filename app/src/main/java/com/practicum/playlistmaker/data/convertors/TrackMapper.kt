package com.practicum.playlistmaker.data.convertors

import com.practicum.playlistmaker.favourites.data.db.FavouriteTrackEntity
import com.practicum.playlistmaker.playlist.data.db.LibraryTrackEntity
import com.practicum.playlistmaker.search.data.dto.TrackDto
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.domain.model.millisToMMSS
import java.time.OffsetDateTime
import java.time.format.DateTimeParseException

class TrackMapper {
    private fun map(dto: TrackDto): Track {
        return Track(
            trackId = dto.trackId,
            trackName = dto.trackName,
            artistName = dto.artistName,
            trackTimeMillis = dto.trackTimeMillis,
            lengthText = millisToMMSS(dto.trackTimeMillis),
            artworkUrl100 = dto.artworkUrl100,
            coverArtwork = artworkUrlToCoverUrl(dto.artworkUrl100),
            collectionName = dto.collectionName ?: "",
            trackYear = getYearFromUtcDateTimeString(dto.releaseDate ?: ""),
            primaryGenreName = dto.primaryGenreName ?: "",
            country = dto.country ?: "",
            previewUrl = dto.previewUrl ?: ""
        )
    }

    fun map(track: FavouriteTrackEntity): Track {
        return Track(
            trackId = track.id,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            lengthText = millisToMMSS(track.trackTimeMillis),
            artworkUrl100 = track.artworkUrl100,
            coverArtwork = artworkUrlToCoverUrl(track.artworkUrl100),
            collectionName = track.collectionName,
            trackYear = track.trackYear,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl
        )
    }

    fun map(track: Track, insertTime: Long): FavouriteTrackEntity {
        return FavouriteTrackEntity(
            id = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            trackYear = track.trackYear,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
            insertTime = insertTime
        )
    }

    private fun getYearFromUtcDateTimeString(dateTimeString: String): String {
        return try {
            OffsetDateTime.parse(dateTimeString).year.toString()
        } catch (e: DateTimeParseException) {
            ""
        }
    }

    private fun artworkUrlToCoverUrl(artworkUrl: String): String {
        return artworkUrl.replaceAfterLast('/', "512x512bb.jpg")
    }

    fun map(entity: LibraryTrackEntity): Track {
        return Track(
            trackId = entity.id,
            trackName = entity.trackName,
            artistName = entity.artistName,
            trackTimeMillis = entity.trackTimeMillis,
            lengthText = millisToMMSS(entity.trackTimeMillis),
            artworkUrl100 = entity.artworkUrl100,
            coverArtwork = artworkUrlToCoverUrl(entity.artworkUrl100),
            collectionName = entity.collectionName,
            trackYear = entity.trackYear,
            primaryGenreName = entity.primaryGenreName,
            country = entity.country,
            previewUrl = entity.previewUrl
        )
    }

    fun map(dto: List<TrackDto>): List<Track> {
        return dto.map { map(it) }
    }
}