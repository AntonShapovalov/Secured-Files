package ru.org.adons.securedfiles.file

import android.content.Context
import ru.org.adons.securedfiles.ext.internalDir
import ru.org.adons.securedfiles.ext.tryCopyTo
import ru.org.adons.securedfiles.ui.edit.DownloadItem
import ru.org.adons.securedfiles.ui.main.InternalItem
import ru.org.adons.securedfiles.ui.main.ItemLoadProgress
import ru.org.adons.securedfiles.ui.main.ItemLoadSuccess
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
    fun copyFromDownloads(newItems: Set<DownloadItem>, dir: String) {
        val items = newItems.map { InternalItem(it.file, dir, loadState = ItemLoadProgress) }
        Observable.from(items)
                .doOnSubscribe { addToQueue(items, dir) }
                .doOnNext { it.loadState = it.file.tryCopyTo(context.internalDir(dir)) }
//                .doOnNext { if (it.loadState is ItemLoadSuccess) removeFromQueue(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ queueSubject.onNext(dir) }, { it.printStackTrace(); removeAllFromQueue(items) })
    }

    fun getQueueItems(dir: String): List<InternalItem> = queueItems.values.filter { it.dir == dir }

    private fun addToQueue(items: List<InternalItem>, dir: String) {
        queueItems.putAll(items.map { Pair(it.file.name, it) })
        queueSubject.onNext(dir)
    }

    private fun removeFromQueue(item: InternalItem) {
        queueItems.remove(item.file.name)
    }

    private fun removeAllFromQueue(items: List<InternalItem>) = items.filter { it.loadState !is ItemLoadSuccess }.forEach { removeFromQueue(it) }

}
