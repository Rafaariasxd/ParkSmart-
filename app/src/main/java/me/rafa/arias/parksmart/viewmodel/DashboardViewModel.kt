package me.rafa.arias.parksmart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.rafa.arias.parksmart.repository.VehicleItem
import me.rafa.arias.parksmart.repository.VehicleRepository

data class DashboardUiState(
    val isLoading: Boolean = true,
    val operadorNombre: String = "",
    val parqueaderoNombre: String = "",
    val cuposDisponibles: Int = 0,
    val cuposTotales: Int = 0,
    val ultimosVehiculos: List<VehicleItem> = emptyList()
)

class DashboardViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val lotInfo = VehicleRepository.loadParkingLotInfo().getOrNull()
            val operName = VehicleRepository.loadOperatorName().getOrNull() ?: ""
            val cuposTotales = (lotInfo?.cuposCarros ?: 0) + (lotInfo?.cuposMotos ?: 0)

            _uiState.update {
                it.copy(
                    parqueaderoNombre = lotInfo?.nombre ?: "Mi Parqueadero",
                    operadorNombre = operName,
                    cuposTotales = cuposTotales
                )
            }

            VehicleRepository.observeTodayVehiculos().collect { vehiculos ->
                val adentro = vehiculos.count { it.estado == "Adentro" }
                _uiState.update { s ->
                    s.copy(
                        isLoading = false,
                        cuposDisponibles = (s.cuposTotales - adentro).coerceAtLeast(0),
                        ultimosVehiculos = vehiculos.take(3)
                    )
                }
            }
        }
    }
}
