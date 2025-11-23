package com.practicum.playlistmaker.playlist.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LibraryTrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: LibraryTrackEntity)

    @Query("SELECT * FROM library_track WHERE playlistId = :playlistId ORDER BY insertTime DESC")
    fun getTracks(playlistId: Int): Flow<List<LibraryTrackEntity>>

    @Query("DELETE FROM library_track WHERE playlistId = :playlistId AND id = :trackId")
    suspend fun deleteTrackById(playlistId: Int, trackId: Int)

    @Query("SELECT id FROM library_track WHERE playlistId = :playlistId")
    suspend fun getTracksIds(playlistId: Int): List<Int>

    @Query("SELECT SUM(trackTimeMillis) FROM library_track WHERE playlistId = :playlistId")
    suspend fun getTracksLength(playlistId: Int): Long?

    @Query("SELECT COUNT(*) FROM library_track WHERE playlistId = :playlistId")
    suspend fun getTracksCount(playlistId: Int): Long?
}