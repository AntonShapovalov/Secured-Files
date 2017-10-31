package ru.org.adons.securedfiles.ext

import android.content.Context
import java.io.File

/**
 * Extensions for [File]
 */
const val DOCUMENTS_PATH = "docs"
const val MUSIC_PATH = "music"
const val PICTURES_PATH = "pic"
const val VIDEOS_PATH = "video"

fun Context.getFiles(path: String): List<File> {
    val dir = File(filesDir, path).also { it.mkdir() }
    return dir.listFiles().toList().sortedByDescending { it.lastModified() }
}