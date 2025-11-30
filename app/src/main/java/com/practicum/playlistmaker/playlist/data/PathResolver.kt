package com.practicum.playlistmaker.playlist.data

import android.net.Uri
import java.io.File

class PathResolver {
    fun getFilename(filePath: String?): String {
        return filePath?.let { File(it).name} ?: ""
    }

    fun getFilename(uri: Uri?): String {
        return uri?.path?.let { File(it).name} ?: ""
    }
}