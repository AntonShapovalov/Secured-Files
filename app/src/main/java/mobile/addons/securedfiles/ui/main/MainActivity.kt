package mobile.addons.securedfiles.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.coordinator_main.*
import mobile.addons.securedfiles.R
import mobile.addons.securedfiles.ext.MAIN_FRAGMENT_TAG
import mobile.addons.securedfiles.ext.addFragment
import mobile.addons.securedfiles.ext.appComponent
import mobile.addons.securedfiles.ext.getFragment
import mobile.addons.securedfiles.ui.add.AddActivity
import mobile.addons.securedfiles.ui.pass.PasswordActivity

/**
 * Main screen activity, manages navigation
 */
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        const val NAV_ID_KEY = "NAV_ID_KEY"
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
        }
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE) // do not show content in recent activities

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
                .also { appComponent.inject(it) }
                .also { vm -> vm.title.observe(this, Observer { supportActionBar?.title = it }) }

        ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close).also {
            drawer.addDrawerListener(it)
            it.syncState()
        }

        navigationView.setNavigationItemSelectedListener(this)
        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_docs)
            viewModel.setInitialState(R.string.nav_title_doc)
            val fragment = getFragment(MAIN_FRAGMENT_TAG) ?: MainFragment()
            addFragment(R.id.fragment_container, fragment, MAIN_FRAGMENT_TAG)
        } else {
            viewModel.setInitialState(savedInstanceState.getInt(NAV_ID_KEY, R.string.nav_title_doc))
        }

        fab.setOnClickListener { startActivity(Intent(this, AddActivity::class.java)) }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putInt(NAV_ID_KEY, viewModel.navId)
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_change_password) {
            PasswordActivity.changePassword(this)
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        viewModel.onNavItemSelected(item.itemId)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

}
