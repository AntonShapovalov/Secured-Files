package mobile.addons.securedfiles.ui.pass

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_password.*
import mobile.addons.securedfiles.R
import mobile.addons.securedfiles.ext.*
import mobile.addons.securedfiles.ui.abs.PasswordChange

class PasswordActivity : AppCompatActivity() {

    companion object {
        private const val IS_CHANGE_PASSWORD_KEY = "IS_CHANGE_PASSWORD_KEY"
        fun changePassword(context: Context) {
            val intent = Intent(context, PasswordActivity::class.java)
            intent.putExtra(IS_CHANGE_PASSWORD_KEY, true)
            context.startActivity(intent)
        }
    }

    private lateinit var viewModel: PasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)
        val isChangePassword = intent.getBooleanExtra(IS_CHANGE_PASSWORD_KEY, false)
        if (isChangePassword) {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setTitle(R.string.password_activity_title)
        } else {
            appbar.gone()
        }
        viewModel = ViewModelProviders.of(this).get(PasswordViewModel::class.java)
                .also { appComponent.inject(it) }
                .also { if (isChangePassword) it.state.value = PasswordChange }
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
