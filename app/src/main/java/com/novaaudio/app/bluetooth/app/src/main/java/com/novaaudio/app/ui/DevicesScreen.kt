package com.novaaudio.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novaaudio.app.bluetooth.DeviceListState
import com.novaaudio.app.bluetooth.PairedDevice

@Composable
fun DevicesScreen(
    hasPermission: Boolean,
    state: DeviceListState,
    onRequestPermission: () -> Unit,
    onRefresh: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Devices", color = Nova.Cyan, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text(
            "Real data from your phone's Bluetooth. Read-only: nothing is sent to your earbuds.",
            color = Nova.TextSecondary,
            fontSize = 13.sp
        )

        if (!hasPermission) {
            NovaCard {
                Text("Permission needed", color = Nova.TextPrimary, fontSize = 18.sp)
                Spacer(Modifier.height(6.dp))
                Text(
                    "NOVA AUDIO needs the Nearby devices permission to see which earbuds are paired. It only reads names and connection state.",
                    color = Nova.TextSecondary,
                    fontSize = 13.sp
                )
                Spacer(Modifier.height(12.dp))
                Button(onClick = onRequestPermission) { Text("Allow") }
            }
        } else {
            Button(onClick = onRefresh) { Text("Refresh") }
            when (state) {
                DeviceListState.Idle -> Text("Tap Refresh to load your paired devices.", color = Nova.TextSecondary)
                DeviceListState.BluetoothUnavailable -> Text("This phone has no Bluetooth.", color = Nova.Warning)
                DeviceListState.BluetoothOff -> Text("Bluetooth is off. Turn it on, then tap Refresh.", color = Nova.Warning)
                is DeviceListState.Failed -> Text(state.message, color = Nova.Warning)
                is DeviceListState.Ready -> {
                    if (state.devices.isEmpty()) {
                        Text("No paired devices found.", color = Nova.TextSecondary)
                    }
                    state.devices.forEach { DeviceCard(it) }
                }
            }
        }
    }
}

@Composable
private fun DeviceCard(device: PairedDevice) {
    val likely = device.name.contains("nirvana", ignoreCase = true)
    NovaCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(device.name, color = Nova.TextPrimary, fontSize = 16.sp, modifier = Modifier.weight(1f))
            Text(
                if (device.isConnected) "Connected" else "Not connected",
                color = if (device.isConnected) Nova.Cyan else Nova.TextSecondary,
                fontSize = 13.sp
            )
        }
        Spacer(Modifier.height(4.dp))
        Text("Address ending ${device.address.takeLast(5)}", color = Nova.TextSecondary, fontSize = 12.sp)
        if (likely) {
            Spacer(Modifier.height(4.dp))
            Text("Looks like your Nirvana Ion ANC", color = Nova.Violet, fontSize = 12.sp)
        }
    }
}
