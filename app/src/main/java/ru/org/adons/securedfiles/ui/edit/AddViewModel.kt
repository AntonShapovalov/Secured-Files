package ru.org.adons.securedfiles.ui.edit

import android.arch.lifecycle.ViewModel
import ru.org.adons.securedfiles.ext.getDownloadFiles
import ru.org.adons.securedfiles.ext.log
import ru.org.adons.securedfiles.ui.base.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.io.File

/**
 * Provides data for [AddFragment] and handles "Done" button click from [EditActivity]
 */
class AddViewModel : ViewModel(), EditViewModel {

    val state = StateLiveData()

    private val checkedItems = HashMap<String, DownloadItem>()
    private val subscriptions: CompositeSubscription = CompositeSubscription()

    /**
     * Load all files from Download directory
     */
    fun loadFiles() {
        if (state.value is DownloadFilesLoaded) return
        val s = Observable.fromCallable { getDownloadFiles() }
                .map { it.map { DownloadItem(it) } }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { state.value = StateProgress }
                .subscribe({ state.value = DownloadFilesLoaded(it) }, { state.value = StateError(it) })
        subscriptions.add(s)
    }

    /**
     * Store (remove) checked item, if it has checked in list
     */
    fun addItem(item: DownloadItem) {
        checkedItems.put(item.file.name, item)
    }

    fun removeItem(item: DownloadItem) {
        checkedItems.remove(item.file.name)
    }

    /**
     * [EditActivity] "Done" action bar button click
     */
    override fun done() {
        checkedItems.values.forEach { log(it.file.name) }
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.unsubscribe()
    }

}

class DownloadItem(override val file: File, var isChecked: Boolean = false) : FileItem
