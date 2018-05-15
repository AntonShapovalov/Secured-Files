package mobile.addons.securedfiles.ext

import android.content.Context
import android.os.Environment
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.webkit.MimeTypeMap
import mobile.addons.securedfiles.ui.main.ItemLoadError
import mobile.addons.securedfiles.ui.main.ItemLoadState
import mobile.addons.securedfiles.ui.main.ItemLoadSuccess
import java.io.File

/**
 * Extensions for [File]
 */
const val INTERNAL_DIR = "loaded"
const val DOCUMENTS_TYPE = "docs"
const val AUDIO_TYPE = "audio"
const val IMAGE_TYPE = "image"
const val VIDEO_TYPE = "video"

fun Context.internalDir() = File(filesDir, INTERNAL_DIR)

fun Context.getInternalFiles(type: String): List<File> = internalDir()
        .also { if (!it.exists()) it.mkdir() }
        .listFiles().toList()
        .filter { it.getType() == type }
        .sortedByDescending { it.lastModified() }

fun getDownloadFiles(): List<File> = Environment
        .getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS)
        .listFiles().toList()
        .filter { it.isFile }
        .sortedByDescending { it.lastModified() }

fun File.getType(): String {
    if (extension.isEmpty()) return DOCUMENTS_TYPE
    val fileType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    return when {
        fileType == null -> DOCUMENTS_TYPE
        fileType.startsWith(AUDIO_TYPE) -> AUDIO_TYPE
        fileType.startsWith(IMAGE_TYPE) -> IMAGE_TYPE
        fileType.startsWith(VIDEO_TYPE) -> VIDEO_TYPE
        else -> DOCUMENTS_TYPE
    }
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
