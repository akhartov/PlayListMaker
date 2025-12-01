package com.practicum.playlistmaker.playlist.data

import com.practicum.playlistmaker.data.convertors.PlaylistMapper
import com.practicum.playlistmaker.playlist.data.db.CoverDao
import com.practicum.playlistmaker.playlist.data.db.CoverEntity
import com.practicum.playlistmaker.playlist.domain.CoverRepository
import com.practicum.playlistmaker.playlist.domain.model.PlaylistCover
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CoverRepositoryImpl(
    private val coverDao: CoverDao,
    private val playlistMapper: PlaylistMapper,
    private val pathResolver: PathResolver,
) : CoverRepository {
    override suspend fun addCover(title: String, description: String?, imagePath: String?) {
        coverDao.insertCover(
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
        coverDao.updateCover(playlistId, title, description, fileName)
    }

    override suspend fun deleteCover(playlistId: Int) {
        coverDao.deleteById(playlistId)
    }

    override fun getCovers(): Flow<List<PlaylistCover>> {
        return coverDao.getItems().map { entity -> playlistMapper.map(entity) }
    }

    override fun getCover(playlistId: Int): Flow<PlaylistCover?> {
        return coverDao.getItem(playlistId)
            .map { entity -> playlistMapper.map(entity) }
    }

    override fun getCoversWithStatistics(): Flow<List<PlaylistCover>> {
        return coverDao.getCoversWithStatistics().map { entities ->
            entities.map { entity -> playlistMapper.map(entity) }
        }
    }

    override fun getCoverWithStatistics(playlistId: Int): Flow<PlaylistCover?> {
        return coverDao.getCoverWithStatistics(playlistId).map { entity ->
            entity?.let { playlistMapper.map(entity) }
        }
    }
}