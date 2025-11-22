package com.practicum.playlistmaker.playlist.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "library_track",
    foreignKeys = [
        ForeignKey(
            entity = PlaylistEntity::class,
            parentColumns = ["id"],
            childColumns = ["playlistId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class LibraryTrackEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,                  // Уникальный идентификатор записи в таблице БД
    val playlistId: Int,          // Ключ плейлиста для этого трека
    val insertTime: Long,         // Время добавления в базу данных
    val trackName: String,        // Название композиции
    val artistName: String,       // Имя исполнителя
    val trackTimeMillis: Int,     // Продолжительность трека
    val artworkUrl100: String,    // Ссылка на изображение обложки
    val collectionName: String,   // Название альбома
    val trackYear: String,        // Год релиза трека
    val primaryGenreName: String, // Жанр трека
    val country: String,          // Страна исполнителя
    val previewUrl: String,       // Ссылка на отрывок трека
)