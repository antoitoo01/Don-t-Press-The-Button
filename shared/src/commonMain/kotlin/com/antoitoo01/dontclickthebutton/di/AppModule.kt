package com.antoitoo01.dontclickthebutton.di

import com.antoitoo01.dontclickthebutton.data.GameSaveDataSource
import com.antoitoo01.dontclickthebutton.data.GameStateRepository
import com.antoitoo01.dontclickthebutton.data.LocalSaveDataSource
import com.antoitoo01.dontclickthebutton.domain.EconomyEngine
import com.antoitoo01.dontclickthebutton.ui.GameViewModel
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val sharedModule = module {
    single { Json { ignoreUnknownKeys = true } }
    single<GameSaveDataSource> { LocalSaveDataSource(get()) }
    single { GameStateRepository(get(), get()) }
    single { EconomyEngine() }
    viewModel { GameViewModel(get(), get()) }
}