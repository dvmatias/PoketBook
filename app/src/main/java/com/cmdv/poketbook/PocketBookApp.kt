package com.cmdv.poketbook

import android.app.Application
import com.cmdv.core.navigator.Navigator
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class PocketBookApp : Application() {

    private lateinit var navigator: Navigator

    override fun onCreate() {
        super.onCreate()
        initKoin()
        initNavigator()
    }

    @Suppress("SpellCheckingInspection")
    private fun initKoin() {
        startKoin {
//            androidLogger() TODO
            androidContext(this@PocketBookApp)
            modules(appModule, repositoryModule, viewModelModule, providerModule, librariesModule)
        }
    }

    private fun initNavigator() {
        navigator = NavigatorImpl()
    }
}