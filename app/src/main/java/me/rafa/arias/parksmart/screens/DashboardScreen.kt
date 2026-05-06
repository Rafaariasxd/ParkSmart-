package me.rafa.arias.parksmart.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.rafa.arias.parksmart.ui.ParkSmartColors
import me.rafa.arias.parksmart.viewmodel.DashboardViewModel


@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onScanClick: () -> Unit = {},
    onCheckoutClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onReportsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()

    val porcentajeOcupado = state.cuposDisponibles.toFloat() / state.cuposTotales.toFloat()
    val parqueaderoLleno = state.cuposDisponibles == 0

    Scaffold(
        bottomBar = {
            ParkSmartNavBar(
                selectedIndex = 0,
                onHomeClick = {},
                onHistoryClick = onHistoryClick,
                onReportsClick = onReportsClick
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ParkSmartColors.Background)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(if (parqueaderoLleno) ParkSmartColors.Error else ParkSmartColors.Primary)
                    .padding(horizontal = 20.dp),
            ) {
                Column(modifier = Modifier.align(Alignment.CenterStart)) {
                    Text(
                        text = "Hola, Carlos ",
                        style = MaterialTheme.typography.titleLarge,
                        color = ParkSmartColors.White
                    )
                    Text(
                        text = "Sede Cabecera",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ParkSmartColors.White.copy(alpha = 0.85f)
                    )
                }
                IconButton(
                    onClick = onProfileClick,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Text("👤", fontSize = 22.sp)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(ParkSmartColors.Surface)
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Cupos Disponibles",
                                style = MaterialTheme.typography.bodyMedium,
                                color = ParkSmartColors.TextSecondary
                            )
                            if (parqueaderoLleno) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(ParkSmartColors.Error)
                                        .padding(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    Text(text = "⚠ LLENO", fontSize = 11.sp, color = ParkSmartColors.White)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "${state.cuposDisponibles} / ${state.cuposTotales}",
                            style = MaterialTheme.typography.headlineLarge,
                            color = if (parqueaderoLleno) ParkSmartColors.Error else ParkSmartColors.TextPrimary,
                            fontSize = 32.sp
                        )
                        Text(
                            text = "cupos libres hoy",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (parqueaderoLleno) ParkSmartColors.Error else ParkSmartColors.Primary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        LinearProgressIndicator(
                            progress = { porcentajeOcupado },
                            modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                            color = if (parqueaderoLleno) ParkSmartColors.Error else ParkSmartColors.Primary,
                            trackColor = ParkSmartColors.Divider
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Acciones Rápidas",
                    style = MaterialTheme.typography.titleMedium,
                    color = ParkSmartColors.TextPrimary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ActionButton(
                        icon = "📷",
                        label = "Ingresar\nVehículo",
                        enabled = !parqueaderoLleno,
                        isPrimary = true,
                        modifier = Modifier.weight(1f),
                        onClick = onScanClick
                    )
                    ActionButton(
                        icon = "💳",
                        label = "Registrar\nSalida",
                        enabled = true,
                        isPrimary = false,
                        modifier = Modifier.weight(1f),
                        onClick = onCheckoutClick
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Últimos Ingresos",
                        style = MaterialTheme.typography.titleMedium,
                        color = ParkSmartColors.TextPrimary
                    )
                    Text(
                        text = "Ver todos →",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ParkSmartColors.Primary,
                        modifier = Modifier.clickable { onHistoryClick() }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                state.ultimosVehiculos.forEach { vehiculo ->
                    VehicleCard(
                        placa = vehiculo.placa,
                        tipo = vehiculo.tipo,
                        hora = vehiculo.horaIngreso,
                        estado = vehiculo.estado
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}


@Composable
fun ActionButton(
    icon: String,
    label: String,
    enabled: Boolean,
    isPrimary: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .shadow(if (enabled) 6.dp else 0.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(
                when {
                    !enabled -> ParkSmartColors.TextSecondary.copy(alpha = 0.35f)
                    isPrimary -> ParkSmartColors.Primary
                    else -> ParkSmartColors.Surface
                }
            )
            .clickable(enabled = enabled) { onClick() }
            .padding(vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(icon, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                color = if (isPrimary) ParkSmartColors.White else ParkSmartColors.TextPrimary,
                textAlign = TextAlign.Center,
                fontSize = 13.sp
            )
        }
    }
}


@Composable
fun VehicleCard(
    placa: String,
    tipo: String,
    hora: String,
    estado: String
) {
    val estadoAdentro = estado == "Adentro"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(ParkSmartColors.Surface)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = placa, style = MaterialTheme.typography.titleMedium, color = ParkSmartColors.TextPrimary)
                Text(text = tipo, style = MaterialTheme.typography.bodyMedium, color = ParkSmartColors.TextSecondary)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(text = hora, style = MaterialTheme.typography.bodyMedium, color = ParkSmartColors.TextSecondary, fontSize = 11.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(11.dp))
                        .background(if (estadoAdentro) ParkSmartColors.PrimaryLight else ParkSmartColors.ErrorLight)
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = estado,
                        fontSize = 10.sp,
                        color = if (estadoAdentro) ParkSmartColors.Primary else ParkSmartColors.Error
                    )
                }
            }
        }
    }
}


@Composable
fun ParkSmartNavBar(
    selectedIndex: Int,
    onHomeClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onReportsClick: () -> Unit
) {
    val items = listOf(
        Triple("🏠", "Inicio", onHomeClick),
        Triple("📋", "Historial", onHistoryClick),
        Triple("📊", "Reportes", onReportsClick),
    )

    NavigationBar(containerColor = ParkSmartColors.Surface, tonalElevation = 8.dp) {
        items.forEachIndexed { index, (icon, label, onClick) ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = onClick,
                icon = { Text(icon, fontSize = 22.sp) },
                label = {
                    Text(
                        text = label,
                        fontSize = 10.sp,
                        color = if (selectedIndex == index) ParkSmartColors.Primary else ParkSmartColors.TextSecondary
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = ParkSmartColors.Primary,
                    indicatorColor = ParkSmartColors.PrimaryLight
                )
            )
        }
    }
}
