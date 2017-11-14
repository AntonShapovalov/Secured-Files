package ru.org.adons.securedfiles.ui.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import ru.org.adons.securedfiles.ui.edit.AddViewModel
import ru.org.adons.securedfiles.ui.main.MainViewModel
import java.lang.RuntimeException

/**
 * Factory to provide ViewModel-s for all UI components
 */
class ViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        MainViewModel::class.java -> MainViewModel() as T
        AddViewModel::class.java -> AddViewModel() as T
        else -> throw RuntimeException("Unknown ViewModel")
    }

}