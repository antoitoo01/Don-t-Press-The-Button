package com.antoitoo01.dontclickthebutton.domain

enum class UpgradeDuration {
    PERMANENT,
    TIMED
}

enum class UpgradeType {
    TAP,
    PER_SECOND,
    MULTIPLIER
}

data class Upgrade(
    val id: String,
    val name: String,
    val description: String,
    val baseCost: Double,
    val costGrowth: Double,
    val type: UpgradeType,
    val baseValue: Double,
    val duration: UpgradeDuration = UpgradeDuration.PERMANENT,
    val activeSeconds: Long? = null,   // si TIMED: duración actividad
    val cooldownSeconds: Long? = null  // si TIMED: duración cooldown
)

val defaultUpgrades: List<Upgrade> = listOf(
    // -- TAP -- //
    Upgrade("stronger_finger", "Dedo Fuerte", "+1 culpa por pulsación", 15.0, 1.15, UpgradeType.TAP, 1.0),
    Upgrade("double_tap", "Doble Pulsación", "+2 culpa por pulsación", 100.0, 1.18, UpgradeType.TAP, 2.0),
    Upgrade("critical_tap", "Pulsación Crítica", "+5 culpa por pulsación", 500.0, 1.20, UpgradeType.TAP, 5.0),
    // -- PER SECOND -- //
    Upgrade("intern", "Becario", "+1 culpa por segundo", 200.0, 1.20, UpgradeType.PER_SECOND, 1.0),
    Upgrade("robot_presser", "Robot Pulsador", "+5 culpa por segundo", 1_000.0, 1.25, UpgradeType.PER_SECOND, 5.0),
    Upgrade("quantum_presser", "Pulsador Cuántico", "+25 culpa por segundo", 10_000.0, 1.30, UpgradeType.PER_SECOND, 25.0),
    // -- MULTIPLIER -- //
    Upgrade("overclock", "Overclock", "×1.5 toda la culpa", 2_500.0, 1.50, UpgradeType.MULTIPLIER, 0.5),
    Upgrade("better_wiring", "Mejor Cableado", "×1.5 toda la culpa", 20_000.0, 1.50, UpgradeType.MULTIPLIER, 0.5)
)