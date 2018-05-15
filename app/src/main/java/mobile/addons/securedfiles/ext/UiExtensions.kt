package mobile.addons.securedfiles.ext

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.ViewFlipper
import mobile.addons.securedfiles.BuildConfig

/**
 * Extensions for UI elements
 */
fun log(message: String, tag: String = "LOG") {
    val prefix = "***"
    if (BuildConfig.DEBUG) {
        val prefixTag = if (!tag.startsWith(prefix)) "$prefix$tag" else tag
        Log.d(prefixTag, message)
    }
}

fun RecyclerView.initList(adapter: RecyclerView.Adapter<*>) {
    val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    layoutManager.isSmoothScrollbarEnabled = true
    this.layoutManager = layoutManager
    this.adapter = adapter
}

fun ViewFlipper.placeholder() = let { displayedChild = 0 } // show empty dir placeholder

fun ViewFlipper.progress() = let { displayedChild = 1 } // show progress

fun ViewFlipper.empty() = let { displayedChild = 2 } // hide all when list loaded
