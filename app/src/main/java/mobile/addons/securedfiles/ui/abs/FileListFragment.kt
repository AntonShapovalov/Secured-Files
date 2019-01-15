package mobile.addons.securedfiles.ui.abs

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_file_list.*
import mobile.addons.securedfiles.R
import mobile.addons.securedfiles.ext.*
import mobile.addons.securedfiles.ui.add.AddFragment
import mobile.addons.securedfiles.ui.main.MainFragment

/**
 * Base fragment for [MainFragment] and [AddFragment]
 */
abstract class FileListFragment<T : FileItem> : Fragment() {

    private val adapter by lazy { getFileListAdapter() }

    abstract fun getFileListAdapter(): FileListAdapter<T>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater
            .inflate(R.layout.fragment_file_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fileList.initList(adapter)
    }

    open fun onStateChanged(state: ViewModelState?) = when (state) {
        is StateProgress -> flipper.progress()
        is StateError -> showError(state.throwable, R.string.error_message_files) { flipper.default() }
        else -> flipper.default()
    }

    protected fun setFiles(files: List<T>) = if (files.isEmpty()) {
        flipper.placeholder()
        adapter.clearItems()
    } else {
        flipper.default()
        adapter.setItems(files)
    }

}