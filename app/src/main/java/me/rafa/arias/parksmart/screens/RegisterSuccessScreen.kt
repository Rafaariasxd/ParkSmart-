package me.rafa.arias.parksmart.screens



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.rafa.arias.parksmart.ui.ParkSmartColors


@Composable
fun RegisterSuccessScreen(
    nombreParqueadero: String = "Mi Parqueadero",
    cupos: String = "0",
    onGoToDashboard: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ParkSmartColors.Background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(ParkSmartColors.Primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "¡Registro exitoso!",
                style = MaterialTheme.typography.titleLarge,
                color = ParkSmartColors.White
            )
        }

        Spacer(modifier = Modifier.height(48.dp))


        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(ParkSmartColors.Primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "✓",
                fontSize = 60.sp,
                color = ParkSmartColors.White
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "¡Bienvenido!",
            style = MaterialTheme.typography.headlineLarge,
            color = ParkSmartColors.TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Tu parqueadero fue registrado\nexitosamente en la plataforma.",
            style = MaterialTheme.typography.bodyLarge,
            color = ParkSmartColors.TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(36.dp))


        Box(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(ParkSmartColors.Surface)
        ) {
            Column {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        .background(ParkSmartColors.PrimaryLight)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "🅿  $nombreParqueadero",
                        style = MaterialTheme.typography.titleMedium,
                        color = ParkSmartColors.Primary
                    )
                }


                ResumenRow(label = "Cupos totales", valor = "$cupos cupos")
                HorizontalDivider(color = ParkSmartColors.Divider, thickness = 1.dp)
                ResumenRow(label = "Plan activo", valor = "Gratuito ✓")
                HorizontalDivider(color = ParkSmartColors.Divider, thickness = 1.dp)
                ResumenRow(label = "Estado", valor = "Activo 🟢")
            }
        }

        Spacer(modifier = Modifier.height(36.dp))


        Button(
            onClick = onGoToDashboard,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ParkSmartColors.Primary
            )
        ) {
            Text(
                text = "Ir a mi panel de control",
                style = MaterialTheme.typography.titleMedium,
                color = ParkSmartColors.White
            )
        }
    }
}

@Composable
fun ResumenRow(label: String, valor: String) {
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