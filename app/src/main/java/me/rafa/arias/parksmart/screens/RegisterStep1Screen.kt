package me.rafa.arias.parksmart.screens




import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.rafa.arias.parksmart.ui.ParkSmartColors


@Composable
fun RegisterStep1Screen(
    onNextClick: (nombre: String, cedula: String, email: String, telefono: String, password: String) -> Unit = { _, _, _, _, _ -> },
    onBackClick: () -> Unit = {}
) {
    var nombre by remember { mutableStateOf("") }
    var cedula by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

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
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart).padding(start = 8.dp)
            ) {
                Text("←", fontSize = 22.sp, color = ParkSmartColors.White)
            }
            Column(modifier = Modifier.align(Alignment.Center)) {
                Text(
                    text = "Datos del propietario",
                    style = MaterialTheme.typography.titleLarge,
                    color = ParkSmartColors.White
                )
            }
            Text(
                text = "1 de 2",
                style = MaterialTheme.typography.bodyMedium,
                color = ParkSmartColors.White.copy(alpha = 0.85f),
                modifier = Modifier.align(Alignment.CenterEnd).padding(end = 16.dp)
            )
        }


        LinearProgressIndicator(
            progress = { 0.5f },
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


            SectionLabel(text = "👤  Información personal")
            Spacer(modifier = Modifier.height(12.dp))

            ParkSmartInput(
                value = nombre,
                onValueChange = { nombre = it },
                placeholder = "Nombre completo"
            )
            Spacer(modifier = Modifier.height(12.dp))

            ParkSmartInput(
                value = cedula,
                onValueChange = { cedula = it },
                placeholder = "Número de cédula",
                keyboardType = KeyboardType.Number
            )
            Spacer(modifier = Modifier.height(12.dp))

            ParkSmartInput(
                value = email,
                onValueChange = { email = it },
                placeholder = "Correo electrónico",
                keyboardType = KeyboardType.Email
            )
            Spacer(modifier = Modifier.height(12.dp))

            ParkSmartInput(
                value = telefono,
                onValueChange = { telefono = it },
                placeholder = "Teléfono de contacto",
                keyboardType = KeyboardType.Phone
            )

            Spacer(modifier = Modifier.height(24.dp))


            SectionLabel(text = "🔒  Seguridad")
            Spacer(modifier = Modifier.height(12.dp))

            ParkSmartInput(
                value = password,
                onValueChange = { password = it },
                placeholder = "Contraseña",
                keyboardType = KeyboardType.Password,
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible)
                                Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = null,
                            tint = ParkSmartColors.TextSecondary
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(12.dp))

            ParkSmartInput(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = "Confirmar contraseña",
                keyboardType = KeyboardType.Password,
                visualTransformation = if (confirmPasswordVisible)
                    VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector = if (confirmPasswordVisible)
                                Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = null,
                            tint = ParkSmartColors.TextSecondary
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(ParkSmartColors.PrimaryLight)
                    .padding(16.dp)
            ) {
                Text(
                    text = "ℹ  La cédula se usará para verificar tu identidad como propietario del parqueadero.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ParkSmartColors.PrimaryDark
                )
            }


            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage,
                    color = ParkSmartColors.Error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))


            Button(
                onClick = {
                    when {
                        nombre.isBlank() || cedula.isBlank() || email.isBlank() || telefono.isBlank() ->
                            errorMessage = "Por favor completa todos los campos"
                        password.length < 6 ->
                            errorMessage = "La contraseña debe tener al menos 6 caracteres"
                        password != confirmPassword ->
                            errorMessage = "Las contraseñas no coinciden"
                        else -> {
                            errorMessage = ""
                            onNextClick(nombre, cedula, email, telefono, password)
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
                    text = "Siguiente →",
                    style = MaterialTheme.typography.titleMedium,
                    color = ParkSmartColors.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            TextButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "¿Ya tienes cuenta? Inicia sesión",
                    color = ParkSmartColors.Primary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}


@Composable
fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = ParkSmartColors.TextPrimary
    )
}