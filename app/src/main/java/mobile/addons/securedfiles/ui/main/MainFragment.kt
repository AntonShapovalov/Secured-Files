package mobile.addons.securedfiles.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import mobile.addons.securedfiles.ext.viewFile
import mobile.addons.securedfiles.ui.abs.FileListAdapter
import mobile.addons.securedfiles.ui.abs.FileListFragment
import mobile.addons.securedfiles.ui.abs.InternalFilesLoaded
import mobile.addons.securedfiles.ui.abs.ViewModelState

/**
 * Main screen content, display list of files
 */
class MainFragment : FileListFragment<InternalItem>() {

    private lateinit var viewModel: MainViewModel

    override fun getFileListAdapter(): FileListAdapter<InternalItem> = MainListAdapter { activity?.viewFile(it.file) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val act = activity ?: return
        viewModel = ViewModelProviders.of(act)
                .get(MainViewModel::class.java)
                .also { it.state.observe(this, Observer { onStateChanged(it) }) }
    }

    override fun onStateChanged(state: ViewModelState?) = when (state) {
        is InternalFilesLoaded -> setFiles(state.files)
        else -> super.onStateChanged(state)
    }

}