package mobile.addons.securedfiles.ui.pass

import android.support.design.widget.TextInputLayout
import android.text.Editable
import android.text.TextWatcher

/**
 * Clear error on change password
 */
class PasswordTextWatcher(private val textInputLayout: TextInputLayout) : TextWatcher {

    override fun afterTextChanged(s: Editable?) {}
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        textInputLayout.error = null
        textInputLayout.isErrorEnabled = false
    }

}