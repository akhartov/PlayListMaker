package com.practicum.playlistmaker.playlist.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.playlist.data.dto.CoverStatisticsDto
import kotlinx.coroutines.flow.Flow

@Dao
interface CoverDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCover(playlistCover: CoverEntity)

    @Query("UPDATE playlists SET title = :title, description = :description, coverFilename = :coverFilename WHERE id = :entityId")
    suspend fun updateCover(entityId: Int, title: String, description: String, coverFilename: String?)

    @Query("SELECT * FROM playlists ORDER BY insertTime DESC")
    fun getItems(): Flow<List<CoverEntity>>

    @Query("SELECT * FROM playlists WHERE id = :playlistId ORDER BY insertTime DESC")
    fun getItem(playlistId: Int): Flow<CoverEntity>

    @Query("DELETE FROM playlists WHERE id = :playlistId")
    suspend fun deleteById(playlistId: Int)

    @Query("""
        SELECT 
            playlists.*,
            COUNT(library_track.id) AS tracksCount,
            SUM(library_track.trackTimeMillis) AS tracksDurationMilliseconds
        FROM playlists
        LEFT JOIN library_track ON playlists.id = library_track.playlistId
        GROUP BY playlists.id
        ORDER BY playlists.insertTime DESC
    """)
    fun getCoversWithStatistics(): Flow<List<CoverStatisticsDto>>

    @Query("""
        SELECT
            playlists.*,
            COUNT(library_track.id) AS tracksCount,
            SUM(library_track.trackTimeMillis) AS tracksDurationMilliseconds
        FROM playlists
        LEFT JOIN library_track ON playlists.id = library_track.playlistId
        WHERE playlists.id = :albumId
        GROUP BY playlists.id
    """)
    fun getCoverWithStatistics(albumId: Int): Flow<CoverStatisticsDto?>
}