package me.rafa.arias.parksmart.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.rafa.arias.parksmart.model.Vehicle

data class DashboardUiState(
    val cuposDisponibles: Int = 120,
    val cuposTotales: Int = 150,
    val ultimosVehiculos: List<Vehicle> = listOf(
        Vehicle(placa = "ABC-123", tipo = "🚗 Carro", horaIngreso = "10:32 AM"),
        Vehicle(placa = "XYZ-456", tipo = "🏍️ Moto",  horaIngreso = "10:15 AM"),
        Vehicle(placa = "DEF-789", tipo = "🚗 Carro", horaIngreso = "09:58 AM")
    )
)

class DashboardViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()
}
