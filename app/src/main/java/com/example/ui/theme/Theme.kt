package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = SantotoNavy,
  onPrimary = Color.White,
  primaryContainer = SantotoNavy,
  onPrimaryContainer = Color.White,
  secondary = SantotoYellow,
  onSecondary = SantotoNavy,
  background = SantotoBg,
  onBackground = SantotoTextPrimary,
  surface = Color.White,
  onSurface = SantotoTextPrimary,
  surfaceVariant = SantotoSurfaceVariant,
  onSurfaceVariant = SantotoTextSecondary
)

private val LightColorScheme = lightColorScheme(
  primary = SantotoNavy,
  onPrimary = Color.White,
  primaryContainer = SantotoNavy,
  onPrimaryContainer = Color.White,
  secondary = SantotoYellow,
  onSecondary = SantotoNavy,
  secondaryContainer = SantotoGoldLight,
  onSecondaryContainer = SantotoNavy,
  tertiary = SantotoNavy,
  onTertiary = Color.White,
  background = SantotoBg,
  onBackground = SantotoTextPrimary,
  surface = Color.White,
  onSurface = SantotoTextPrimary,
  surfaceVariant = SantotoSurfaceVariant,
  onSurfaceVariant = SantotoTextSecondary
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = false, // Institutional branding always clean and high-contrast
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

