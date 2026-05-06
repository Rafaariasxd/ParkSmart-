package me.rafa.arias.parksmart.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CheckoutUiState(
    val placaBuscada: String = "",
    val vehiculoEncontrado: Boolean = false,
    val vehiculoNoEncontrado: Boolean = false,
    val datosVehiculo: Map<String, String> = mapOf(
        "placa" to "ABC-123",
        "tipo" to "🚗 Carro",
        "horaIngreso" to "10:32 AM",
        "horaSalida" to "01:15 PM",
        "tiempoTotal" to "2h 43min",
        "total" to "$5.400",
        "tarifa" to "$2.000/hora"
    )
)

class CheckoutViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    fun onPlacaChange(value: String) {
        _uiState.update {
            it.copy(
                placaBuscada = value.uppercase(),
                vehiculoEncontrado = false,
                vehiculoNoEncontrado = false
            )
        }
    }

    fun buscarVehiculo() {
        // Aquí después se conecta Firebase
        val placa = _uiState.value.placaBuscada
        if (placa == "ABC-123") {
            _uiState.update { it.copy(vehiculoEncontrado = true, vehiculoNoEncontrado = false) }
        } else {
            _uiState.update { it.copy(vehiculoNoEncontrado = true, vehiculoEncontrado = false) }
        }
    }

    fun limpiarBusqueda() {
        _uiState.update {
            it.copy(
                placaBuscada = "",
                vehiculoEncontrado = false,
                vehiculoNoEncontrado = false
            )
        }
    }
}
