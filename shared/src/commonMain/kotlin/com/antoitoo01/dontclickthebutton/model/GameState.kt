package com.antoitoo01.dontclickthebutton.model

import com.antoitoo01.dontclickthebutton.data.STATE_VERSION
import kotlinx.serialization.Serializable
import kotlin.collections.emptyMap
import kotlin.collections.emptySet

@Serializable
data class GameState(
    // Formato / migración
    val schemaVersion: Int,

    // == CULPA (la moneda visible) ==
    val guilt: Double,               // culpa actual (visible)
    val guiltPerTap: Double,         // +culpa por pulso (mejoras runa-local)
    val guiltPerSecond: Double,      // automatización (mejoras runa-local)

    // == Historial de culpa (para prestigio) ==
    val accumulatedGuilt: Double,    // culpa total jamás acumulada (nunca baja) → fuente de Cicatrices

    // == CICATRICES (moneda permanente de prestigio) ==
    val scars: Int,                  // cicatrices totales acumuladas

    // == HABILIDADES PERMANENTES (desbloqueadas con Cicatrices) ==
    val unlockedSkills: Set<SkillId>,     // habilidades que ya tienes
    val skillCooldowns: Map<SkillId, Long>, // timestamp de último uso (para cooldown)

    // == Métricas / utilitarias ==
    val totalTaps: Long,
    val totalGuilt: Double,
    val lastSeenEpochMs: Long
) {


    companion object {
        fun initial(): GameState = GameState(
            schemaVersion = STATE_VERSION,
            guilt = 0.0,
            guiltPerTap = 1.0,          // ← CORREGIDO: +1 culpa por tap
            guiltPerSecond = 0.0,       // sin automatización al inicio
            accumulatedGuilt = 0.0,
            totalGuilt = 0.0,
            scars = 0,
            unlockedSkills = emptySet(),
            skillCooldowns = emptyMap(),
            totalTaps = 0L,
            lastSeenEpochMs = 0L
        )
    }

}