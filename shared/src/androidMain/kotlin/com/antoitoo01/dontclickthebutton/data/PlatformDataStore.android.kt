package com.antoitoo01.dontclickthebutton.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import org.koin.dsl.module
import java.io.File

val androidDataStoreModule = module {
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(
            produceFile = {
                val context = get<android.content.Context>()
                File(context.filesDir, "datastore/game_state.preferences_pb")
            }
        )
    }
}