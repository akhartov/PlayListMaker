package com.practicum.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.favourites.data.db.FavouriteTrackEntity
import com.practicum.playlistmaker.favourites.data.db.FavouriteTrackDao
import com.practicum.playlistmaker.playlist.data.db.LibraryTrackDao
import com.practicum.playlistmaker.playlist.data.db.LibraryTrackEntity
import com.practicum.playlistmaker.playlist.data.db.PlaylistEntity
import com.practicum.playlistmaker.playlist.data.db.PlaylistDao

@Database(version = 1, entities = [FavouriteTrackEntity::class, PlaylistEntity::class, LibraryTrackEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouriteTrackDao(): FavouriteTrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun libraryTrackDao(): LibraryTrackDao
}
