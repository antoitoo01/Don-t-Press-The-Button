package com.antoitoo01.dontclickthebutton.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeGameSaveDataSource(initialValue: String? = null) : GameSaveDataSource {
    private val _state = MutableStateFlow(initialValue)
    override val gameState: Flow<String?> = _state

    var lastSaved: String? = null
        private set

    fun emit(value: String?) { _state.value = value }

    override suspend fun save(state: String) { lastSaved = state }
}