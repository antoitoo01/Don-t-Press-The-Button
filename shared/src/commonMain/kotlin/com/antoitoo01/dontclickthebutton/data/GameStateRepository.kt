package com.antoitoo01.dontclickthebutton.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.antoitoo01.dontclickthebutton.model.GameState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class GameStateRepository(
    private val localSave: GameSaveDataSource,
    private val json: Json
) {
    // Flow que NUNCA emite null:
    // null/JSON inválido/schema viejo → GameState.initial()
    val gameState: Flow<GameState> = localSave.gameState.map { raw ->
        parse(raw)
    }

    suspend fun save(state: GameState) {
        localSave.save(json.encodeToString(state))
    }

    private fun parse(raw: String?): GameState {
        if (raw == null) return GameState.initial()
        return try {
            val decoded = json.decodeFromString<GameState>(raw)
            if (decoded.schemaVersion != GameState.CURRENT_SCHEMA_VERSION) GameState.initial()
            else decoded
        } catch (e: Exception) {
            GameState.initial()
        }
    }
}