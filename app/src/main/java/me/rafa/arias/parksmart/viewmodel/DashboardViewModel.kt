package me.rafa.arias.parksmart.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.rafa.arias.parksmart.model.VehicleRecord

data class DashboardUiState(
    val cuposDisponibles: Int = 120,
    val cuposTotales: Int = 150,
    val ultimosVehiculos: List<VehicleRecord> = listOf(
        VehicleRecord("ABC-123", "🚗 Carro", "10:32 AM", null, "Adentro"),
        VehicleRecord("XYZ-456", "🏍️ Moto", "10:15 AM", null, "Adentro"),
        VehicleRecord("DEF-789", "🚗 Carro", "09:58 AM", null, "Adentro")
    )
)

class DashboardViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()
}
