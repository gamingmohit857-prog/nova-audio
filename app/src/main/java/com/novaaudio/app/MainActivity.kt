package com.novaaudio.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.novaaudio.app.ui.HomeScreen
import com.novaaudio.app.ui.Nova
import com.novaaudio.app.ui.NoiseScreen
import com.novaaudio.app.ui.NovaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NovaTheme { NovaApp() }
        }
    }
}

@Composable
private fun NovaApp() {
    var tab by rememberSaveable { mutableStateOf(0) }
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
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (tab == 0) HomeScreen() else NoiseScreen()
        }
    }
}
