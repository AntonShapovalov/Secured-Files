package mobile.addons.securedfiles.ext

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
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

fun View.visibilityCondition(isVisible: Boolean?) = if (isVisible == true) show() else hide()

fun View.show() = let { visibility = View.VISIBLE }

fun View.hide() = let { visibility = View.INVISIBLE }

fun View.gone() = let { visibility = View.GONE }

fun ViewFlipper.placeholder() = let { displayedChild = 0 } // show empty dir placeholder

fun ViewFlipper.progress() = let { displayedChild = 1 } // show progress

fun ViewFlipper.empty() = let { displayedChild = 2 } // hide all when list loaded

fun RecyclerView.initList(adapter: RecyclerView.Adapter<*>) {
    val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    layoutManager.isSmoothScrollbarEnabled = true
    this.layoutManager = layoutManager
    this.adapter = adapter
}
