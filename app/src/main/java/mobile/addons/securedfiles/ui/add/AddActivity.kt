package mobile.addons.securedfiles.ui.add

import android.Manifest
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.tbruyelle.rxpermissions.RxPermissions
import mobile.addons.securedfiles.R
import mobile.addons.securedfiles.ext.*
import rx.android.schedulers.AndroidSchedulers

class AddActivity : AppCompatActivity() {

    private lateinit var viewModel: AddViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_clear)
            it.setTitle(R.string.add_activity_title)
        }
        RxPermissions(this)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ onPermissionSuccess(it, savedInstanceState) }, { onPermissionError(it) })
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
        if (isGranted) setContent(savedInstanceState) else finish()
    }

    private fun onPermissionError(throwable: Throwable) {
        showError(throwable, R.string.error_permissions, { finish() })
    }

    private fun setContent(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(AddViewModel::class.java).also { appComponent.inject(it) }
        if (savedInstanceState == null) {
            val fragment = getFragment(ADD_FILES_FRAGMENT_TAG) ?: AddFragment()
            addFragment(R.id.fragment_container, fragment, ADD_FILES_FRAGMENT_TAG)
        }
    }

}
