package com.tinderview.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val BrandDark = darkColorScheme(
    primary = Ink.Coral,
    onPrimary = Ink.Cream,
    secondary = Ink.Mint,
    tertiary = Ink.Ice,
    background = Ink.Night,
    surface = Ink.Ink,
    surfaceVariant = Ink.Raised,
    onBackground = Ink.Cream,
    onSurface = Ink.Cream,
    onSurfaceVariant = Ink.CreamMuted,
    outline = Ink.Hairline,
)

@Composable
fun TinderViewTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = BrandDark,
        typography = TinderViewTypography,
        content = content,
    )
}
