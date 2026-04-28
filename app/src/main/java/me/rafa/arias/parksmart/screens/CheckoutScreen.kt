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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.rafa.arias.parksmart.ui.ParkSmartColors


@Composable
fun CheckoutScreen(
    onBackClick: () -> Unit = {},
    onConfirmClick: () -> Unit = {}
) {
    var placaBuscada by remember { mutableStateOf("") }
    var vehiculoEncontrado by remember { mutableStateOf(false) }
    var vehiculoNoEncontrado by remember { mutableStateOf(false) }

    // Datos de ejemplo despues implemento   Firebase
    val datosVehiculo = mapOf(
        "placa" to "ABC-123",
        "tipo" to "🚗 Carro",
        "horaIngreso" to "10:32 AM",
        "horaSalida" to "01:15 PM",
        "tiempoTotal" to "2h 43min",
        "total" to "$5.400",
        "tarifa" to "$2.000/hora"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ParkSmartColors.Background)
            .verticalScroll(rememberScrollState())
    ) {


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(ParkSmartColors.Primary)
                .padding(horizontal = 8.dp),
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Text("←", fontSize = 22.sp, color = ParkSmartColors.White)
            }
            Text(
                text = "Registrar Salida",
                style = MaterialTheme.typography.titleLarge,
                color = ParkSmartColors.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = placaBuscada,
                    onValueChange = { nuevo ->
                        placaBuscada = nuevo.uppercase()
                        vehiculoEncontrado = false
                        vehiculoNoEncontrado = false
                    },
                    placeholder = {
                        Text(
                            "🔍  Buscar placa...",
                            color = ParkSmartColors.TextSecondary
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = ParkSmartColors.Surface,
                        focusedContainerColor = ParkSmartColors.Surface,
                        unfocusedBorderColor = ParkSmartColors.PrimaryBorder,
                        focusedBorderColor = ParkSmartColors.Primary,
                    ),
                    singleLine = true
                )
                Button(
                    onClick = {
                        // después conecto Firebase
                        if (placaBuscada == "ABC-123") {
                            vehiculoEncontrado = true
                            vehiculoNoEncontrado = false
                        } else {
                            vehiculoNoEncontrado = true
                            vehiculoEncontrado = false
                        }
                    },
                    modifier = Modifier.height(52.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ParkSmartColors.Primary
                    )
                ) {
                    Text(
                        text = "Buscar",
                        color = ParkSmartColors.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ──  Vehículo no encontrado ──
            if (vehiculoNoEncontrado) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(ParkSmartColors.ErrorLight)
                        .padding(16.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🔍", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(8.dp),)
                        Text(
                            text = "Vehículo no encontrado",
                            style = MaterialTheme.typography.titleMedium,
                            color = ParkSmartColors.Error
                        )
                        Text(
                            text = "La placa $placaBuscada no tiene\ningreso registrado hoy.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = ParkSmartColors.Error,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(
                            onClick = {
                                placaBuscada = ""
                                vehiculoNoEncontrado = false
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = ParkSmartColors.Error
                            )
                        ) {
                            Text("Buscar otra placa")
                        }
                    }
                }
            }

            // ──  Vehículo encontrado ──
            if (vehiculoEncontrado) {


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(ParkSmartColors.Surface)
                ) {
                    Column {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 16.dp,
                                        topEnd = 16.dp
                                    )
                                )
                                .background(ParkSmartColors.Primary)
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${datosVehiculo["tipo"]}  ${datosVehiculo["placa"]}",
                                style = MaterialTheme.typography.titleLarge,
                                color = ParkSmartColors.White
                            )
                        }


                        DetalleRow("Hora de ingreso", datosVehiculo["horaIngreso"] ?: "")
                        HorizontalDivider(color = ParkSmartColors.Divider)
                        DetalleRow("Hora de salida", datosVehiculo["horaSalida"] ?: "")
                        HorizontalDivider(color = ParkSmartColors.Divider)
                        DetalleRow("Tiempo total", datosVehiculo["tiempoTotal"] ?: "")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(ParkSmartColors.PrimaryLight)
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Total a Pagar",
                            style = MaterialTheme.typography.bodyMedium,
                            color = ParkSmartColors.TextSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = datosVehiculo["total"] ?: "",
                            style = MaterialTheme.typography.headlineLarge,
                            color = ParkSmartColors.Primary,
                            fontSize = 36.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tarifa: ${datosVehiculo["tarifa"]} · ${datosVehiculo["tipo"]}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = ParkSmartColors.TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Botón confirmar pago
                Button(
                    onClick = onConfirmClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ParkSmartColors.Primary
                    )
                ) {
                    Text(
                        text = "✓  Confirmar Pago y Liberar Cupo",
                        style = MaterialTheme.typography.titleMedium,
                        color = ParkSmartColors.White
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = {
                        placaBuscada = ""
                        vehiculoEncontrado = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Cancelar",
                        color = ParkSmartColors.Error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}


@Composable
fun DetalleRow(label: String, valor: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = ParkSmartColors.TextSecondary
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyMedium,
            color = ParkSmartColors.TextPrimary
        )
    }
}