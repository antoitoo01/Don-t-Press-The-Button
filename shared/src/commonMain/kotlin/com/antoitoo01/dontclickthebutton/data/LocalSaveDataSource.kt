package com.antoitoo01.dontclickthebutton.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val STATE_VERSION = 1
val STATE_KEY = stringPreferencesKey("game_state")

class LocalSaveDataSource(private val dataStore: DataStore<Preferences>) {
    val gameState: Flow<String?> = dataStore.data.map { it[STATE_KEY] }

    suspend fun save(state: String) {
        dataStore.edit { it[STATE_KEY] = state }
    }
}