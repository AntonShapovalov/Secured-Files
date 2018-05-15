package mobile.addons.securedfiles.ext

import android.app.Activity
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.widget.Toast
import mobile.addons.securedfiles.app.AppComponent
import mobile.addons.securedfiles.app.SFApplication

/**
 * Extensions for [Activity]
 */
const val MAIN_FRAGMENT_TAG = "MAIN_FRAGMENT_TAG"
const val ADD_FILES_FRAGMENT_TAG = "ADD_FILES_FRAGMENT_TAG"
const val PASSWORD_FRAGMENT_TAG = "PASSWORD_FRAGMENT_TAG"

fun FragmentActivity.getFragment(tag: String): Fragment? = supportFragmentManager.findFragmentByTag(tag)

fun FragmentActivity.addFragment(containerId: Int, fragment: Fragment, tag: String) = supportFragmentManager
        .beginTransaction().add(containerId, fragment, tag).commit()

val FragmentActivity.appComponent: AppComponent get() = (application as SFApplication).appComponent

fun Activity.showToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Activity.showError(throwable: Throwable, messageId: Int, action: () -> Unit) {
    action()
    throwable.printStackTrace()
    showToast(throwable.message ?: getString(messageId))
}

fun Fragment.showError(throwable: Throwable, messageId: Int, action: () -> Unit) {
    activity?.showError(throwable, messageId, action)
}
