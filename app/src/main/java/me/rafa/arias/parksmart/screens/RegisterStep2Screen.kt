package me.rafa.arias.parksmart.screens



import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.rafa.arias.parksmart.ui.ParkSmartColors


@Composable
fun RegisterStep2Screen(
    onRegisterClick: (
        nombreParqueadero: String,
        direccion: String,
        ciudad: String,
        nit: String,
        cupos: String,
        tarifaCarro: String,
        tarifaMoto: String
    ) -> Unit = { _, _, _, _, _, _, _ -> },
    onBackClick: () -> Unit = {}
) {
    var nombreParqueadero by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    var nit by remember { mutableStateOf("") }
    var cupos by remember { mutableStateOf("") }
    var cuposMotos by remember { mutableStateOf("") }
    var tarifaCarro by remember { mutableStateOf("") }
    var tarifaMoto by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var colorSeleccionado by remember { mutableStateOf(ParkSmartColors.Primary) }

    val coloresDisponibles = listOf(
        Color(0xFF90C749), // verde
        Color(0xFF2196F3), // azul
        Color(0xFFFF5722), // naranja
        Color(0xFF9C27B0), // morado
        Color(0xFFE91E63), // rosa
        Color(0xFF009688), // teal
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
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 8.dp)
            ) {
                Text("←", fontSize = 22.sp, color = ParkSmartColors.White)
            }
            Column(modifier = Modifier.align(Alignment.Center)) {
                Text(
                    text = "Tu parqueadero",
                    style = MaterialTheme.typography.titleLarge,
                    color = ParkSmartColors.White
                )
            }
            Text(
                text = "2 de 2",
                style = MaterialTheme.typography.bodyMedium,
                color = ParkSmartColors.White.copy(alpha = 0.85f),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
            )
        }


        LinearProgressIndicator(
            progress = { 1f },
            modifier = Modifier.fillMaxWidth().height(4.dp),
            color = ParkSmartColors.White,
            trackColor = ParkSmartColors.PrimaryDark
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))


            SectionLabel(text = "🅿  Información del negocio")
            Spacer(modifier = Modifier.height(12.dp))

            ParkSmartInput(
                value = nombreParqueadero,
                onValueChange = { nombreParqueadero = it },
                placeholder = "Nombre del parqueadero"
            )
            Spacer(modifier = Modifier.height(12.dp))

            ParkSmartInput(
                value = direccion,
                onValueChange = { direccion = it },
                placeholder = "Dirección exacta"
            )
            Spacer(modifier = Modifier.height(12.dp))

            ParkSmartInput(
                value = ciudad,
                onValueChange = { ciudad = it },
                placeholder = "Ciudad"
            )
            Spacer(modifier = Modifier.height(12.dp))


            ParkSmartInput(
                value = nit,
                onValueChange = { nuevo ->
                    if (nuevo.all { it.isDigit() }) nit = nuevo
                },
                placeholder = "NIT o RUT del negocio (solo números)",
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(24.dp))


            SectionLabel(text = "  Capacidad y tarifas")
            Spacer(modifier = Modifier.height(12.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                ParkSmartInput(
                    value = cupos,
                    onValueChange = { nuevo ->
                        if (nuevo.all { it.isDigit() }) cupos = nuevo
                    },
                    placeholder = "Cupos carros",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )

                ParkSmartInput(
                    value = cuposMotos,
                    onValueChange = { nuevo ->
                        if (nuevo.all { it.isDigit() }) cuposMotos = nuevo
                    },
                    placeholder = "Cupos motos",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                ParkSmartInput(
                    value = tarifaCarro,
                    onValueChange = { nuevo ->
                        if (nuevo.all { it.isDigit() }) tarifaCarro = nuevo
                    },
                    placeholder = "Tarifa/hora 🚗",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )

                ParkSmartInput(
                    value = tarifaMoto,
                    onValueChange = { nuevo ->
                        if (nuevo.all { it.isDigit() }) tarifaMoto = nuevo
                    },
                    placeholder = "Tarifa/hora 🏍️",
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))


            SectionLabel(text = "🎨  Color principal de tu marca")
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Este color se usará en toda tu app",
                style = MaterialTheme.typography.bodyMedium,
                color = ParkSmartColors.TextSecondary
            )
            Spacer(modifier = Modifier.height(12.dp))


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(ParkSmartColors.Surface)
                    .padding(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    coloresDisponibles.forEach { color ->
                        val isSelected = color == colorSeleccionado
                        Box(
                            modifier = Modifier
                                .size(if (isSelected) 42.dp else 36.dp)
                                .clip(CircleShape)
                                .background(color)
                                .then(
                                    if (isSelected) Modifier.border(
                                        3.dp,
                                        ParkSmartColors.TextPrimary,
                                        CircleShape
                                    ) else Modifier
                                )
                                .clickable { colorSeleccionado = color }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(colorSeleccionado),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Vista previa de tu color",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ParkSmartColors.White
                )
            }


            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(ParkSmartColors.ErrorLight)
                        .padding(12.dp)
                ) {
                    Text(
                        text = "❌  $errorMessage",
                        color = ParkSmartColors.Error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))


            Button(
                onClick = {
                    when {
                        nombreParqueadero.isBlank() || direccion.isBlank() ||
                                ciudad.isBlank() || nit.isBlank() ->
                            errorMessage = "Por favor completa todos los campos del negocio"
                        cupos.isBlank() || tarifaCarro.isBlank() || tarifaMoto.isBlank() ->
                            errorMessage = "Completa la capacidad y tarifas"
                        nit.length < 9 ->
                            errorMessage = "El NIT debe tener al menos 9 dígitos"
                        cupos.toIntOrNull() == null || cupos.toInt() <= 0 ->
                            errorMessage = "El número de cupos debe ser mayor a 0"
                        else -> {
                            errorMessage = ""
                            onRegisterClick(
                                nombreParqueadero,
                                direccion,
                                ciudad,
                                nit,
                                cupos,
                                tarifaCarro,
                                tarifaMoto
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ParkSmartColors.Primary
                )
            ) {
                Text(
                    text = "✓  Registrar mi parqueadero",
                    style = MaterialTheme.typography.titleMedium,
                    color = ParkSmartColors.White
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Al registrarte aceptas los términos y condiciones de uso de la plataforma.",
                style = MaterialTheme.typography.labelSmall,
                color = ParkSmartColors.TextSecondary,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}