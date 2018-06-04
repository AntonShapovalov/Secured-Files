package mobile.addons.securedfiles.ui.pass

import android.arch.lifecycle.ViewModel
import mobile.addons.securedfiles.ui.abs.PasswordIsCorrect
import mobile.addons.securedfiles.ui.abs.StateLiveData

/**
 * Provides data for [PasswordFragment]
 */
class PasswordViewModel : ViewModel() {

    val state = StateLiveData()

    fun getPasswordState(pass: CharArray) {
        state.value = PasswordIsCorrect
    }

}