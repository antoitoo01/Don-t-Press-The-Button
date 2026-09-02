package com.antoitoo01.dontclickthebutton

import android.app.Application
import com.antoitoo01.dontclickthebutton.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MainApplication)
            androidLogger(Level.DEBUG)
        }
    }
}