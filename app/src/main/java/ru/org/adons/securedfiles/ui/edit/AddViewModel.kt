package ru.org.adons.securedfiles.ui.edit

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import ru.org.adons.securedfiles.ext.DOCUMENTS_DIR
import ru.org.adons.securedfiles.ext.getDownloadFiles
import ru.org.adons.securedfiles.file.FileManager
import ru.org.adons.securedfiles.ui.base.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.io.File
import javax.inject.Inject

/**
 * Provides data for [AddFragment] and handles "Done" button click from [EditActivity]
 *
 * @param dir - target internal directory to copy files into
 */
class AddViewModel(private val dir: String) : ViewModel(), EditViewModel {

    @Inject lateinit var fileManager: FileManager

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
        fileManager.copyFromDownloads(checkedItems.values.toSet(), dir)
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.unsubscribe()
    }

}

class DownloadItem(override val file: File, var isChecked: Boolean = false) : FileItem

@Suppress("UNCHECKED_CAST")
class AddViewModelFactory(private val dir: String = DOCUMENTS_DIR) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = AddViewModel(dir) as T
}
