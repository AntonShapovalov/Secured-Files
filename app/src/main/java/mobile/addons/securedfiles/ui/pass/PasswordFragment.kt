package mobile.addons.securedfiles.ui.pass

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_password.*
import mobile.addons.securedfiles.R
import mobile.addons.securedfiles.ext.visibilityCondition
import mobile.addons.securedfiles.ui.abs.*
import mobile.addons.securedfiles.ui.main.MainActivity

/**
 * Set/check/edit password screen
 */
class PasswordFragment : Fragment() {

    private lateinit var viewModel: PasswordViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater
            .inflate(R.layout.fragment_password, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonOk.setOnClickListener { getPasswordState() }
        editTextPassword.addTextChangedListener(PasswordTextWatcher(textLayoutPassword))
        editTextPassword.setOnEditorActionListener(PasswordEditorActionListener { getPasswordState() })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val act = activity ?: return
        viewModel = ViewModelProviders.of(act)
                .get(PasswordViewModel::class.java)
                .also { it.state.observe(this, Observer { onStateChanged(it); it?.log() }) }
                .also { it.process.observe(this, Observer { progress.visibilityCondition(it) }) }
                .also { it.getInitialState() }
    }

    private fun getPasswordState() {
        viewModel.getPasswordState(editTextPassword.text.toString().toCharArray())
    }

    private fun onStateChanged(state: ViewModelState?) = when (state) {
        is PasswordCheck -> updateUI(hint = state.hint)
        is PasswordNew -> updateUI(hint = state.hint)
        is PasswordConfirm -> updateUI(hint = state.hint)
        is PasswordIncorrect -> updateUI(state.hint, state.error)
        is StateError -> updateUI(error = state.throwable.message)
        PasswordCorrect -> activity?.let { MainActivity.start(it) }
        else -> state?.log()
    }

    private fun updateUI(hint: String? = null, error: String? = null) {
        editTextPassword.text = null
        hint?.let { textLayoutPassword.hint = it }
        error?.let { textLayoutPassword.error = it }
    }

}