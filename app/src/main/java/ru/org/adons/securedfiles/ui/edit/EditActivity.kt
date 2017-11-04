package ru.org.adons.securedfiles.ui.edit

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import ru.org.adons.securedfiles.R
import ru.org.adons.securedfiles.ext.EDIT_ACTIVITY_TITLE_KEY
import ru.org.adons.securedfiles.ext.log

class EditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_clear)
            it.setTitle(intent.getIntExtra(EDIT_ACTIVITY_TITLE_KEY, R.string.add_files_title))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_edit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_done -> {
                log("done")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
