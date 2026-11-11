package com.novaaudio.app

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.novaaudio.app.bluetooth.DeviceListState
import com.novaaudio.app.bluetooth.PairedDeviceReader
import com.novaaudio.app.ui.DevicesScreen
import com.novaaudio.app.ui.HomeScreen
import com.novaaudio.app.ui.NoiseScreen
import com.novaaudio.app.ui.Nova
import com.novaaudio.app.ui.NovaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NovaTheme { NovaApp() }
        }
    }
}

private fun checkPermission(context: Context): Boolean =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        ContextCompat.checkSelfPermission(
            context, Manifest.permission.BLUETOOTH_CONNECT
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }

@Composable
private fun NovaApp() {
    val context = LocalContext.current
    val reader = remember { PairedDeviceReader(context.applicationContext) }
    var tab by rememberSaveable { mutableStateOf(0) }
    var state by remember { mutableStateOf<DeviceListState>(DeviceListState.Idle) }
    var hasPermission by remember { mutableStateOf(checkPermission(context)) }

    fun refresh() {
        hasPermission = checkPermission(context)
        if (hasPermission) reader.read { state = it }
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
        if (granted) reader.read { state = it }
    }

    Scaffold(
        containerColor = Nova.Black,
        bottomBar = {
            NavigationBar(containerColor = Nova.Graphite) {
                NavigationBarItem(
                    selected = tab == 0,
                    onClick = { tab = 0 },
                    icon = { Text("◉") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = tab == 1,
                    onClick = { tab = 1 },
                    icon = { Text("◎") },
                    label = { Text("Noise") }
                )
                NavigationBarItem(
                    selected = tab == 2,
                    onClick = {
                        tab = 2
                        refresh()
                    },
                    icon = { Text("◈") },
                    label = { Text("Devices") }
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (tab) {
                0 -> HomeScreen()
                1 -> NoiseScreen()
                else -> DevicesScreen(
                    hasPermission = hasPermission,
                    state = state,
                    onRequestPermission = {
                        launcher.launch(Manifest.permission.BLUETOOTH_CONNECT)
                    },
                    onRefresh = { refresh() }
                )
            }
        }
    }
}
