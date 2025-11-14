package com.practicum.playlistmaker.playlist.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.playlist.data.PlaylistEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCover(playlistCover: PlaylistEntry)

    @Query("SELECT * FROM playlists ORDER BY title")
    fun getItems(): Flow<List<PlaylistEntry>>

    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    fun getItem(playlistId:Int): Flow<PlaylistEntry>
}