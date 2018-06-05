package mobile.addons.securedfiles.pass

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Base64
import java.security.SecureRandom
import java.security.spec.KeySpec
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.inject.Inject

/**
 * File manager, check and save password
 */
class PasswordManager @Inject constructor(context: Context) {

    companion object {
        private const val BASE64_FLAGS = Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
        private const val PASSWORD_KEY = "PASSWORD_KEY"
        private const val SEPARATOR = "]"
        private const val ALGORITHM = "PBKDF2WithHmacSHA1"
        private const val ITERATION_COUNT = 65536
        private const val KEY_LENGTH = 256
    }

    private val pref: SharedPreferences by lazy { context.getSharedPreferences("${context.packageName}.settings", MODE_PRIVATE) }

    fun isPasswordEmpty(): Boolean = pref.getString(PASSWORD_KEY, "").isEmpty()

    fun savePassword(pass: CharArray) {
        val salt: ByteArray = ByteArray(KEY_LENGTH / 8).also { SecureRandom().nextBytes(it) }
        val passHash = getPassHash(pass, salt)
        val saltHash = Base64.encodeToString(salt, BASE64_FLAGS)
        pref.edit().putString(PASSWORD_KEY, "$saltHash$SEPARATOR$passHash").apply()
    }

    fun checkPassword(pass: CharArray): Boolean {
        val key = pref.getString(PASSWORD_KEY, "")
        if (key.isEmpty()) return false
        val data = key.split(SEPARATOR)
        if (data.size != 2) return false
        val salt = Base64.decode(data[0], BASE64_FLAGS)
        val passHash = getPassHash(pass, salt)
        return passHash == data[1]
    }

    private fun getPassHash(pass: CharArray, salt: ByteArray): String {
        val factory: SecretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM)
        val spec: KeySpec = PBEKeySpec(pass, salt, ITERATION_COUNT, KEY_LENGTH)
        val key: SecretKey = factory.generateSecret(spec)
        return Base64.encodeToString(key.encoded, BASE64_FLAGS)
    }

}