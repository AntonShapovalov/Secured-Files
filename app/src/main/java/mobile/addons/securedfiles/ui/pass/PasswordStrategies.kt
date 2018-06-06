package mobile.addons.securedfiles.ui.pass

import android.content.Context
import mobile.addons.securedfiles.R
import mobile.addons.securedfiles.pass.PasswordManager
import mobile.addons.securedfiles.ui.abs.*

/**
 * All kinds of password's operations:
 * 1. set new pass on first start
 * 2. check existing pass each start
 * 3. change pass
 */
interface IPasswordStrategy {
    fun getPasswordState(pass: CharArray, currentState: ViewModelState): ViewModelState
}

/**
 * If application starts first time and password is empty
 */
class PasswordEmptyStrategy(private val context: Context, private val passManager: PasswordManager) : IPasswordStrategy {
    override fun getPasswordState(pass: CharArray, currentState: ViewModelState): ViewModelState = when {
        pass.isEmpty() || pass.size < 4 -> {
            val error: String
            val hint: String
            if (currentState is PasswordConfirm) {
                error = context.getString(R.string.error_pass_not_match)
                hint = context.getString(R.string.password_fragment_hint_set_new)
            } else {
                error = context.getString(R.string.error_pass_to_short)
                hint = ""
            }
            PasswordIncorrect(error, hint)
        }
        currentState is PasswordConfirm -> {
            if (currentState.newPassword.contentEquals(pass)) {
                passManager.savePassword(pass)
                PasswordCorrect
            } else {
                val error = context.getString(R.string.error_pass_not_match)
                val hint = context.getString(R.string.password_fragment_hint_set_new)
                PasswordIncorrect(error, hint)
            }
        }
        else -> PasswordConfirm(pass.copyOf(), context.getString(R.string.password_fragment_hint_confirm))
    }
}

/**
 * Check existing password
 */
class PasswordCheckStrategy(private val context: Context, private val passManager: PasswordManager) : IPasswordStrategy {
    override fun getPasswordState(pass: CharArray, currentState: ViewModelState): ViewModelState = when {
        passManager.checkPassword(pass) -> PasswordCorrect
        else -> PasswordIncorrect(context.getString(R.string.error_pass_incorrect))
    }
}

/**
 * Change existing password
 */
class PasswordChangeStrategy(private val context: Context, private val passManager: PasswordManager) : IPasswordStrategy {
    override fun getPasswordState(pass: CharArray, currentState: ViewModelState): ViewModelState = when {
        passManager.checkPassword(pass) -> PasswordNew(context.getString(R.string.password_fragment_hint_set_new))
        else -> PasswordIncorrect(context.getString(R.string.error_pass_incorrect))
    }
}



