package com.antoitoo01.dontclickthebutton.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.round

@Composable
fun GameScreen(viewModel: GameViewModel) {
    val state by viewModel.state.collectAsState()

    // Timer: tick cada 1 segundo
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            viewModel.onTick(1000L)
        }
    }

    // Guardar al salir (Lifecycle)
    DisposableEffect(Unit) {
        onDispose { viewModel.onSave() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header: Culpa
        Text(
            text = "Culpa",
            style = MaterialTheme.typography.labelLarge
        )
        Text(
            text = formatNumber(state.guilt),
            style = MaterialTheme.typography.displayLarge
        )
        Text(
            text = "×${formatNumber(state.multiplier)}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.weight(1f))

        // Botón principal
        GameButton(
            onTap = { viewModel.onButtonPressed() },
            guiltPerTap = state.guiltPerTap * state.multiplier
        )

        Spacer(modifier = Modifier.weight(1f))

        // Stats
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatItem("Taps", formatNumber(state.totalTaps.toDouble()))
            StatItem("Culpa/s", formatNumber(state.guiltPerSecond))
            StatItem("Cicatrices", "${state.scars}")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Upgrade shop
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.upgrades) { upgrade ->
                UpgradeCard(
                    upgrade = upgrade,
                    onBuy = { viewModel.onBuyUpgrade(upgrade.id) }
                )
            }
        }
    }
}

@Composable
private fun GameButton(onTap: () -> Unit, guiltPerTap: Double) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (pressed) 0.9f else 1f)

    Button(
        onClick = {
            pressed = true
            onTap()
            pressed = false
        },
        modifier = Modifier.size(200.dp),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("NO", fontSize = 32.sp)
            Text("PULSES", fontSize = 24.sp)
            Text("+$guiltPerTap", fontSize = 14.sp)
        }
    }
}

@Composable
private fun UpgradeCard(upgrade: UpgradeUiState, onBuy: () -> Unit) {
    Card(
        onClick = { if (upgrade.affordable) onBuy() },
        modifier = Modifier.fillMaxWidth(),
        enabled = upgrade.affordable
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(upgrade.name, style = MaterialTheme.typography.titleSmall)
                Text(upgrade.description, style = MaterialTheme.typography.bodySmall)
                Text("Nivel ${upgrade.currentLevel}", style = MaterialTheme.typography.labelSmall)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(formatNumber(upgrade.cost), style = MaterialTheme.typography.titleMedium)
                if (upgrade.maxAffordable >= 10) {
                    Text("Comprar ${upgrade.maxAffordable}", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleMedium)
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}

private fun formatNumber(value: Double): String {
    return when {
        value >= 1_000_000_000 -> round(value / 1_000_000_000, 1) + "B"
        value >= 1_000_000 -> round(value / 1_000_000, 1) + "M"
        value >= 1_000 -> round(value / 1_000, 1) + "K"
        value >= 100 -> round(value, 0)
        else -> round(value, 1)
    }
}

private fun round(value: Double, decimals: Int): String {
    val factor = listOf(1.0, 10.0, 100.0, 1000.0)[decimals]
    val shifted = kotlin.math.floor(value * factor + 0.5) / factor
    val str = shifted.toString()
    val dotIndex = str.indexOf('.')
    return if (decimals == 0) {
        if (dotIndex >= 0) str.substring(0, dotIndex) else str
    } else {
        if (dotIndex >= 0) {
            str.substring(0, (dotIndex + 1 + decimals).coerceAtMost(str.length))
        } else {
            str + "." + "0".repeat(decimals)
        }
    }
}

private fun formatNumber(value: Long): String = formatNumber(value.toDouble())