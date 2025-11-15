package com.practicum.playlistmaker.playlist.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCover(playlistCover: PlaylistEntity)

    @Query("SELECT * FROM playlists")
    fun getItems(): Flow<List<PlaylistEntity>>
}