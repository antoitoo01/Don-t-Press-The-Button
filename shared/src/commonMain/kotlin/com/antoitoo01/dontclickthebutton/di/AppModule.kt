package com.antoitoo01.dontclickthebutton.di

import com.antoitoo01.dontclickthebutton.data.GameStateRepository
import com.antoitoo01.dontclickthebutton.data.LocalSaveDataSource
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val sharedModule = module {
    val sharedModule = module {
        single { Json { ignoreUnknownKeys = true } }
        single { LocalSaveDataSource(get()) }
        single { GameStateRepository(get(), get()) }   // LocalSaveDataSource + Json
    }
}