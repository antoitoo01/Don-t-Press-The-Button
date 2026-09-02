package com.antoitoo01.dontclickthebutton.di

import com.antoitoo01.dontclickthebutton.data.iosDataStoreModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module

actual fun initKoin(config: (KoinApplication.() -> Unit)?): List<Module> {
    startKoin {
        config?.invoke(this)
        modules(iosDataStoreModule + sharedModule)
    }
    return emptyList()
}