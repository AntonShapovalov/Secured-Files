package ru.org.adons.securedfiles.app

import android.content.Context
import dagger.Component
import ru.org.adons.securedfiles.ui.main.MainActivity
import javax.inject.Singleton

/**
 * Provide Application scope dependencies
 */
@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {

    fun context(): Context

    fun inject(mainActivity: MainActivity)

}