package ru.org.adons.securedfiles.ui.main

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_item_internal_file.view.*
import ru.org.adons.securedfiles.R
import ru.org.adons.securedfiles.ui.base.FileListAdapter

/**
 * Recycler adapter for File List in [MainFragment]
 */
class MainListAdapter(onItemClick: (InternalItem) -> Unit) : FileListAdapter<InternalItem>(onItemClick) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_internal_file, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val file = item.file
        val view = holder.itemView
        view.progress.visibility = if (item.isLoaded) View.GONE else View.VISIBLE
        view.textName.text = file.name
        view.textDate.text = DateUtils.getRelativeTimeSpanString(file.lastModified())
        view.setOnClickListener { onItemClick(item) }
    }

}
