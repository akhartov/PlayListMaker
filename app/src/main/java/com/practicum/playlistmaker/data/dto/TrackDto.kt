package com.practicum.playlistmaker.data.dto

data class TrackDto(
    val trackId: Int,              // Уникальный идентификатор записи
    val trackName: String,         // Название композиции
    val artistName: String,        // Имя исполнителя
    //TODO: make Formatter for trackTimeMillis -> Track.Time + make TrackMapper
    val trackTimeMillis: Int,      // Продолжительность трека
    val artworkUrl100: String,     // Ссылка на изображение обложки
    val collectionName: String?,   // Название альбома
    val releaseDate: String?,      // Дата релиза трека
    val primaryGenreName: String?, // Жанр трека
    val country: String?,          // Страна исполнителя
    val previewUrl: String?        // Ссылка на отрывок трека
)
