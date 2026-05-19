package me.rafa.arias.parksmart.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ── Colores ──
object ParkSmartColors {
    var Primary       by mutableStateOf(Color(0xFF90C749))
    var PrimaryLight  by mutableStateOf(Color(0xFFF2F9E8))
    var PrimaryBorder by mutableStateOf(Color(0xFFC8E6A0))
    var PrimaryDark   by mutableStateOf(Color(0xFF6FA832))
    var Background    by mutableStateOf(Color(0xFFF4F7F0))
    var HeaderGreen   by mutableStateOf(Color(0xFF90C749))

    val Surface        = Color(0xFFFFFFFF)
    val TextPrimary    = Color(0xFF2D3142)
    val TextSecondary  = Color(0xFF9E9E9E)
    val Error          = Color(0xFFE53935)
    val ErrorLight     = Color(0xFFFFEBEE)
    val Divider        = Color(0xFFEEEEEE)
    val White          = Color(0xFFFFFFFF)
    val Black          = Color(0xFF000000)
    val CameraBackground = Color(0xFF262626)
}

fun applyBrandColor(hex: String) {
    try {
        val argb = android.graphics.Color.parseColor(hex)
        val base = Color(argb)
        val r = base.red; val g = base.green; val b = base.blue
        ParkSmartColors.Primary       = base
        ParkSmartColors.PrimaryDark   = Color((r * 0.78f).coerceIn(0f,1f), (g * 0.78f).coerceIn(0f,1f), (b * 0.78f).coerceIn(0f,1f))
        ParkSmartColors.PrimaryLight  = Color((r * 0.12f + 0.88f).coerceIn(0f,1f), (g * 0.12f + 0.88f).coerceIn(0f,1f), (b * 0.12f + 0.88f).coerceIn(0f,1f))
        ParkSmartColors.PrimaryBorder = Color((r * 0.45f + 0.55f).coerceIn(0f,1f), (g * 0.45f + 0.55f).coerceIn(0f,1f), (b * 0.45f + 0.55f).coerceIn(0f,1f))
        ParkSmartColors.Background    = Color((r * 0.05f + 0.93f).coerceIn(0f,1f), (g * 0.05f + 0.93f).coerceIn(0f,1f), (b * 0.05f + 0.93f).coerceIn(0f,1f))
        ParkSmartColors.HeaderGreen   = base
    } catch (_: Exception) {}
}

// ── Tipografía ──
val ParkSmartTypography = Typography(
    headlineLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp, color = ParkSmartColors.TextPrimary),
    headlineMedium = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp, color = ParkSmartColors.TextPrimary),
    titleLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp, color = ParkSmartColors.TextPrimary),
    titleMedium = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = ParkSmartColors.TextPrimary),
    bodyLarge = TextStyle(fontWeight = FontWeight.Normal, fontSize = 15.sp, color = ParkSmartColors.TextPrimary),
    bodyMedium = TextStyle(fontWeight = FontWeight.Normal, fontSize = 13.sp, color = ParkSmartColors.TextSecondary),
    labelSmall = TextStyle(fontWeight = FontWeight.Normal, fontSize = 11.sp, color = ParkSmartColors.TextSecondary)
)

// ── Tema principal ──
@Composable
fun ParkSmartTheme(content: @Composable () -> Unit) {
    val colorScheme = lightColorScheme(
        primary          = ParkSmartColors.Primary,
        onPrimary        = ParkSmartColors.White,
        primaryContainer = ParkSmartColors.PrimaryLight,
        background       = ParkSmartColors.Background,
        surface          = ParkSmartColors.Surface,
        onBackground     = ParkSmartColors.TextPrimary,
        onSurface        = ParkSmartColors.TextPrimary,
        error            = ParkSmartColors.Error,
        errorContainer   = ParkSmartColors.ErrorLight
    )
    MaterialTheme(
        colorScheme = colorScheme,
        typography  = ParkSmartTypography,
        content     = content
    )
}
