package me.rafa.arias.parksmart.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.rafa.arias.parksmart.ui.ParkSmartColors
import me.rafa.arias.parksmart.viewmodel.ScannerViewModel


@Composable
fun ScannerScreen(
    viewModel: ScannerViewModel,
    onBackClick: () -> Unit = {},
    onConfirmClick: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ParkSmartColors.CameraBackground)
    ) {

        Box(
            modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A1A)),
            contentAlignment = Alignment.Center
        ) {
            if (state.escaneando) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "📷", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Cámara activa", color = Color.White.copy(alpha = 0.5f), fontSize = 14.sp)
                    Text(
                        text = "(Funcionalidad real se agrega despues)",
                        color = Color.White.copy(alpha = 0.3f),
                        fontSize = 11.sp
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(Color.Black.copy(alpha = 0.5f))
                .align(Alignment.TopCenter)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart).padding(start = 8.dp)
            ) {
                Text("←", fontSize = 22.sp, color = Color.White)
            }
            Text(
                text = "Escanear Placa",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Text(
            text = "Alinea la placa dentro del marco",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 80.dp)
        )

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-60).dp)
                .width(280.dp)
                .height(100.dp)
                .border(width = 3.dp, color = ParkSmartColors.Primary, shape = RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = state.placaDetectada,
                fontSize = 28.sp,
                color = Color.White.copy(alpha = 0.3f),
                style = MaterialTheme.typography.headlineMedium
            )
            Box(
                modifier = Modifier.fillMaxWidth().height(2.dp).background(ParkSmartColors.Primary)
            )
        }

        if (state.escaneando) {
            Button(
                onClick = { viewModel.onSimularEscaneo() },
                modifier = Modifier.align(Alignment.Center).offset(y = 80.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ParkSmartColors.Primary)
            ) {
                Text(text = "📷  Simular escaneo", color = ParkSmartColors.White, style = MaterialTheme.typography.titleMedium)
            }
        }

        if (!state.escaneando) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(ParkSmartColors.Surface)
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .width(40.dp).height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(ParkSmartColors.Divider)
                            .align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Placa detectada",
                        style = MaterialTheme.typography.titleMedium,
                        color = ParkSmartColors.TextPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = state.placaDetectada,
                        onValueChange = { viewModel.onPlacaChange(it) },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(10.dp),
                        textStyle = MaterialTheme.typography.headlineMedium.copy(
                            textAlign = TextAlign.Center,
                            fontSize = 22.sp
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = ParkSmartColors.PrimaryLight,
                            focusedContainerColor = ParkSmartColors.PrimaryLight,
                            unfocusedBorderColor = ParkSmartColors.Primary,
                            focusedBorderColor = ParkSmartColors.Primary,
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Tipo de vehículo",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ParkSmartColors.TextSecondary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TipoVehiculoBtn(
                            label = "🚗  Carro",
                            selected = state.tipoVehiculo == "Carro",
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.onTipoVehiculoChange("Carro") }
                        )
                        TipoVehiculoBtn(
                            label = "🏍️  Moto",
                            selected = state.tipoVehiculo == "Moto",
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.onTipoVehiculoChange("Moto") }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onConfirmClick,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ParkSmartColors.Primary)
                    ) {
                        Text(
                            text = "Registrar Ingreso",
                            style = MaterialTheme.typography.titleMedium,
                            color = ParkSmartColors.White
                        )
                    }

                    TextButton(
                        onClick = { viewModel.onVolverAEscanear() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Volver a escanear",
                            color = ParkSmartColors.Primary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun TipoVehiculoBtn(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(44.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) ParkSmartColors.Primary else ParkSmartColors.PrimaryLight
        ),
        border = if (!selected) ButtonDefaults.outlinedButtonBorder else null
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = if (selected) ParkSmartColors.White else ParkSmartColors.Primary,
            fontSize = 13.sp
        )
    }
}
