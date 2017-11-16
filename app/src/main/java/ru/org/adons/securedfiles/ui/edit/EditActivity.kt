package ru.org.adons.securedfiles.ui.edit

import android.Manifest
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.tbruyelle.rxpermissions.RxPermissions
import ru.org.adons.securedfiles.R
import ru.org.adons.securedfiles.ext.*
import rx.android.schedulers.AndroidSchedulers

class EditActivity : AppCompatActivity() {

    private lateinit var viewModel: EditViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        val titleId = intent.getIntExtra(EDIT_ACTIVITY_TITLE_KEY, R.string.add_files_title)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_clear)
            it.setTitle(titleId)
        }
        if (titleId == R.string.add_files_title) {
            RxPermissions(this)
                    .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ onPermissionSuccess(it, savedInstanceState) }, { onPermissionError(it) })
        } else {
            setContent(savedInstanceState, titleId)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_edit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        R.id.action_done -> {
            viewModel.done()
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun onPermissionSuccess(isGranted: Boolean, savedInstanceState: Bundle?) {
        if (isGranted) setContent(savedInstanceState, R.string.add_files_title) else finish()
    }

    private fun onPermissionError(throwable: Throwable) {
        showError(throwable, R.string.error_permissions, { finish() })
    }

    private fun setContent(savedInstanceState: Bundle?, titleId: Int) {
        if (savedInstanceState == null) {
            val fragment: Fragment
            val tag: String
            if (titleId == R.string.add_files_title) {
                tag = ADD_FILES_FRAGMENT_TAG
                fragment = getFragment(tag) ?: AddFragment()
            } else {
                tag = PASSWORD_FRAGMENT_TAG
                fragment = getFragment(tag) ?: PasswordFragment()
            }
            addFragment(R.id.fragment_container, fragment, tag)
        }
        viewModel = if (titleId == R.string.add_files_title) {
            val dir = intent.getStringExtra(EDIT_ACTIVITY_DIR_KEY) ?: DOCUMENTS_DIR
            ViewModelProviders.of(this, AddViewModelFactory(dir))
                    .get(AddViewModel::class.java)
                    .also { appComponent.inject(it) }
        } else {
            ViewModelProviders.of(this).get(PasswordViewModel::class.java)
        }
    }

}
