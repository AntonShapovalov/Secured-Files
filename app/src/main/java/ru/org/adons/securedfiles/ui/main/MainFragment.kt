package ru.org.adons.securedfiles.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main.*
import ru.org.adons.securedfiles.R
import ru.org.adons.securedfiles.ext.*
import java.io.File
import javax.inject.Inject

/**
 * Main screen content, display list of files
 */
class MainFragment : Fragment() {

    @Inject lateinit var factory: ViewModelFactory

    private lateinit var viewModel: MainViewModel

    private val adapter = FileListAdapter { viewFile(it) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater
            .inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fileList.initList(adapter)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val act = activity ?: return
        act.appComponent.inject(this)
        viewModel = ViewModelProviders.of(act, factory).get(MainViewModel::class.java)
        viewModel.state.observe(this, Observer { onStateChanged(it) })
    }

    private fun onStateChanged(state: ViewModelState?) = when (state) {
        is StateProgress -> flipper.progress()
        is FilesLoaded -> setFiles(state.files)
        is StateError -> showError(state.throwable, R.string.error_message_files, { flipper.empty() })
        else -> flipper.empty()
    }

    private fun setFiles(files: List<File>) = if (files.isEmpty()) {
        flipper.placeholder()
        adapter.clearItems()
    } else {
        flipper.empty()
        adapter.setItems(files)
    }

    private fun viewFile(file: File) {
        log("File -> ${file.name}")
    }

}