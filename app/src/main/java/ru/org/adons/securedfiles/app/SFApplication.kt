package ru.org.adons.securedfiles.app

import android.app.Application

/**
 * Instance of Application, provide Singleton dependencies via [AppComponent]
 */
class SFApplication : Application() {

    val appComponent: AppComponent by lazy { DaggerAppComponent.builder()
            .appModule(AppModule(applicationContext))
            .build() }

}