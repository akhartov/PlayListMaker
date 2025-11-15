package com.practicum.playlistmaker.favourites.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_track")
data class FavouriteTrackEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,                  // Уникальный для таблицы БД идентификатор записи
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