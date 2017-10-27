package ru.org.adons.securedfiles.ui.main

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import ru.org.adons.securedfiles.R
import javax.inject.Inject

/**
 * Handles navigation drawer events from [MainActivity] and provides data for [MainFragment]
 */
class MainViewModel @Inject constructor() : ViewModel() {

    @SuppressLint("StaticFieldLeak")
    @Inject lateinit var context: Context

    val title = MutableLiveData<String>()

    fun setDefaultTitle() {
        title.value = context.getString(R.string.nav_title_doc)
    }

    fun onNavItemSelected(navId: Int) {
        val titleId = when (navId) {
            R.id.nav_music -> R.string.nav_title_music
            R.id.nav_pictures -> R.string.nav_title_pic
            R.id.nav_videos -> R.string.nav_title_video
            else -> R.string.nav_title_doc
        }
        title.value = context.getString(titleId)
    }

}