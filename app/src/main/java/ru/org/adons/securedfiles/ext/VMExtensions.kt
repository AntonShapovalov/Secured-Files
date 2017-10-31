package ru.org.adons.securedfiles.ext

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import ru.org.adons.securedfiles.ui.main.MainViewModel
import java.io.File
import java.lang.RuntimeException
import javax.inject.Inject

/**
 * General State of all ViewModel: Idle, Progress, Error
 * Each ViewModel can add own State like OwnState:ViewModelState
 */
sealed class ViewModelState

object StateIdle : ViewModelState()
object StateProgress : ViewModelState()
data class StateError(val throwable: Throwable) : ViewModelState()

data class FilesLoaded(val files: List<File>) : ViewModelState()

/**
 * ViewModelState LiveData - to init default state value
 */
class StateLiveData(state: ViewModelState = StateIdle) : MutableLiveData<ViewModelState>() {
    init {
        value = state
    }
}

/**
 * Factory to provide ViewModel-s for all UI components
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