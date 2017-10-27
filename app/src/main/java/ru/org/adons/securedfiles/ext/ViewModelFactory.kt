package ru.org.adons.securedfiles.ext

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import ru.org.adons.securedfiles.ui.main.MainViewModel
import java.lang.RuntimeException
import javax.inject.Inject

/**
 * Factory to provide ViewModel-s for all Fragments
 */
class ViewModelFactory @Inject constructor(
        private val mainViewModel: MainViewModel
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>?): T = when {
        modelClass == null -> throw RuntimeException("Provide Class<T> for Factory")
        modelClass.isInstance(mainViewModel) -> mainViewModel as T
        else -> throw RuntimeException("Unknown ViewModel, inject it in constructor")
    }
}