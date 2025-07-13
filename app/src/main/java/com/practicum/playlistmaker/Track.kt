package com.practicum.playlistmaker

import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.format.DateTimeParseException
import java.util.Locale

data class Track(
    val trackId: Int,              // Уникальный идентификатор записи
    val trackName: String,         // Название композиции
    val artistName: String,        // Имя исполнителя
    val trackTimeMillis: Int,      // Продолжительность трека
    val artworkUrl100: String,     // Ссылка на изображение обложки
    val collectionName: String?,   // Название альбома
    val releaseDate: String?,      // Дата релиза трека
    val primaryGenreName: String?, // Жанр трека
    val country: String?,           // Страна исполнителя
    val previewUrl: String?,        // Ссылка на отрывок трека
) {
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    fun trackTimeMMSS() = millisToMMSS(trackTimeMillis)
    fun trackYear() = getYearFromUtcDateTimeString(releaseDate?: "")

    private fun getYearFromUtcDateTimeString(dateTimeString: String): String {
        return try {
            OffsetDateTime.parse(dateTimeString).year.toString()
        } catch (e: DateTimeParseException) {
            ""
        }
    }

    companion object {
        fun millisToMMSS(timeMillis: Int): String {
            return SimpleDateFormat("mm:ss", Locale.getDefault()).format(timeMillis)
        }
    }
}