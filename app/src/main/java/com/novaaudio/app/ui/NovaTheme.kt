package com.novaaudio.app.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object Nova {
    val Black = Color(0xFF0B0B0F)
    val Graphite = Color(0xFF16161D)
    val GraphiteHigh = Color(0xFF20202A)
    val Cyan = Color(0xFF22D3EE)
    val Violet = Color(0xFFA78BFA)
    val TextPrimary = Color(0xFFF2F2F7)
    val TextSecondary = Color(0xFF9A9AAE)
    val Warning = Color(0xFFFBBF24)
}

private val NovaColors = darkColorScheme(
    primary = Nova.Cyan,
    secondary = Nova.Violet,
    background = Nova.Black,
    surface = Nova.Graphite,
    onPrimary = Nova.Black,
    onSecondary = Nova.Black,
    onBackground = Nova.TextPrimary,
    onSurface = Nova.TextPrimary
)

@Composable
fun NovaTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = NovaColors, content = content)
}

@Composable
fun DemoBadge() {
    Surface(
        color = Nova.Warning.copy(alpha = 0.15f),
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.dp, Nova.Warning)
    ) {
        Text(
            "DEMO DATA - NOT FROM YOUR EARBUDS",
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = Nova.Warning,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun NovaCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Nova.Graphite,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Nova.GraphiteHigh)
    ) {
        Column(modifier = Modifier.padding(16.dp), content = content)
    }
}
