package mobile.addons.securedfiles

import org.junit.Assert
import org.junit.Test
import java.security.SecureRandom
import java.security.spec.KeySpec
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/**
 * Example local unit test, which will execute on the development machine (host).
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class Simple {

    companion object {
        const val ALGORITHM = "PBKDF2WithHmacSHA1"
        const val ITERATION_COUNT = 65536
        const val KEY_LENGTH = 256
    }

    private val salt: ByteArray by lazy { ByteArray(KEY_LENGTH / 8).also { SecureRandom().nextBytes(it) } }
    private var pass: String = ""

    @Test
    @Throws(Exception::class)
    fun test() {
        val password = "password".toCharArray()
        pass = getHashValue(password)
        Assert.assertEquals(pass, getHashValue(password))
        Assert.assertNotEquals(pass, getHashValue("newPass".toCharArray()))
    }

    private fun getHashValue(password: CharArray): String {
        val factory: SecretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM)
        val spec: KeySpec = PBEKeySpec(password, salt, ITERATION_COUNT, KEY_LENGTH)
        val key: SecretKey = factory.generateSecret(spec)
        return Base64.getEncoder().encodeToString(key.encoded)
    }

}