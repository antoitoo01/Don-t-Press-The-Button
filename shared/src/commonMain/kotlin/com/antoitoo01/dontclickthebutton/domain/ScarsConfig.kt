package com.antoitoo01.dontclickthebutton.domain

data class ScarsTier(
    val guiltThreshold: Double,
    val scarsAwarded: Int
)

data class SkillUnlockTier(
    val scarsRequired: Int,
    val skillId: String
)

val scarsTiers: List<ScarsTier> = listOf(
    ScarsTier(1_000.0, 1),
    ScarsTier(10_000.0, 2),
    ScarsTier(100_000.0, 3),
    ScarsTier(1_000_000.0, 4),
    ScarsTier(10_000_000.0, 6),
    ScarsTier(1_000_000_000.0, 14)
)

val skillUnlockTiers: List<SkillUnlockTier> = listOf(
    SkillUnlockTier(5, "guilt_doubler")
)