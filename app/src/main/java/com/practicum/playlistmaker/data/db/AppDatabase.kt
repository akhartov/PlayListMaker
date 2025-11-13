package com.practicum.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.library.data.TrackEntity
import com.practicum.playlistmaker.library.data.db.TrackDao
import com.practicum.playlistmaker.playlist.data.PlaylistEntry
import com.practicum.playlistmaker.playlist.data.db.PlaylistDao

@Database(version = 1, entities = [TrackEntity::class, PlaylistEntry::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
}
