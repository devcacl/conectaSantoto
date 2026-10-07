package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
  primary = SantotoGold,
  onPrimary = SantotoNavyDark,
  primaryContainer = SantotoRoyal,
  onPrimaryContainer = SantotoGoldLight,
  secondary = SantotoCyan,
  onSecondary = SantotoNavyDark,
  background = SantotoNavyDark,
  onBackground = SantotoSurface,
  surface = SantotoCardNavy,
  onSurface = SantotoSurface,
  surfaceVariant = SantotoRoyal,
  onSurfaceVariant = SantotoSurfaceVariant
)

private val LightColorScheme = lightColorScheme(
  primary = SantotoNavy,
  onPrimary = SantotoSurface,
  primaryContainer = SantotoRoyal,
  onPrimaryContainer = SantotoSurface,
  secondary = SantotoGold,
  onSecondary = SantotoNavyDark,
  secondaryContainer = SantotoGoldLight,
  onSecondaryContainer = SantotoNavyDark,
  tertiary = SantotoCyan,
  onTertiary = SantotoSurface,
  background = SantotoBg,
  onBackground = SantotoTextPrimary,
  surface = SantotoSurface,
  onSurface = SantotoTextPrimary,
  surfaceVariant = SantotoSurfaceVariant,
  onSurfaceVariant = SantotoTextSecondary
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Keep institutional branding consistent
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
