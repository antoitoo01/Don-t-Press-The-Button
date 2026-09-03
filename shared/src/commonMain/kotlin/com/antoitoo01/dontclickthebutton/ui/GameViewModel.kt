package com.antoitoo01.dontclickthebutton.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antoitoo01.dontclickthebutton.data.GameStateRepository
import com.antoitoo01.dontclickthebutton.domain.EconomyEngine
import com.antoitoo01.dontclickthebutton.domain.Upgrade
import com.antoitoo01.dontclickthebutton.domain.defaultUpgrades
import com.antoitoo01.dontclickthebutton.model.GameState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GameUiState(
    val guilt: Double = 0.0,
    val guiltPerTap: Double = 1.0,
    val guiltPerSecond: Double = 0.0,
    val multiplier: Double = 1.0,
    val scars: Int = 0,
    val totalTaps: Long = 0L,
    val totalGuilt: Double = 0.0,
    val upgrades: List<UpgradeUiState> = emptyList()
)

data class UpgradeUiState(
    val id: String,
    val name: String,
    val description: String,
    val cost: Double,
    val currentLevel: Int,
    val affordable: Boolean,
    val maxAffordable: Int
)

class GameViewModel(
    private val repository: GameStateRepository,
    private val engine: EconomyEngine
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private var currentState: GameState = GameState.initial()

    init {
        viewModelScope.launch {
            currentState = repository.gameState.first()
            emitUiState()
        }
    }

    fun onButtonPressed() {
        val nowMs = currentTimeMs()
        currentState = engine.press(currentState, nowMs)
        emitUiState()
    }

    fun onBuyUpgrade(upgradeId: String) {
        val upgrade = defaultUpgrades.firstOrNull { it.id == upgradeId } ?: return
        currentState = engine.buyUpgrade(currentState, upgrade)
        emitUiState()
    }

    fun onTick(elapsedMs: Long) {
        val nowMs = currentTimeMs()
        currentState = engine.tick(currentState, nowMs, elapsedMs)
        emitUiState()
    }

    fun onSave() {
        viewModelScope.launch {
            repository.save(currentState)
        }
    }

    private fun emitUiState() {
        _state.update {
            GameUiState(
                guilt = currentState.guilt,
                guiltPerTap = currentState.guiltPerTap,
                guiltPerSecond = currentState.guiltPerSecond,
                multiplier = currentState.multiplier,
                scars = currentState.scars,
                totalTaps = currentState.totalTaps,
                totalGuilt = currentState.totalGuilt,
                upgrades = defaultUpgrades.map { upgrade ->
                    val level = currentState.ownedUpgrades[upgrade.id] ?: 0
                    val cost = engine.getUpgradeCost(upgrade, level)
                    val affordable = currentState.guilt >= cost
                    val maxAffordable = engine.maxAffordable(currentState, upgrade)
                    UpgradeUiState(
                        id = upgrade.id,
                        name = upgrade.name,
                        description = upgrade.description,
                        cost = cost,
                        currentLevel = level,
                        affordable = affordable,
                        maxAffordable = maxAffordable
                    )
                }
            )
        }
    }

    // Multiplatform: expect/actual for currentTimeMs
    private fun currentTimeMs(): Long = currentTimeMillis()
}

