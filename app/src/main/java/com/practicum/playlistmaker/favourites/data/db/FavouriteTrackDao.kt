package com.practicum.playlistmaker.favourites.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteTrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: FavouriteTrackEntity)

    @Query("SELECT * FROM favourite_track ORDER BY insertTime DESC")
    fun getTracks(): Flow<List<FavouriteTrackEntity>>

    @Query("DELETE FROM favourite_track WHERE id = :trackId")
    suspend fun deleteTrackById(trackId: Int)

    @Query("SELECT id FROM favourite_track")
    suspend fun getAllTracksIds(): List<Int>
}