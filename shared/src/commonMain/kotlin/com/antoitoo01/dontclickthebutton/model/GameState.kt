package com.antoitoo01.dontclickthebutton.model

import com.antoitoo01.dontclickthebutton.data.STATE_VERSION
import kotlinx.serialization.Serializable
import kotlin.collections.emptyMap
import kotlin.collections.emptySet


@Serializable
data class GameState(
    val schemaVersion: Int,
    val guilt: Double,
    val guiltPerTap: Double,
    val guiltPerSecond: Double,
    val multiplier: Double,
    val accumulatedGuilt: Double,
    val totalGuilt: Double,
    val scars: Int,
    val ownedUpgrades: Map<String, Int>,
    val unlockedSkills: Set<String>,
    val skillCooldowns: Map<String, Long>,
    val totalTaps: Long,
    val lastSeenEpochMs: Long
) {
    companion object {
        const val CURRENT_SCHEMA_VERSION = 1

        fun initial(): GameState = GameState(
            schemaVersion = CURRENT_SCHEMA_VERSION,
            guilt = 0.0,
            guiltPerTap = 1.0,
            guiltPerSecond = 0.0,
            multiplier = 1.0,
            accumulatedGuilt = 0.0,
            totalGuilt = 0.0,
            scars = 0,
            ownedUpgrades = emptyMap(),
            unlockedSkills = emptySet(),
            skillCooldowns = emptyMap(),
            totalTaps = 0L,
            lastSeenEpochMs = 0L
        )
    }
}