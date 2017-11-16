package ru.org.adons.securedfiles.ui.main

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import ru.org.adons.securedfiles.R
import ru.org.adons.securedfiles.ext.*
import ru.org.adons.securedfiles.file.FileManager
import ru.org.adons.securedfiles.ui.base.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.io.File
import javax.inject.Inject

/**
 * Handles navigation drawer events from [MainActivity] and provides data for [MainFragment]
 */
class MainViewModel : ViewModel() {

    @SuppressLint("StaticFieldLeak")
    @Inject lateinit var context: Context
    @Inject lateinit var fileManager: FileManager

    val title = MutableLiveData<String>()
    var dir = DOCUMENTS_DIR
    val state = StateLiveData()

    private val subscriptions: CompositeSubscription = CompositeSubscription()

    fun setDefaultState() {
        if (title.value == null && state.value == StateIdle) onNavItemSelected(R.string.nav_title_doc)
        val s = fileManager.queueSubject.filter { it == dir }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ loadFiles() }, { it.printStackTrace() })
        subscriptions.add(s)
    }

    /**
     * Navigation Drawer item selected in [MainActivity]
     */
    fun onNavItemSelected(navId: Int) {
        val titleId: Int
        when (navId) {
            R.id.nav_music -> {
                titleId = R.string.nav_title_music
                dir = MUSIC_DIR
            }
            R.id.nav_pictures -> {
                titleId = R.string.nav_title_pic
                dir = PICTURES_DIR
            }
            R.id.nav_videos -> {
                titleId = R.string.nav_title_video
                dir = VIDEOS_DIR
            }
            else -> {
                titleId = R.string.nav_title_doc
                dir = DOCUMENTS_DIR
            }
        }
        title.value = context.getString(titleId)
        loadFiles()
    }

    /**
     * Load files from selected directory and notify [MainFragment]
     */
    private fun loadFiles() {
        val queueItems = Observable.fromCallable { fileManager.getQueueItems(dir) }
                .doOnNext { log("queueItems -->");it.forEach { log("file=${it.file.name},${it.loadState.javaClass.simpleName}") } }
        val internalItems = Observable.fromCallable { context.getInternalFiles(dir).map { InternalItem(it, dir) } }
                .doOnNext { log("internalItems -->");it.forEach { log("file=${it.file.name},${it.loadState.javaClass.simpleName}") } }
        val s = queueItems.concatWith(internalItems)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { state.value = StateProgress }
                .subscribe({ state.value = InternalFilesLoaded(it) }, { state.value = StateError(it) })
        subscriptions.add(s)
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.unsubscribe()
    }

}

class InternalItem(override val file: File, val dir: String, var loadState: ItemLoadState = ItemLoadSuccess) : FileItem

sealed class ItemLoadState
object ItemLoadSuccess : ItemLoadState()
object ItemLoadProgress : ItemLoadState()
data class ItemLoadError(val message: String) : ItemLoadState()