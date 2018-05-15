package mobile.addons.securedfiles.ui.main

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import mobile.addons.securedfiles.R
import mobile.addons.securedfiles.ext.*
import mobile.addons.securedfiles.file.FileManager
import mobile.addons.securedfiles.ui.base.*
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
    var type = DOCUMENTS_TYPE
    val state = StateLiveData()

    private val subscriptions: CompositeSubscription = CompositeSubscription()

    fun setDefaultState() {
        if (title.value == null && state.value == StateIdle) onNavItemSelected(R.string.nav_title_doc)
        val s = fileManager.queueSubject.filter { it == type }
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
                type = AUDIO_TYPE
            }
            R.id.nav_pictures -> {
                titleId = R.string.nav_title_pic
                type = IMAGE_TYPE
            }
            R.id.nav_videos -> {
                titleId = R.string.nav_title_video
                type = VIDEO_TYPE
            }
            else -> {
                titleId = R.string.nav_title_doc
                type = DOCUMENTS_TYPE
            }
        }
        title.value = context.getString(titleId)
        loadFiles()
    }

    /**
     * Load files from selected directory and notify [MainFragment]
     */
    private fun loadFiles() {
        val queueItems = Observable.fromCallable { fileManager.getQueueItems(type) }
                .doOnNext { logItems("queueItems", it) }
        val internalItems = Observable.fromCallable { context.getInternalFiles(type).map { InternalItem(it, type) } }
                .doOnNext { logItems("internalItems", it) }
        val s = Observable.zip(queueItems, internalItems, { q, i -> q.toMutableList().apply { addAll(i) }.toList() })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { state.value = StateProgress }
                .subscribe({ state.value = InternalFilesLoaded(it) }, { state.value = StateError(it) })
        subscriptions.add(s)
    }

    private fun logItems(title: String, items: List<InternalItem>) {
        log("$title -->")
        items.forEach { log("file=${it.file.name},${it.loadState.javaClass.simpleName}") }
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.unsubscribe()
    }

}

class InternalItem(override val file: File, val type: String, var loadState: ItemLoadState = ItemLoadSuccess) : FileItem

sealed class ItemLoadState
object ItemLoadSuccess : ItemLoadState()
object ItemLoadProgress : ItemLoadState()
data class ItemLoadError(val message: String) : ItemLoadState()