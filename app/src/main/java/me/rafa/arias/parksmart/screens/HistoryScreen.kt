package me.rafa.arias.parksmart.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.rafa.arias.parksmart.model.Vehicle
import me.rafa.arias.parksmart.ui.ParkSmartColors
import me.rafa.arias.parksmart.viewmodel.HistoryViewModel


@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    onBackClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onReportsClick: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    val filtros = listOf("Todos", "Carros", "Motos", "Salidas")

    Scaffold(
        bottomBar = {
            ParkSmartNavBar(
                selectedIndex = 1,
                onHomeClick = onHomeClick,
                onHistoryClick = {},
                onReportsClick = onReportsClick
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ParkSmartColors.Background)
                .padding(paddingValues)
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
                    text = "Historial del Día",
                    style = MaterialTheme.typography.titleLarge,
                    color = ParkSmartColors.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filtros.forEach { filtro ->
                    FilterChip(
                        selected = state.filtroSeleccionado == filtro,
                        onClick = { viewModel.onFiltroChange(filtro) },
                        label = {
                            Text(
                                text = filtro,
                                fontSize = 12.sp,
                                color = if (state.filtroSeleccionado == filtro)
                                    ParkSmartColors.White else ParkSmartColors.TextSecondary
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ParkSmartColors.Primary,
                            containerColor = ParkSmartColors.Surface
                        )
                    )
                }
            }

            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .shadow(2.dp, RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .background(ParkSmartColors.Surface)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ResumenDiaItem(valor = "47", label = "Ingresos", color = ParkSmartColors.Primary)
                    VerticalDivider(modifier = Modifier.height(40.dp), color = ParkSmartColors.Divider)
                    ResumenDiaItem(valor = "31", label = "Salidas", color = ParkSmartColors.TextSecondary)
                    VerticalDivider(modifier = Modifier.height(40.dp), color = ParkSmartColors.Divider)
                    ResumenDiaItem(valor = "$94.200", label = "Recaudado", color = ParkSmartColors.Primary)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(state.vehiculosFiltrados) { vehiculo ->
                    HistoryVehicleCard(vehiculo = vehiculo)
                }
            }
        }
    }
}


@Composable
fun ResumenDiaItem(
    valor: String,
    label: String,
    color: androidx.compose.ui.graphics.Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = valor, style = MaterialTheme.typography.titleLarge, color = color, fontSize = 18.sp)
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = ParkSmartColors.TextSecondary)
    }
}


@Composable
fun HistoryVehicleCard(vehiculo: Vehicle) {
    val estadoAdentro = vehiculo.estaAdentro()

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
                Text(text = vehiculo.getPlaca(), style = MaterialTheme.typography.titleMedium, color = ParkSmartColors.TextPrimary)
                Text(text = vehiculo.getTipo(), style = MaterialTheme.typography.bodyMedium, color = ParkSmartColors.TextSecondary)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ingresó: ${vehiculo.getHoraIngreso()}" +
                            if (vehiculo.getHoraSalida() != null) "  •  Salió: ${vehiculo.getHoraSalida()}" else "",
                    fontSize = 11.sp,
                    color = ParkSmartColors.TextSecondary
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(11.dp))
                    .background(if (estadoAdentro) ParkSmartColors.PrimaryLight else ParkSmartColors.ErrorLight)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = vehiculo.getEstado(),
                    fontSize = 11.sp,
                    color = if (estadoAdentro) ParkSmartColors.Primary else ParkSmartColors.Error
                )
            }
        }
    }
}
