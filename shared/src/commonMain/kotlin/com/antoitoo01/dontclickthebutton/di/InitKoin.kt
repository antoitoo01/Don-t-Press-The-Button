package com.antoitoo01.dontclickthebutton.di

import org.koin.core.KoinApplication
import org.koin.core.module.Module

expect fun initKoin(config: (KoinApplication.() -> Unit)? = null): List<Module>
