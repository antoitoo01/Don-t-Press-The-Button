package com.antoitoo01.dontclickthebutton.domain

import com.antoitoo01.dontclickthebutton.model.GameState
import kotlin.math.pow

class EconomyEngine {

    fun press(state: GameState, nowMs: Long): GameState {
        val gain = state.guiltPerTap * state.multiplier
        return state.copy(
            guilt = state.guilt + gain,
            accumulatedGuilt = state.accumulatedGuilt + gain,
            totalGuilt = state.totalGuilt + gain,
            totalTaps = state.totalTaps + 1,
            lastSeenEpochMs = nowMs
        )
    }

    fun tick(state: GameState, nowMs: Long, elapsedMs: Long): GameState {
        if (state.guiltPerSecond <= 0.0 || elapsedMs <= 0) {
            return state.copy(lastSeenEpochMs = nowMs)
        }
        val gain = state.guiltPerSecond * state.multiplier * (elapsedMs / 1000.0)
        return state.copy(
            guilt = state.guilt + gain,
            accumulatedGuilt = state.accumulatedGuilt + gain,
            totalGuilt = state.totalGuilt + gain,
            lastSeenEpochMs = nowMs
        )
    }

    fun getUpgradeCost(upgrade: Upgrade, currentLevel: Int): Double =
        upgrade.baseCost * upgrade.costGrowth.pow(currentLevel)

    fun buyUpgrade(state: GameState, upgrade: Upgrade): GameState {
        val currentLevel = state.ownedUpgrades[upgrade.id] ?: 0
        val cost = getUpgradeCost(upgrade, currentLevel)
        if (state.guilt < cost) return state
        return state.copy(
            guilt = state.guilt - cost,
            ownedUpgrades = state.ownedUpgrades + (upgrade.id to currentLevel + 1),
            guiltPerTap = if (upgrade.type == UpgradeType.TAP) state.guiltPerTap + upgrade.baseValue else state.guiltPerTap,
            guiltPerSecond = if (upgrade.type == UpgradeType.PER_SECOND) state.guiltPerSecond + upgrade.baseValue else state.guiltPerSecond,
            multiplier = if (upgrade.type == UpgradeType.MULTIPLIER) state.multiplier + upgrade.baseValue else state.multiplier
        )
    }

    fun buyMaxUpgrades(state: GameState, upgrade: Upgrade): GameState {
        var currentState = state
        while (true) {
            val currentLevel = currentState.ownedUpgrades[upgrade.id] ?: 0
            val cost = getUpgradeCost(upgrade, currentLevel)
            if (currentState.guilt < cost) break
            currentState = currentState.copy(
                guilt = currentState.guilt - cost,
                ownedUpgrades = currentState.ownedUpgrades + (upgrade.id to currentLevel + 1),
                guiltPerTap = if (upgrade.type == UpgradeType.TAP) currentState.guiltPerTap + upgrade.baseValue else currentState.guiltPerTap,
                guiltPerSecond = if (upgrade.type == UpgradeType.PER_SECOND) currentState.guiltPerSecond + upgrade.baseValue else currentState.guiltPerSecond,
                multiplier = if (upgrade.type == UpgradeType.MULTIPLIER) currentState.multiplier + upgrade.baseValue else currentState.multiplier
            )
        }
        return currentState
    }

    fun maxAffordable(state: GameState, upgrade: Upgrade): Int {
        var count = 0
        var guilt = state.guilt
        var level = state.ownedUpgrades[upgrade.id] ?: 0
        while (true) {
            val cost = getUpgradeCost(upgrade, level)
            if (guilt < cost) break
            guilt -= cost
            level++
            count++
        }
        return count
    }

    fun scarsFromGuilt(accumulatedGuilt: Double): Int {
        var total = 0
        for (tier in scarsTiers) {
            if (accumulatedGuilt >= tier.guiltThreshold) total += tier.scarsAwarded
        }
        return total
    }

    fun skillsUnlockedFor(scars: Int): Set<String> =
        skillUnlockTiers.filter { scars >= it.scarsRequired }.map { it.skillId }.toSet()

    fun prestige(state: GameState, nowMs: Long): GameState = state.copy(
        guilt = 0.0,
        guiltPerTap = 1.0,
        guiltPerSecond = 0.0,
        multiplier = 1.0,
        accumulatedGuilt = 0.0,
        scars = state.scars + scarsFromGuilt(state.accumulatedGuilt),
        ownedUpgrades = emptyMap(),
        lastSeenEpochMs = nowMs
    )
}