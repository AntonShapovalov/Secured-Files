package ru.org.adons.securedfiles.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.coordinator_main.*
import ru.org.adons.securedfiles.R
import ru.org.adons.securedfiles.ext.*
import javax.inject.Inject

/**
 * Main screen activity, manages navigation
 */
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    @Inject lateinit var factory: ViewModelFactory

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        appComponent.inject(this)
        viewModel = ViewModelProviders.of(this, factory).get(MainViewModel::class.java)
        viewModel.title.observe(this, Observer { supportActionBar?.title = it })

        ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close).also {
            drawer.addDrawerListener(it)
            it.syncState()
        }

        navigationView.setNavigationItemSelectedListener(this)
        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_docs)
            viewModel.setDefaultTitle()
            addFragment(R.id.fragment_container, getFragment(MAIN_FRAGMENT_TAG) ?: MainFragment(), MAIN_FRAGMENT_TAG)
        }
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return if (item.itemId == R.id.action_settings) true
        else super.onOptionsItemSelected(item)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        viewModel.onNavItemSelected(item.itemId)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

}
