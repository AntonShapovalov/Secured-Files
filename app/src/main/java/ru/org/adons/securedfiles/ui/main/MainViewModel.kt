package ru.org.adons.securedfiles.ui.main

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import ru.org.adons.securedfiles.R
import ru.org.adons.securedfiles.ext.*
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

    val title = MutableLiveData<String>()
    val state = StateLiveData()

    private val subscriptions: CompositeSubscription = CompositeSubscription()

    fun setDefaultState() {
        if (title.value == null && state.value == StateIdle) onNavItemSelected(R.string.nav_title_doc)
    }

    /**
     * Navigation Drawer item selected in [MainActivity]
     */
    fun onNavItemSelected(navId: Int) {
        val titleId: Int
        val path: String
        when (navId) {
            R.id.nav_music -> {
                titleId = R.string.nav_title_music
                path = MUSIC_PATH
            }
            R.id.nav_pictures -> {
                titleId = R.string.nav_title_pic
                path = PICTURES_PATH
            }
            R.id.nav_videos -> {
                titleId = R.string.nav_title_video
                path = VIDEOS_PATH
            }
            else -> {
                titleId = R.string.nav_title_doc
                path = DOCUMENTS_PATH
            }
        }
        title.value = context.getString(titleId)
        loadFiles(path)
    }

    /**
     * Load files from selected directory and notify [MainFragment]
     */
    private fun loadFiles(path: String) {
        val s = Observable.just(path)
                .map { context.getInternalFiles(it) }
                .map { it.map { InternalItem(it) } }
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

class InternalItem(override val file: File, val isLoaded: Boolean = true) : FileItem