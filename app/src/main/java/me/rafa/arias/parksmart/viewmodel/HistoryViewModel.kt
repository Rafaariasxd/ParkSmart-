package me.rafa.arias.parksmart.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.rafa.arias.parksmart.model.Vehicle

data class HistoryUiState(
    val filtroSeleccionado: String = "Todos",
    val vehiculosFiltrados: List<Vehicle> = emptyList()
)

class HistoryViewModel : ViewModel() {

    private val todosLosVehiculos = listOf(
        Vehicle(placa = "ABC-123", tipo = "🚗 Carro", horaIngreso = "10:32 AM", estado = "Adentro"),
        Vehicle(placa = "XYZ-456", tipo = "🏍️ Moto",  horaIngreso = "10:15 AM", horaSalida = "11:30 AM", estado = "Salió"),
        Vehicle(placa = "DEF-789", tipo = "🚗 Carro", horaIngreso = "09:58 AM", estado = "Adentro"),
        Vehicle(placa = "GHI-321", tipo = "🏍️ Moto",  horaIngreso = "09:40 AM", horaSalida = "10:50 AM", estado = "Salió"),
        Vehicle(placa = "JKL-654", tipo = "🚗 Carro", horaIngreso = "09:22 AM", estado = "Adentro"),
        Vehicle(placa = "MNO-987", tipo = "🚗 Carro", horaIngreso = "08:55 AM", horaSalida = "10:10 AM", estado = "Salió"),
        Vehicle(placa = "PQR-111", tipo = "🏍️ Moto",  horaIngreso = "08:30 AM", estado = "Adentro")
    )

    private val _uiState = MutableStateFlow(
        HistoryUiState(vehiculosFiltrados = todosLosVehiculos)
    )
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    fun onFiltroChange(filtro: String) {
        val filtrados = when (filtro) {
            "Carros"  -> todosLosVehiculos.filter { it.getTipo().contains("Carro") }
            "Motos"   -> todosLosVehiculos.filter { it.getTipo().contains("Moto") }
            "Salidas" -> todosLosVehiculos.filter { !it.estaAdentro() }
            else      -> todosLosVehiculos
        }
        _uiState.update { it.copy(filtroSeleccionado = filtro, vehiculosFiltrados = filtrados) }
    }
}
