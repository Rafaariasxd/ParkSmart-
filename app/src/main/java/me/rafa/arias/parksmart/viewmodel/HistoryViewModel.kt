package me.rafa.arias.parksmart.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.rafa.arias.parksmart.model.VehicleRecord

data class HistoryUiState(
    val filtroSeleccionado: String = "Todos",
    val vehiculosFiltrados: List<VehicleRecord> = emptyList()
)

class HistoryViewModel : ViewModel() {

    private val todosLosVehiculos = listOf(
        VehicleRecord("ABC-123", "🚗 Carro", "10:32 AM", null, "Adentro"),
        VehicleRecord("XYZ-456", "🏍️ Moto", "10:15 AM", "11:30 AM", "Salió"),
        VehicleRecord("DEF-789", "🚗 Carro", "09:58 AM", null, "Adentro"),
        VehicleRecord("GHI-321", "🏍️ Moto", "09:40 AM", "10:50 AM", "Salió"),
        VehicleRecord("JKL-654", "🚗 Carro", "09:22 AM", null, "Adentro"),
        VehicleRecord("MNO-987", "🚗 Carro", "08:55 AM", "10:10 AM", "Salió"),
        VehicleRecord("PQR-111", "🏍️ Moto", "08:30 AM", null, "Adentro")
    )

    private val _uiState = MutableStateFlow(
        HistoryUiState(vehiculosFiltrados = todosLosVehiculos)
    )
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    fun onFiltroChange(filtro: String) {
        val filtrados = when (filtro) {
            "Carros" -> todosLosVehiculos.filter { it.tipo.contains("Carro") }
            "Motos"  -> todosLosVehiculos.filter { it.tipo.contains("Moto") }
            "Salidas" -> todosLosVehiculos.filter { it.estado == "Salió" }
            else -> todosLosVehiculos
        }
        _uiState.update { it.copy(filtroSeleccionado = filtro, vehiculosFiltrados = filtrados) }
    }
}
