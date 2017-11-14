package ru.org.adons.securedfiles.ui.base

import android.arch.lifecycle.MutableLiveData
import ru.org.adons.securedfiles.ui.edit.DownloadItem
import ru.org.adons.securedfiles.ui.main.InternalItem

/**
 * General State of all ViewModel: Idle, Progress, Error
 * Each ViewModel can add own State like OwnState:ViewModelState
 */
sealed class ViewModelState

object StateIdle : ViewModelState()
object StateProgress : ViewModelState()
data class StateError(val throwable: Throwable) : ViewModelState()

data class InternalFilesLoaded(val files: List<InternalItem>) : ViewModelState()
data class DownloadFilesLoaded(val files: List<DownloadItem>) : ViewModelState()

/**
 * ViewModelState LiveData - to init default state value
 */
class StateLiveData(state: ViewModelState = StateIdle) : MutableLiveData<ViewModelState>() {
    init {
        value = state
    }
}