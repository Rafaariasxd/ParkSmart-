package me.rafa.arias.parksmart.screens



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.rafa.arias.parksmart.ui.ParkSmartColors


@Composable
fun ProfileScreen(
    onBackClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Datos de ejemplo — después vendrán de Firebase
    val datosOperario = mapOf(
        "nombre" to "Carlos Gómez",
        "rol" to "Operario",
        "sede" to "Cabecera, Bucaramanga",
        "turno" to "7:00 AM - 3:00 PM",
        "vehiculosHoy" to "47 registrados",
        "recaudadoHoy" to "$94.200"
    )

    // Diálogo de confirmación cerrar sesión
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(
                    text = "Cerrar Sesión",
                    style = MaterialTheme.typography.titleMedium,
                    color = ParkSmartColors.TextPrimary
                )
            },
            text = {
                Text(
                    text = "¿Estás seguro que deseas cerrar sesión?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ParkSmartColors.TextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogoutClick()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ParkSmartColors.Error
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Sí, cerrar sesión", color = ParkSmartColors.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar", color = ParkSmartColors.TextSecondary)
                }
            },
            containerColor = ParkSmartColors.Surface,
            shape = RoundedCornerShape(16.dp)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ParkSmartColors.Background)
            .verticalScroll(rememberScrollState())
    ) {

        // ── Top Bar ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(ParkSmartColors.Primary)
                .padding(horizontal = 8.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Text("←", fontSize = 22.sp, color = ParkSmartColors.White)
            }
            Text(
                text = "Mi Perfil",
                style = MaterialTheme.typography.titleLarge,
                color = ParkSmartColors.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // ── Avatar ──
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .shadow(8.dp, CircleShape)
                    .clip(CircleShape)
                    .background(ParkSmartColors.Surface),
                contentAlignment = Alignment.Center
            ) {
                Text("👤", fontSize = 44.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = datosOperario["nombre"] ?: "",
                style = MaterialTheme.typography.headlineMedium,
                color = ParkSmartColors.TextPrimary
            )

            Text(
                text = "${datosOperario["rol"]} · ${datosOperario["sede"]}",
                style = MaterialTheme.typography.bodyMedium,
                color = ParkSmartColors.TextSecondary
            )

            Spacer(modifier = Modifier.height(28.dp))

            // ── Cards de info ──
            val infoItems = listOf(
                Triple("📍", "Sede asignada", datosOperario["sede"] ?: ""),
                Triple("🕐", "Turno actual", datosOperario["turno"] ?: ""),
                Triple("📅", "Vehículos hoy", datosOperario["vehiculosHoy"] ?: ""),
                Triple("💰", "Recaudado hoy", datosOperario["recaudadoHoy"] ?: ""),
            )

            infoItems.forEach { (icon, label, valor) ->
                ProfileInfoCard(icon = icon, label = label, valor = valor)
                Spacer(modifier = Modifier.height(10.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Botón cerrar sesión ──
            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ParkSmartColors.ErrorLight
                )
            ) {
                Text(
                    text = "🚪  Cerrar Sesión",
                    style = MaterialTheme.typography.titleMedium,
                    color = ParkSmartColors.Error
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "ParkSmart v1.0.0",
                style = MaterialTheme.typography.labelSmall,
                color = ParkSmartColors.TextSecondary
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// ── Card de info del perfil ──
@Composable
fun ProfileInfoCard(
    icon: String,
    label: String,
    valor: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(ParkSmartColors.Surface)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = ParkSmartColors.TextSecondary
                )
                Text(
                    text = valor,
                    style = MaterialTheme.typography.bodyMedium,
                    color = ParkSmartColors.TextPrimary
                )
            }
        }
    }
}