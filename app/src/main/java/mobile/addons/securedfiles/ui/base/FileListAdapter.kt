package mobile.addons.securedfiles.ui.base

import android.support.v7.widget.RecyclerView
import android.view.View
import mobile.addons.securedfiles.ui.add.DownloadItem
import mobile.addons.securedfiles.ui.main.InternalItem
import java.io.File

/**
 * Base interfaces for [InternalItem] and [DownloadItem]
 */
interface FileItem {
    val file: File
}

/**
 * Base Recycler adapter for File List
 */
abstract class FileListAdapter<T : FileItem>(protected val onItemClick: (T) -> Unit) : RecyclerView.Adapter<FileListAdapter.ViewHolder>() {

    protected val items = ArrayList<T>()

    override fun getItemCount(): Int = items.size

    fun setItems(newItems: List<T>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun clearItems() {
        items.clear()
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}

