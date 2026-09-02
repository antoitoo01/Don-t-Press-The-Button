package com.antoitoo01.dontclickthebutton.domain

import com.antoitoo01.dontclickthebutton.model.GameState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class EconomyEngineTest {

    @Test
    fun press_incrementsGuiltByGuiltPerTapTimesMultiplier() {
        val engine = EconomyEngine()
        val initial = GameState.initial()  // guiltPerTap=1.0, multiplier=1.0
        val result = engine.press(initial, nowMs = 1000L)

        assertEquals(1.0, result.guilt)           // 1.0 × 1.0 = 1
        assertEquals(1.0, result.accumulatedGuilt)
        assertEquals(1.0, result.totalGuilt)
        assertEquals(1L, result.totalTaps)
        assertEquals(1000L, result.lastSeenEpochMs)
    }

    @Test
    fun press_appliesMultiplier() {
        val engine = EconomyEngine()
        val state = GameState.initial().copy(multiplier = 2.0, guiltPerTap = 3.0)
        val result = engine.press(state, nowMs = 1000L)

        assertEquals(6.0, result.guilt)  // 3.0 × 2.0 = 6
    }

    @Test
    fun buyUpgrade_reducesGuiltAndAppliesEffect() {
        val engine = EconomyEngine()
        val upgrade = defaultUpgrades.first { it.id == "stronger_finger" }  // baseCost=15, +1 TAP
        val state = GameState.initial().copy(guilt = 100.0)
        val result = engine.buyUpgrade(state, upgrade)

        assertEquals(100.0 - 15.0, result.guilt)  // gastó 15
        assertEquals(2.0, result.guiltPerTap)      // 1.0 + 1.0
        assertEquals(1, result.ownedUpgrades["stronger_finger"])
    }

    @Test
    fun buyUpgrade_doesNotBuyIfCannotAfford() {
        val engine = EconomyEngine()
        val upgrade = defaultUpgrades.first { it.id == "stronger_finger" }
        val state = GameState.initial().copy(guilt = 10.0)  // menos de 15
        val result = engine.buyUpgrade(state, upgrade)

        assertEquals(10.0, result.guilt)  // no cambió
        assertEquals(1.0, result.guiltPerTap)  // no cambió
        assertEquals(null, result.ownedUpgrades["stronger_finger"])
    }

    @Test
    fun getUpgradeCost_scalesExponentially() {
        val upgrade = defaultUpgrades.first { it.id == "stronger_finger" }
        val engine = EconomyEngine()

        assertEquals(15.0, engine.getUpgradeCost(upgrade, 0))       // 15 × 1.15^0
        assertEquals(15.0 * 1.15, engine.getUpgradeCost(upgrade, 1), 0.01)  // 15 × 1.15^1
        assertEquals(15.0 * 1.15 * 1.15, engine.getUpgradeCost(upgrade, 2), 0.01)  // 15 × 1.15^2
    }

    @Test
    fun maxAffordable_countsHowManyYouCanBuy() {
        val engine = EconomyEngine()
        val upgrade = defaultUpgrades.first { it.id == "stronger_finger" }
        val state = GameState.initial().copy(guilt = 50.0)

        val count = engine.maxAffordable(state, upgrade)
        assertTrue(count >= 2)  // con 50 culp, puedes comprar al menos 2 (15 + 17.25 = 32.25)
    }

    @Test
    fun scarsFromGuilt_returnsCorrectTiers() {
        val engine = EconomyEngine()

        assertEquals(0, engine.scarsFromGuilt(0.0))
        assertEquals(0, engine.scarsFromGuilt(500.0))
        assertEquals(1, engine.scarsFromGuilt(1_000.0))
        assertEquals(3, engine.scarsFromGuilt(10_000.0))
        assertEquals(6, engine.scarsFromGuilt(100_000.0))
    }

    @Test
    fun prestige_resetsCorrectlyAndAddsScars() {
        val engine = EconomyEngine()
        val state = GameState.initial().copy(
            guilt = 5_000.0,
            accumulatedGuilt = 5_000.0,
            guiltPerTap = 5.0,
            guiltPerSecond = 3.0,
            multiplier = 2.0,
            ownedUpgrades = mapOf("stronger_finger" to 3),
            totalTaps = 500L,
            totalGuilt = 10_000.0
        )
        val result = engine.prestige(state, nowMs = 9999L)

        assertEquals(0.0, result.guilt)
        assertEquals(1.0, result.guiltPerTap)
        assertEquals(0.0, result.guiltPerSecond)
        assertEquals(1.0, result.multiplier)
        assertEquals(0.0, result.accumulatedGuilt)
        assertEquals(emptyMap(), result.ownedUpgrades)
        assertEquals(500L, result.totalTaps)        // NO se resetea
        assertEquals(10_000.0, result.totalGuilt)    // NO se resetea
        assertTrue(result.scars > 0)                 // ganó cicatrices
    }

}

