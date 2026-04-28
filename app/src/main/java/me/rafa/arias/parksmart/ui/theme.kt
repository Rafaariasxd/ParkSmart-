package me.rafa.arias.parksmart.ui



import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ── Colores ──
object ParkSmartColors {
    val Primary        = Color(0xFF90C749)

    val PrimaryLight   = Color(0xFFF2F9E8)
    val PrimaryBorder  = Color(0xFFC8E6A0)
    val PrimaryDark    = Color(0xFF6FA832)
    val Background     = Color(0xFFF4F7F0)
    val Surface        = Color(0xFFFFFFFF)
    val TextPrimary    = Color(0xFF2D3142)
    val TextSecondary  = Color(0xFF9E9E9E)
    val Error          = Color(0xFFE53935)
    val ErrorLight     = Color(0xFFFFEBEE)
    val Divider        = Color(0xFFEEEEEE)
    val White          = Color(0xFFFFFFFF)
    val Black          = Color(0xFF000000)
    val HeaderGreen    = Color(0xFF90C749)
    val CameraBackground = Color(0xFF262626)
}

// ── Tipografía ──
val ParkSmartTypography = Typography(
    headlineLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        color = ParkSmartColors.TextPrimary
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = ParkSmartColors.TextPrimary
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = ParkSmartColors.TextPrimary
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = ParkSmartColors.TextPrimary
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        color = ParkSmartColors.TextPrimary
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        color = ParkSmartColors.TextSecondary
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        color = ParkSmartColors.TextSecondary
    )
)

// ── Materiales ColorScheme ──
private val ParkSmartColorScheme = lightColorScheme(
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

// ── Tema principal ──
@Composable
fun ParkSmartTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ParkSmartColorScheme,
        typography  = ParkSmartTypography,
        content     = content
    )
}