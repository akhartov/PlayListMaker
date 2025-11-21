package com.practicum.playlistmaker.playlist.data

import android.net.Uri
import java.io.File

class PathResolver {
    companion object {
        @JvmStatic
        fun getFilename(uri: Uri?): String? {
            return uri?.path?.let { File(it).name}
        }
    }
}