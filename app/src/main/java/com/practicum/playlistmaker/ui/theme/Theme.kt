package com.practicum.playlistmaker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.practicum.playlistmaker.ui.AppTypography

@Composable
fun YsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    typography: Typography = AppTypography,
    content: @Composable () -> Unit
) {
    val colorScheme = when (darkTheme) {
        true -> DarkColorScheme
        false -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}

@Composable
fun getSwitchColors(darkTheme: Boolean): SwitchColors {
    return SwitchDefaults.colors(
        checkedThumbColor = if (darkTheme) DarkSwitchThumbColor else LightSwitchThumbColor,
        checkedTrackColor = if (darkTheme) DarkSwitchTrackColor else LightSwitchTrackColor,
        uncheckedThumbColor = if (darkTheme) DarkSwitchThumbColor else LightSwitchThumbColor,
        uncheckedTrackColor = if (darkTheme) DarkSwitchTrackColor else LightSwitchTrackColor,
        checkedBorderColor = Color.Transparent,
        uncheckedBorderColor = Color.Transparent,
        disabledCheckedBorderColor = Color.Transparent,
        disabledUncheckedBorderColor = Color.Transparent
    )
}

@Composable
fun getSettingsIconColor(darkTheme: Boolean): Color {
    val onLightColor = LightInactive
    val onDarkColor = MaterialTheme.colorScheme.onSurface
    return if (darkTheme) onDarkColor else onLightColor
}