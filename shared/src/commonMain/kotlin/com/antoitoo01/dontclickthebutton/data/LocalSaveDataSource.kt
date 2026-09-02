package com.antoitoo01.dontclickthebutton.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val STATE_KEY = stringPreferencesKey("game_state")

interface GameSaveDataSource {
    val gameState: Flow<String?>
    suspend fun save(state: String)
}

class LocalSaveDataSource(
    private val dataStore: DataStore<Preferences>
) : GameSaveDataSource {
    override val gameState: Flow<String?> = dataStore.data.map { it[STATE_KEY] }

    override suspend fun save(state: String) {
        dataStore.edit { it[STATE_KEY] = state }
    }
}