package com.practicum.playlistmaker.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color


val LightColorScheme = lightColorScheme(
    onBackground = Color(0xFF1A1B22), // Светлый режим: тёмный текст на светлом фоне
)

val DarkColorScheme = darkColorScheme(
    onBackground = Color(0xFFFFFFFF), // Тёмный режим: светлый текст на тёмном фоне
)

val LightSwitchThumbColor = Color(0xFF00D6C3)
val LightSwitchTrackColor = Color(0xFF76EAE0)
val DarkSwitchThumbColor = Color(0xFF3772E7)
val DarkSwitchTrackColor = Color(0x7B3772E7)

val LightInactive = Color(0xFFAEAFB4)