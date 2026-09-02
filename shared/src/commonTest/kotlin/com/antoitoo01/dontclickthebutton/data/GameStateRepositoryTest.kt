package com.antoitoo01.dontclickthebutton.data


import com.antoitoo01.dontclickthebutton.model.GameState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals


class GameStateRepositoryTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun parse_null_returnsInitial() = runTest {
        val fake = FakeGameSaveDataSource(initialValue = null)
        val repo = GameStateRepository(fake, json)

        repo.gameState.first().let { state ->
            assertEquals(GameState.initial(), state)
        }
    }

    @Test
    fun parse_validJson_returnsDeserializedState() = runTest {
        val original = GameState.initial().copy(guilt = 42.0, totalTaps = 10L)
        val fake = FakeGameSaveDataSource(initialValue = json.encodeToString(original))
        val repo = GameStateRepository(fake, json)

        repo.gameState.first().let { state ->
            assertEquals(42.0, state.guilt)
            assertEquals(10L, state.totalTaps)
        }
    }

    @Test
    fun parse_invalidJson_returnsInitial() = runTest {
        val fake = FakeGameSaveDataSource(initialValue = "NOT_JSON{{}}")
        val repo = GameStateRepository(fake, json)

        repo.gameState.first().let { state ->
            assertEquals(GameState.initial(), state)
        }
    }

    @Test
    fun parse_wrongSchema_returnsInitial() = runTest {
        val old = GameState.initial().copy(schemaVersion = 999)
        val fake = FakeGameSaveDataSource(initialValue = json.encodeToString(old))
        val repo = GameStateRepository(fake, json)

        repo.gameState.first().let { state ->
            assertEquals(GameState.initial(), state)
        }
    }

    @Test
    fun save_serializesCorrectly() = runTest {
        val fake = FakeGameSaveDataSource()
        val repo = GameStateRepository(fake, json)
        val state = GameState.initial().copy(guilt = 99.0)

        repo.save(state)

        val decoded = json.decodeFromString<GameState>(fake.lastSaved!!)
        assertEquals(99.0, decoded.guilt)
    }
}