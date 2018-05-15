package mobile.addons.securedfiles.ui.pass

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_password.*
import mobile.addons.securedfiles.R
import mobile.addons.securedfiles.ext.PASSWORD_FRAGMENT_TAG
import mobile.addons.securedfiles.ext.addFragment
import mobile.addons.securedfiles.ext.getFragment

class PasswordActivity : AppCompatActivity() {

    companion object {
        private const val SHOW_TOOLBAR_KEY = "SHOW_TOOLBAR_KEY"
        fun changePassword(context: Context) {
            val intent = Intent(context, PasswordActivity::class.java)
            intent.putExtra(SHOW_TOOLBAR_KEY, true)
            context.startActivity(intent)
        }
    }

    private lateinit var viewModel: PasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)
        if (intent.getBooleanExtra(SHOW_TOOLBAR_KEY, false)) setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setTitle(R.string.password_activity_title)
        }
        viewModel = ViewModelProviders.of(this).get(PasswordViewModel::class.java)
        if (savedInstanceState == null) {
            val fragment = getFragment(PASSWORD_FRAGMENT_TAG) ?: PasswordFragment()
            addFragment(R.id.fragment_container, fragment, PASSWORD_FRAGMENT_TAG)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        // deny close activity by back button, in case of change password user can use toolbar button
    }

}
