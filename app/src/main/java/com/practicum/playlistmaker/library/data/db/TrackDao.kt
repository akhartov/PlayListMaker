package com.practicum.playlistmaker.library.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.library.data.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Query("SELECT * FROM tracks_history ORDER BY insertTime DESC")
    fun getTracks(): Flow<List<TrackEntity>>

    @Query("DELETE FROM tracks_history WHERE trackId = :trackId")
    suspend fun deleteTrackById(trackId: Int)

    @Query("SELECT trackId FROM tracks_history")
    suspend fun getAllTracksIds(): List<Int>
}