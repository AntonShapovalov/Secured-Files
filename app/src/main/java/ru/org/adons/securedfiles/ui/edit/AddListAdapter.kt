package ru.org.adons.securedfiles.ui.edit

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import kotlinx.android.synthetic.main.list_item_download_file.view.*
import ru.org.adons.securedfiles.R
import ru.org.adons.securedfiles.ui.base.FileListAdapter

/**
 * Recycler adapter for File List in [AddFragment]
 */
class AddListAdapter(onItemClick: (DownloadItem) -> Unit) : FileListAdapter<DownloadItem>(onItemClick) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_download_file, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val file = item.file
        val view = holder.itemView
        view.textName.text = file.name
        view.textDate.text = DateUtils.getRelativeTimeSpanString(file.lastModified())
        view.checkBox.setOnClickListener { setChecked(item, view.checkBox) }
        view.setOnClickListener { setChecked(item, view.checkBox) }
    }

    private fun setChecked(item: DownloadItem, checkBox: CheckBox) {
        item.isChecked = !item.isChecked
        checkBox.isChecked = item.isChecked
        onItemClick(item)
    }

}