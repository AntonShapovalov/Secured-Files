package mobile.addons.securedfiles.file

import android.content.Context
import mobile.addons.securedfiles.ext.getType
import mobile.addons.securedfiles.ext.internalDir
import mobile.addons.securedfiles.ext.tryCopyTo
import mobile.addons.securedfiles.ui.add.DownloadItem
import mobile.addons.securedfiles.ui.main.InternalItem
import mobile.addons.securedfiles.ui.main.ItemLoadProgress
import mobile.addons.securedfiles.ui.main.ItemLoadSuccess
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subjects.PublishSubject
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

/**
 * File Manager, adds files from Downloads to Internal directory
 */
@Singleton
class FileManager @Inject constructor() {

    @Inject lateinit var context: Context

    val queueSubject: PublishSubject<String> = PublishSubject.create()

    private val queueItems = ConcurrentHashMap<String, InternalItem>()

    /**
     * Copy selected files from Download directory to internal dir
     */
    fun copyFromDownloads(newItems: Set<DownloadItem>) {
        val items = ArrayList<InternalItem>()
        Observable.just(newItems.toList())
                .map { buildInternalItems(it) }
                .doOnNext { items.addAll(it); addToQueue(it) }
                .flatMap { Observable.from(it) }
                .doOnNext { it.loadState = it.file.tryCopyTo(context.internalDir()) }
                .doOnNext { if (it.loadState === ItemLoadSuccess) removeFromQueue(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ queueSubject.onNext(it.type) }, { it.printStackTrace(); removeAllFromQueue(items) })
    }

    fun getQueueItems(type: String): List<InternalItem> = queueItems.values.filter { it.type == type }

    private fun addToQueue(items: List<InternalItem>) {
        queueItems.putAll(items.map { Pair(it.file.name, it) })
        items.map { it.type }.toHashSet().forEach { queueSubject.onNext(it) } // notify UI about new loading
    }

    private fun removeFromQueue(item: InternalItem) {
        queueItems.remove(item.file.name)
    }

    private fun removeAllFromQueue(items: List<InternalItem>) = items.filter { it.loadState !== ItemLoadSuccess }.forEach {
        removeFromQueue(it)
    }

    private fun buildInternalItems(list: List<DownloadItem>) = list.map {
        InternalItem(it.file, it.file.getType(), loadState = ItemLoadProgress)
    }

}
