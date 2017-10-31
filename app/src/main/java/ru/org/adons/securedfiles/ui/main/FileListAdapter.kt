package ru.org.adons.securedfiles.ui.main

import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_item_file.view.*
import ru.org.adons.securedfiles.R
import java.io.File

/**
 * Recycler adapter for File List in [MainFragment]
 */
class FileListAdapter(private val onItemClick: (File) -> Unit) : RecyclerView.Adapter<FileListAdapter.ViewHolder>() {

    private val items = ArrayList<File>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_file, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val file = items[position]
        val view = holder.itemView
        view.textName.text = file.name
        view.textDate.text = DateUtils.getRelativeTimeSpanString(file.lastModified())
        view.setOnClickListener { onItemClick(file) }
    }

    override fun getItemCount(): Int = items.size

    fun setItems(users: List<File>) {
        items.clear()
        items.addAll(users)
        notifyDataSetChanged()
    }

    fun clearItems() {
        items.clear()
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}