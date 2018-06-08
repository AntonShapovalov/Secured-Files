package mobile.addons.securedfiles.ui.pass

import android.support.design.widget.TextInputLayout
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView

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

/**
 * Handle "Done" button click of software keyboard
 */
class PasswordEditorActionListener(private val onDoneClick: () -> Unit) : TextView.OnEditorActionListener {
    override fun onEditorAction(view: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            onDoneClick()
            return true
        }
        return false
    }
}