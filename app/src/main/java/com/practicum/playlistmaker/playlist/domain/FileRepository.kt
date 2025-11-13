package com.practicum.playlistmaker.playlist.domain

import android.net.Uri

interface FileRepository {
    fun saveImage(uri: Uri?, fileName: String): Boolean
    fun getImagePath(fileName: String): String?
    fun deleteImage(fileName: String): Boolean
}