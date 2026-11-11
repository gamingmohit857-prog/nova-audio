package com.novaaudio.app.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("NOVA AUDIO", color = Nova.Cyan, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("boAt Nirvana Ion ANC", color = Nova.TextSecondary, fontSize = 14.sp)
        DemoBadge()

        NovaCard {
            Text("Connection", color = Nova.TextSecondary, fontSize = 13.sp)
            Text("Not connected (prototype)", color = Nova.TextPrimary, fontSize = 18.sp)
            Spacer(Modifier.height(4.dp))
            Text(
                "Real connection arrives in Stage 3.",
                color = Nova.TextSecondary,
                fontSize = 13.sp
            )
        }

        NovaCard {
            Text("Battery (demo values)", color = Nova.TextSecondary, fontSize = 13.sp)
            Spacer(Modifier.height(12.dp))
            BatteryRow("Left bud", 0.82f)
            Spacer(Modifier.height(10.dp))
            BatteryRow("Right bud", 0.79f)
            Spacer(Modifier.height(10.dp))
            BatteryRow("Case", 0.64f)
        }
    }
}

@Composable
private fun BatteryRow(label: String, level: Float) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, color = Nova.TextPrimary, fontSize = 15.sp)
            Text("${(level * 100).toInt()}% (demo)", color = Nova.Violet, fontSize = 15.sp)
        }
        Spacer(Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { level },
            modifier = Modifier.fillMaxWidth().height(6.dp),
            color = Nova.Cyan,
            trackColor = Nova.GraphiteHigh
        )
    }
}

@Composable
fun NoiseScreen() {
    val modes = listOf("Noise cancelling", "Ambient", "Normal")
    var mode by remember { mutableStateOf(modes[0]) }
    var beast by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Noise control", color = Nova.Cyan, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        DemoBadge()

        NovaCard {
            Text(
                "Prototype only. These buttons do not control your earbuds.",
                color = Nova.Warning,
                fontSize = 13.sp
            )
        }

        NovaCard {
            Text("Mode", color = Nova.TextSecondary, fontSize = 13.sp)
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                modes.forEach { m ->
                    val selected = m == mode
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .selectable(
                                selected = selected,
                                onClick = { mode = m },
                                role = Role.RadioButton
                            ),
                        shape = RoundedCornerShape(14.dp),
                        color = if (selected) Nova.Cyan.copy(alpha = 0.15f) else Nova.GraphiteHigh,
                        border = BorderStroke(1.dp, if (selected) Nova.Cyan else Color.Transparent)
                    ) {
                        Text(
                            m,
                            modifier = Modifier.padding(vertical = 14.dp, horizontal = 6.dp),
                            color = if (selected) Nova.Cyan else Nova.TextPrimary,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        NovaCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("BEAST mode", color = Nova.TextPrimary, fontSize = 16.sp)
                    Text("Low-latency gaming (demo switch)", color = Nova.TextSecondary, fontSize = 12.sp)
                }
                Switch(checked = beast, onCheckedChange = { beast = it })
            }
        }
    }
}
