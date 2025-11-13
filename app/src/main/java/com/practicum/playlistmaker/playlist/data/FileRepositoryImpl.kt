package com.practicum.playlistmaker.playlist.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.practicum.playlistmaker.playlist.domain.FileRepository
import java.io.File
import java.io.FileOutputStream

class FileRepositoryImpl(private val context: Context) : FileRepository {

    private val baseDirectory: File
        get() = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), COVERS_FOLDER)

    init {
        baseDirectory.mkdirs()
    }

    override fun saveImage(uri: Uri?, fileName: String): Boolean {
        if(uri == null)
            return false

        return try {
            val file = File(baseDirectory, fileName)
            val inputStream = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)

            inputStream?.use { input ->
                outputStream.use { output ->
                    BitmapFactory
                        .decodeStream(input)
                        .compress(Bitmap.CompressFormat.JPEG, 30, output)
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun getImagePath(fileName: String): String? {
        val file = File(baseDirectory, fileName)
        return if (file.exists()) file.absolutePath else null
    }

    override fun deleteImage(fileName: String): Boolean {
        return File(baseDirectory, fileName).delete()
    }

    companion object {
        const val COVERS_FOLDER = "cover"
    }
}
