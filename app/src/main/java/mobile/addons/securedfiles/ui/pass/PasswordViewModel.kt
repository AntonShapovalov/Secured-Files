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

    private lateinit var strategy: IPasswordStrategy
    private val subscriptions: CompositeSubscription = CompositeSubscription()

    fun getInitialState() {
        if (state.value != StateIdle && state.value !is PasswordChange) return
        val s = Observable.fromCallable { buildInitialState() }
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
                .map { buildPasswordState(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { process.value = true }
                .doOnUnsubscribe { process.value = false }
                .subscribe({ state.value = it }, { state.value = StateError(it) })
        subscriptions.add(s)
    }

    private fun buildInitialState(): ViewModelState = when {
        state.value is PasswordChange -> {
            strategy = PasswordChangeStrategy(context, passManager)
            state.value ?: StateIdle
        }
        passManager.isPasswordEmpty() -> {
            strategy = PasswordEmptyStrategy(context, passManager)
            PasswordNew(context.getString(R.string.password_fragment_hint_set_new))
        }
        else -> {
            strategy = PasswordCheckStrategy(context, passManager)
            PasswordCheck
        }
    }

    private fun buildPasswordState(pass: CharArray): ViewModelState {
        val currentState = state.value ?: return StateIdle
        val res = strategy.getPasswordState(pass, currentState)
        if (res is PasswordNew) strategy = PasswordEmptyStrategy(context, passManager)
        return res
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.unsubscribe()
    }

}