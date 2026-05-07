package me.rafa.arias.parksmart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.rafa.arias.parksmart.repository.VehicleRepository

data class CheckoutDatosVehiculo(
    val id: String,
    val placa: String,
    val tipo: String,
    val horaIngreso: String,
    val horaSalida: String,
    val tiempoTotal: String,
    val total: String,
    val tarifa: String,
    val entryTimestampMs: Long,
    val tarifaCarro: Int,
    val tarifaMoto: Int
)

data class CheckoutUiState(
    val placaBuscada: String = "",
    val vehiculoEncontrado: Boolean = false,
    val vehiculoNoEncontrado: Boolean = false,
    val isSearching: Boolean = false,
    val isConfirming: Boolean = false,
    val vehiculoData: CheckoutDatosVehiculo? = null,
    val errorMessage: String = ""
)

sealed class CheckoutUiEvent {
    object SalidaRegistrada : CheckoutUiEvent()
}

class CheckoutViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<CheckoutUiEvent>()
    val uiEvent: SharedFlow<CheckoutUiEvent> = _uiEvent.asSharedFlow()

    fun onPlacaChange(value: String) {
        _uiState.update {
            it.copy(
                placaBuscada = value.uppercase(),
                vehiculoEncontrado = false,
                vehiculoNoEncontrado = false,
                errorMessage = ""
            )
        }
    }

    fun buscarVehiculo() {
        val placa = _uiState.value.placaBuscada.trim()
        if (placa.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isSearching = true, errorMessage = "") }
            VehicleRepository.buscarVehiculo(placa).fold(
                onSuccess = { result ->
                    if (result == null) {
                        _uiState.update { it.copy(isSearching = false, vehiculoNoEncontrado = true, vehiculoEncontrado = false) }
                    } else {
                        val c = VehicleRepository.calcularCheckout(result.entryTimestampMs, result.tipo, result.tarifaCarro, result.tarifaMoto)
                        _uiState.update {
                            it.copy(
                                isSearching = false,
                                vehiculoEncontrado = true,
                                vehiculoNoEncontrado = false,
                                vehiculoData = CheckoutDatosVehiculo(
                                    id = result.id,
                                    placa = result.placa,
                                    tipo = if (result.tipo == "Carro") "🚗 Carro" else "🏍️ Moto",
                                    horaIngreso = c.horaIngreso,
                                    horaSalida = c.horaSalida,
                                    tiempoTotal = c.tiempoTotal,
                                    total = c.total,
                                    tarifa = c.tarifa,
                                    entryTimestampMs = result.entryTimestampMs,
                                    tarifaCarro = result.tarifaCarro,
                                    tarifaMoto = result.tarifaMoto
                                )
                            )
                        }
                    }
                },
                onFailure = {
                    _uiState.update { it.copy(isSearching = false, errorMessage = "Error al buscar vehículo") }
                }
            )
        }
    }

    fun confirmarSalida() {
        val data = _uiState.value.vehiculoData ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isConfirming = true, errorMessage = "") }
            VehicleRepository.registrarSalida(
                vehiculoId = data.id,
                entryMs = data.entryTimestampMs,
                tipo = if (data.tipo.contains("Carro")) "Carro" else "Moto",
                tarifaCarro = data.tarifaCarro,
                tarifaMoto = data.tarifaMoto
            ).fold(
                onSuccess = { _uiEvent.emit(CheckoutUiEvent.SalidaRegistrada) },
                onFailure = { _uiState.update { it.copy(isConfirming = false, errorMessage = "Error al registrar salida") } }
            )
        }
    }

    fun limpiarBusqueda() {
        _uiState.update {
            it.copy(
                placaBuscada = "",
                vehiculoEncontrado = false,
                vehiculoNoEncontrado = false,
                vehiculoData = null
            )
        }
    }
}
