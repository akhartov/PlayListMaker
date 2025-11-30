package com.practicum.playlistmaker.playlist.data

import com.practicum.playlistmaker.data.convertors.PlaylistMapper
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.playlist.data.db.CoverEntity
import com.practicum.playlistmaker.playlist.domain.CoverRepository
import com.practicum.playlistmaker.playlist.domain.model.PlaylistCover
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CoverRepositoryImpl(
    private val database: AppDatabase,
    private val playlistMapper: PlaylistMapper,
    private val pathResolver: PathResolver,
) : CoverRepository {
    override suspend fun addCover(title: String, description: String?, imagePath: String?) {
        database.playlistDao().insertCover(
            CoverEntity(
                0,
                System.currentTimeMillis(),
                title,
                description.toString(),
                pathResolver.getFilename(imagePath)
            )
        )
    }

    override suspend fun updateCover(
        playlistId: Int,
        title: String,
        description: String,
        fileName: String?
    ) {
        database.playlistDao().updateCover(playlistId, title, description, fileName)
    }

    override suspend fun deleteCover(playlistId: Int) {
        database.playlistDao().deleteById(playlistId)
    }

    override fun getCovers(): Flow<List<PlaylistCover>> {
        return database.playlistDao().getItems().map { entity -> playlistMapper.map(entity) }
    }

    override fun getCover(playlistId: Int): Flow<PlaylistCover?> {
        return database.playlistDao().getItem(playlistId)
            .map { entity -> playlistMapper.map(entity) }
    }

    override fun getCoversWithStatistics(): Flow<List<PlaylistCover>> {
        return database.playlistDao().getCoversWithStatistics().map { entities ->
            entities.map { entity -> playlistMapper.map(entity) }
        }
    }

    override fun getCoverWithStatistics(playlistId: Int): Flow<PlaylistCover?> {
        return database.playlistDao().getCoverWithStatistics(playlistId).map { entity ->
            entity?.let { playlistMapper.map(entity) }
        }
    }
}