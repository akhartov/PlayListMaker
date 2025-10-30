package com.practicum.playlistmaker.library.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks_history")
data class TrackEntity(
    @PrimaryKey
    val trackId: Int,             // Уникальный идентификатор записи
    val insertTime: Long,         // Время добавления в базу данных
    val trackName: String,        // Название композиции
    val artistName: String,       // Имя исполнителя
    val length: String,           // Продолжительность трека
    val artworkUrl100: String,    // Ссылка на изображение обложки
    val collectionName: String,   // Название альбома
    val trackYear: String,        // Год релиза трека
    val primaryGenreName: String, // Жанр трека
    val country: String,          // Страна исполнителя
    val previewUrl: String,       // Ссылка на отрывок трека
)