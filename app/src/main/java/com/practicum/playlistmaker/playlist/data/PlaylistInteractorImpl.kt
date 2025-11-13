package com.practicum.playlistmaker.playlist.data

import android.net.Uri
import com.practicum.playlistmaker.playlist.domain.FileRepository
import com.practicum.playlistmaker.playlist.domain.PlaylistCover
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistInteractorImpl(
    val playlistRepository: PlaylistRepository,
    val fileRepository: FileRepository
) : PlaylistInteractor {
    override suspend fun updateCover(id: Int, cover: PlaylistCover) {
        TODO("Not yet implemented")
    }

    override suspend fun createCover(
        title: String,
        description: String,
        coverFullPath: Uri?
    ): Boolean {
        val fileName = PathResolver.getFilename(coverFullPath)
        if (fileName.isNullOrEmpty())
            return false

        if (fileRepository.saveImage(coverFullPath, fileName) == false)
            return false

        playlistRepository.addCover(title, description, fileName)
        return true
    }

    override fun getPlaylists(): Flow<List<PlaylistCover>> {
        return playlistRepository.getPlaylists().map { playlists ->
            playlists.map {
                PlaylistCover(
                    title = it.title,
                    description = it.description,
                    tracksInfo = it.items,
                    imagePath = it.coverImagePath
                )
            }
        }
    }
}