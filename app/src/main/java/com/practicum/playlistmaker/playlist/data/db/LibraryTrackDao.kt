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

    @Query("SELECT * FROM library_track ORDER BY insertTime DESC")
    fun getTracks(): Flow<List<LibraryTrackEntity>>

    @Query("DELETE FROM library_track WHERE id = :trackId")
    suspend fun deleteTrackById(trackId: Int)

    @Query("SELECT id FROM library_track WHERE playlistId = :playlistId")
    suspend fun getTracksIds(playlistId: Int): List<Int>
}