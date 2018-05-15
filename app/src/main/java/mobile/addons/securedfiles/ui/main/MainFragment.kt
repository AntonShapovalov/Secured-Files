package mobile.addons.securedfiles.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import mobile.addons.securedfiles.ext.appComponent
import mobile.addons.securedfiles.ext.log
import mobile.addons.securedfiles.ui.base.FileListAdapter
import mobile.addons.securedfiles.ui.base.FileListFragment
import mobile.addons.securedfiles.ui.base.InternalFilesLoaded
import mobile.addons.securedfiles.ui.base.ViewModelState
import java.io.File

/**
 * Main screen content, display list of files
 */
class MainFragment : FileListFragment<InternalItem>() {

    private lateinit var viewModel: MainViewModel

    override fun getFileListAdapter(): FileListAdapter<InternalItem> = MainListAdapter { viewFile(it.file) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val act = activity ?: return
        viewModel = ViewModelProviders.of(act)
                .get(MainViewModel::class.java)
                .also { act.appComponent.inject(it) }
                .also { it.state.observe(this, Observer { onStateChanged(it) }) }
    }

    override fun onStateChanged(state: ViewModelState?) = when (state) {
        is InternalFilesLoaded -> setFiles(state.files)
        else -> super.onStateChanged(state)
    }

    private fun viewFile(file: File) {
        log("File -> ${file.name}")
    }

}