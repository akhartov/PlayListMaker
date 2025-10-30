package com.practicum.playlistmaker.search.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Locale

@Parcelize
data class Track(
    val trackId: Int,              // Уникальный идентификатор записи
    val trackName: String,         // Название композиции
    val artistName: String,        // Имя исполнителя
    val length: String,            // Продолжительность трека
    val artworkUrl100: String,     // Ссылка на изображение обложки
    val coverArtwork: String,      // Ссылка на изображение обложки
    val collectionName: String,   // Название альбома
    val trackYear: String,        // Год релиза трека
    val primaryGenreName: String, // Жанр трека
    val country: String,          // Страна исполнителя
    val previewUrl: String,       // Ссылка на отрывок трека
) : Parcelable

fun millisToMMSS(timeMillis: Int): String {
    return SimpleDateFormat("mm:ss", Locale.getDefault()).format(timeMillis)
}