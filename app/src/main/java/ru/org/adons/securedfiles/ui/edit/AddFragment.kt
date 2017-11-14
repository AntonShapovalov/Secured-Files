package ru.org.adons.securedfiles.ui.edit

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import ru.org.adons.securedfiles.ext.appComponent
import ru.org.adons.securedfiles.ui.base.*

/**
 * Add files screen, display list of files from Downloads directory
 */
class AddFragment : FileListFragment<DownloadItem>() {

    private lateinit var viewModel: AddViewModel

    override fun getFileListAdapter(): FileListAdapter<DownloadItem> = AddListAdapter { onItemCheck(it) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val act = activity ?: return
        viewModel = ViewModelProviders.of(act, ViewModelFactory())
                .get(AddViewModel::class.java)
                .also { act.appComponent.inject(it) }
                .also { it.state.observe(this, Observer { onStateChanged(it) }) }
                .also { it.loadFiles() }
    }

    override fun onStateChanged(state: ViewModelState?) = when (state) {
        is DownloadFilesLoaded -> setFiles(state.files)
        else -> super.onStateChanged(state)
    }

    private fun onItemCheck(item: DownloadItem) {
        if (item.isChecked) viewModel.addItem(item) else viewModel.removeItem(item)
    }

}