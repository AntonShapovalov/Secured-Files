package mobile.addons.securedfiles.app

import android.content.Context
import dagger.Component
import mobile.addons.securedfiles.file.FileManager
import mobile.addons.securedfiles.ui.add.AddViewModel
import mobile.addons.securedfiles.ui.main.MainViewModel
import javax.inject.Singleton

/**
 * Provide Application scope dependencies
 */
@Singleton
@Component(modules = [(AppModule::class)])
interface AppComponent {

    fun context(): Context

    fun fileManager(): FileManager

    fun inject(mainViewModel: MainViewModel)

    fun inject(addViewModel: AddViewModel)

}