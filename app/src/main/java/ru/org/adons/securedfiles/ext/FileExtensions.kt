package ru.org.adons.securedfiles.ext

import android.content.Context
import android.os.Environment
import java.io.File

/**
 * Extensions for [File]
 */
const val DOCUMENTS_PATH = "docs"
const val MUSIC_PATH = "music"
const val PICTURES_PATH = "pic"
const val VIDEOS_PATH = "video"

fun Context.getInternalFiles(path: String): List<File> {
    val dir = File(filesDir, path).also { if (!it.exists()) it.mkdir() }
    return dir.sortedList()
}

fun getDownloadFiles(): List<File> {
    val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    return dir.sortedList()
}

private fun File.sortedList() = listFiles().toList().sortedByDescending { it.lastModified() }