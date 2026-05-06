package me.rafa.arias.parksmart.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.rafa.arias.parksmart.ui.ParkSmartColors
import me.rafa.arias.parksmart.viewmodel.ReportsViewModel


@Composable
fun ReportsScreen(
    viewModel: ReportsViewModel,
    onBackClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    val periodos = listOf("Hoy", "Semana", "Mes")

    Scaffold(
        bottomBar = {
            ParkSmartNavBar(
                selectedIndex = 2,
                onHomeClick = onHomeClick,
                onHistoryClick = onHistoryClick,
                onReportsClick = {}
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
                    .background(ParkSmartColors.Primary)
                    .padding(horizontal = 8.dp)
            ) {
                IconButton(onClick = onBackClick, modifier = Modifier.align(Alignment.CenterStart)) {
                    Text("←", fontSize = 22.sp, color = ParkSmartColors.White)
                }
                Text(
                    text = "Reportes",
                    style = MaterialTheme.typography.titleLarge,
                    color = ParkSmartColors.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp))
                        .background(ParkSmartColors.Surface)
                        .padding(4.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        periodos.forEach { periodo ->
                            val selected = state.periodoSeleccionado == periodo
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (selected) ParkSmartColors.Primary else Color.Transparent)
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                TextButton(onClick = { viewModel.onPeriodoChange(periodo) }) {
                                    Text(
                                        text = periodo,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = if (selected) ParkSmartColors.White else ParkSmartColors.TextSecondary,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricCard(
                        icon = "💰", valor = state.datos["ingresos"] ?: "",
                        label = "Total Ingresos", color = ParkSmartColors.Primary, modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        icon = "🚗", valor = state.datos["vehiculos"] ?: "",
                        label = "Vehículos", color = Color(0xFF2196F3), modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricCard(
                        icon = "🚙", valor = state.datos["carros"] ?: "",
                        label = "Carros", color = ParkSmartColors.Primary, modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        icon = "🏍️", valor = state.datos["motos"] ?: "",
                        label = "Motos", color = Color(0xFFFF5722), modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, RoundedCornerShape(14.dp))
                        .clip(RoundedCornerShape(14.dp))
                        .background(ParkSmartColors.Surface)
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Ocupación promedio",
                                style = MaterialTheme.typography.bodyMedium,
                                color = ParkSmartColors.TextSecondary
                            )
                            Text(
                                text = state.datos["ocupacion"] ?: "",
                                style = MaterialTheme.typography.headlineMedium,
                                color = ParkSmartColors.Primary
                            )
                        }
                        Text("📊", fontSize = 36.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, RoundedCornerShape(14.dp))
                        .clip(RoundedCornerShape(14.dp))
                        .background(ParkSmartColors.Surface)
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = "Ingresos por hora",
                            style = MaterialTheme.typography.titleMedium,
                            color = ParkSmartColors.TextPrimary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth().height(120.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            state.barrasPorHora.forEach { (hora, altura) ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Bottom,
                                    modifier = Modifier.fillMaxHeight()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .width(20.dp)
                                            .fillMaxHeight(altura)
                                            .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                            .background(
                                                if (altura == 1.0f) ParkSmartColors.Primary
                                                else ParkSmartColors.PrimaryBorder
                                            )
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = hora, fontSize = 9.sp, color = ParkSmartColors.TextSecondary)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Hora pico: 10:00 AM",
                            style = MaterialTheme.typography.bodyMedium,
                            color = ParkSmartColors.TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}


@Composable
fun MetricCard(
    icon: String,
    valor: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(2.dp, RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))
            .background(ParkSmartColors.Surface)
            .padding(16.dp)
    ) {
        Column {
            Text(icon, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = valor, style = MaterialTheme.typography.titleLarge, color = color, fontSize = 20.sp)
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = ParkSmartColors.TextSecondary)
        }
    }
}
