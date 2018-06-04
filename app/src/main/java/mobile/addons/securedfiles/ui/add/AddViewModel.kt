package mobile.addons.securedfiles.ui.add

import android.arch.lifecycle.ViewModel
import mobile.addons.securedfiles.ext.getDownloadFiles
import mobile.addons.securedfiles.file.FileManager
import mobile.addons.securedfiles.ui.abs.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.io.File
import javax.inject.Inject

/**
 * Provides data for [AddFragment] and handles "Done" button click from [AddActivity]
 */
class AddViewModel : ViewModel() {

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
        checkedItems[item.file.name] = item
    }

    fun removeItem(item: DownloadItem) {
        checkedItems.remove(item.file.name)
    }

    /**
     * [AddActivity] "Done" action bar button click
     */
    fun done() {
        fileManager.copyFromDownloads(checkedItems.values.toSet())
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.unsubscribe()
    }

}

class DownloadItem(override val file: File, var isChecked: Boolean = false) : FileItem