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
import mobile.addons.securedfiles.ext.appComponent
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
        buttonOk.setOnClickListener { viewModel.getPasswordState(editTextPassword.text.toString().toCharArray()) }
        editTextPassword.addTextChangedListener(PasswordTextWatcher(textLayoutPassword))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val act = activity ?: return
        viewModel = ViewModelProviders.of(act)
                .get(PasswordViewModel::class.java)
                .also { act.appComponent.inject(it) }
                .also { it.state.observe(this, Observer { onStateChanged(it); it?.log() }) }
                .also { it.process.observe(this, Observer { progress.visibilityCondition(it) }) }
                .also { it.getDefaultState() }
    }

    private fun onStateChanged(state: ViewModelState?) = when (state) {
        is PasswordNew -> textLayoutPassword.hint = state.hint
        is PasswordConfirm -> {
            editTextPassword.text = null
            textLayoutPassword.hint = state.hint
        }
        is PasswordIncorrect -> {
            if (state.hint.isNotEmpty()) textLayoutPassword.hint = state.hint
            textLayoutPassword.error = state.error
        }
        PasswordCorrect -> activity?.let { MainActivity.start(it) }
        is StateError -> textLayoutPassword.error = state.throwable.message
        else -> state?.log()
    }

}