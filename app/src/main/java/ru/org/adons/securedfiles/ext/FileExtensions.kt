package ru.org.adons.securedfiles.ext

import android.content.Context
import android.os.Environment
import ru.org.adons.securedfiles.ui.main.ItemLoadError
import ru.org.adons.securedfiles.ui.main.ItemLoadState
import ru.org.adons.securedfiles.ui.main.ItemLoadSuccess
import java.io.File

/**
 * Extensions for [File]
 */
const val DOCUMENTS_DIR = "docs"
const val MUSIC_DIR = "music"
const val PICTURES_DIR = "pic"
const val VIDEOS_DIR = "video"

fun Context.internalDir(dir: String) = File(filesDir, dir)

fun Context.getInternalFiles(dir: String): List<File> {
    val internalDir = internalDir(dir).also { if (!it.exists()) it.mkdir() }
    return internalDir.sortedList()
}

fun getDownloadFiles(): List<File> {
    val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    return dir.sortedList()
}

fun File.tryCopyTo(destDir: File): ItemLoadState {
    val destFile = File(destDir, name)
    return try {
        copyTo(destFile)
        log("copied file $name (${length() / 1024} KB) into ${destDir.name}")
        ItemLoadSuccess
    } catch (e: Exception) {
        if (e is FileAlreadyExistsException) {
            ItemLoadSuccess
        } else {
            e.printStackTrace()
            if (destFile.exists()) destFile.delete()
            ItemLoadError(e.message ?: "Unable copy file $name")
        }
    }
}

private fun File.sortedList() = listFiles().toList().sortedByDescending { it.lastModified() }