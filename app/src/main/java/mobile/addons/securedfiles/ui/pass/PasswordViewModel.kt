package mobile.addons.securedfiles.ui.pass

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import mobile.addons.securedfiles.R
import mobile.addons.securedfiles.pass.PasswordManager
import mobile.addons.securedfiles.ui.abs.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

/**
 * Provides data for [PasswordFragment]
 */
class PasswordViewModel : ViewModel() {

    @SuppressLint("StaticFieldLeak")
    @Inject lateinit var context: Context
    @Inject lateinit var passManager: PasswordManager

    val state = StateLiveData()
    val process = MutableLiveData<Boolean>()

    private val subscriptions: CompositeSubscription = CompositeSubscription()

    fun getDefaultState() {
        if (state.value != StateIdle) return
        val s = Observable.fromCallable { passManager.isPasswordEmpty() }
                .map { if (it) PasswordNew(context.getString(R.string.password_fragment_hint_set_new)) else PasswordCheck }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { process.value = true }
                .doOnUnsubscribe { process.value = false }
                .subscribe({ state.value = it }, { state.value = StateError(it) })
        subscriptions.add(s)
    }

    fun getPasswordState(pass: CharArray) {
        if (state.value == StateProgress) return
        val s = Observable.just(pass)
                .filter { pass.isNotEmpty() }
                .map { getPassState(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { process.value = true }
                .doOnUnsubscribe { process.value = false }
                .subscribe({ state.value = it }, { state.value = StateError(it) })
        subscriptions.add(s)
    }

    private fun getPassState(pass: CharArray): ViewModelState = when {
        passManager.isPasswordEmpty() && (pass.isEmpty() || pass.size < 4) -> {
            val error: String
            val hint: String
            if (state.value is PasswordConfirm) {
                error = context.getString(R.string.error_pass_not_match)
                hint = context.getString(R.string.password_fragment_hint_set_new)
            } else {
                error = context.getString(R.string.error_pass_to_short)
                hint = ""
            }
            PasswordIncorrect(error, hint)
        }
        state.value is PasswordConfirm -> {
            val s = state.value as PasswordConfirm
            if (s.newPassword.contentEquals(pass)) {
                passManager.savePassword(pass)
                PasswordCorrect
            } else {
                val error = context.getString(R.string.error_pass_not_match)
                val hint = context.getString(R.string.password_fragment_hint_set_new)
                PasswordIncorrect(error, hint)
            }
        }
        passManager.isPasswordEmpty() -> {
            PasswordConfirm(pass.copyOf(), context.getString(R.string.password_fragment_hint_confirm))
        }
        passManager.checkPassword(pass) -> PasswordCorrect
        else -> PasswordIncorrect(context.getString(R.string.error_pass_incorrect))
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.unsubscribe()
    }

}