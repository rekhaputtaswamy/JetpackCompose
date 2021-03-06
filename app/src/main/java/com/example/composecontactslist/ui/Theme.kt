package com.example.composecontactslist.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = teal200,
    primaryVariant = purple700,
    secondary = purple500,
    surface = veryLightGrey,
)

private val LightColorPalette = lightColors(
    primary = lightblue,
    primaryVariant = darkblue,
    secondary = blue,
    surface = lightGrey,
)

@Composable
fun MyTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = MaterialTheme.typography,
        shapes = shapes,
        content = content
    )
}