package me.rafa.arias.parksmart.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.rafa.arias.parksmart.ui.ParkSmartColors
import me.rafa.arias.parksmart.viewmodel.LoginViewModel


@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ParkSmartColors.Background)
            .verticalScroll(rememberScrollState())
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                .background(ParkSmartColors.Primary),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "🅿", fontSize = 72.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "ParkSmart",
                    style = MaterialTheme.typography.headlineLarge,
                    color = ParkSmartColors.White
                )
                Text(
                    text = "Gestiona tu parqueadero",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ParkSmartColors.White.copy(alpha = 0.85f)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Iniciar Sesión",
                style = MaterialTheme.typography.headlineMedium,
                color = ParkSmartColors.TextPrimary
            )
            Text(
                text = "Ingresa tus credenciales para continuar",
                style = MaterialTheme.typography.bodyMedium,
                color = ParkSmartColors.TextSecondary
            )

            Spacer(modifier = Modifier.height(28.dp))

            ParkSmartInput(
                value = state.email,
                onValueChange = { viewModel.onEmailChange(it) },
                placeholder = "Correo electrónico",
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            ParkSmartInput(
                value = state.password,
                onValueChange = { viewModel.onPasswordChange(it) },
                placeholder = "Contraseña",
                keyboardType = KeyboardType.Password,
                visualTransformation = if (state.passwordVisible)
                    VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { viewModel.togglePasswordVisibility() }) {
                        Icon(
                            imageVector = if (state.passwordVisible)
                                Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = null,
                            tint = ParkSmartColors.TextSecondary
                        )
                    }
                }
            )

            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "¿Olvidaste tu contraseña?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ParkSmartColors.Primary,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(top = 8.dp)
                        .clickable { }
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            if (state.errorMessage.isNotEmpty()) {
                Text(
                    text = state.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )
            }

            Button(
                onClick = { viewModel.onLoginClick() },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ParkSmartColors.Primary)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = ParkSmartColors.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Ingresar",
                        style = MaterialTheme.typography.titleMedium,
                        color = ParkSmartColors.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Divider(modifier = Modifier.weight(1f), color = ParkSmartColors.Divider)
                Text(
                    text = "  ¿Nuevo aquí?  ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ParkSmartColors.TextSecondary
                )
                Divider(modifier = Modifier.weight(1f), color = ParkSmartColors.Divider)
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { viewModel.onRegisterClick() },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(10.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(width = 2.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ParkSmartColors.Primary)
            ) {
                Text(
                    text = "🅿  Registra tu parqueadero",
                    style = MaterialTheme.typography.titleMedium,
                    color = ParkSmartColors.Primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Al registrarte aceptas los términos y condiciones",
                style = MaterialTheme.typography.labelSmall,
                color = ParkSmartColors.TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}


@Composable
fun ParkSmartInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(text = placeholder, color = ParkSmartColors.TextSecondary, fontSize = 15.sp)
        },
        modifier = Modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = ParkSmartColors.PrimaryLight,
            focusedContainerColor = ParkSmartColors.PrimaryLight,
            unfocusedBorderColor = ParkSmartColors.PrimaryBorder,
            focusedBorderColor = ParkSmartColors.Primary,
            cursorColor = ParkSmartColors.Primary
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        singleLine = true
    )
}

@Composable
fun ParkSmartInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(text = placeholder, color = ParkSmartColors.TextSecondary, fontSize = 15.sp)
        },
        modifier = modifier.height(52.dp),
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = ParkSmartColors.PrimaryLight,
            focusedContainerColor = ParkSmartColors.PrimaryLight,
            unfocusedBorderColor = ParkSmartColors.PrimaryBorder,
            focusedBorderColor = ParkSmartColors.Primary,
            cursorColor = ParkSmartColors.Primary
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        singleLine = true
    )
}
