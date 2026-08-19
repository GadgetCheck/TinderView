package com.tinderview.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColors = darkColorScheme(
    primary = Color(0xFFFF6B6B),
    onPrimary = Color.White,
    secondary = Color(0xFF2ECC71),
    tertiary = Color(0xFF3498DB),
    background = Color(0xFF101114),
    surface = Color(0xFF1A1C1F),
    surfaceVariant = Color(0x3324282C),
    onBackground = Color(0xFFF4F1EA),
    onSurface = Color(0xFFF4F1EA),
)

private val LightColors = lightColorScheme(
    primary = Color(0xFFE94E4E),
    onPrimary = Color.White,
    secondary = Color(0xFF1F9D55),
    tertiary = Color(0xFF2B79C2),
    background = Color(0xFFF6F3EE),
    surface = Color.White,
    onBackground = Color(0xFF1A1C1F),
    onSurface = Color(0xFF1A1C1F),
)

@Composable
fun TinderViewTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val colors = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && darkTheme ->
            dynamicDarkColorScheme(context)
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
            dynamicLightColorScheme(context)
        darkTheme -> DarkColors
        else -> LightColors
    }
    MaterialTheme(
        colorScheme = colors.copy(
            primary = if (darkTheme) DarkColors.primary else LightColors.primary,
        ),
        content = content,
    )
}
