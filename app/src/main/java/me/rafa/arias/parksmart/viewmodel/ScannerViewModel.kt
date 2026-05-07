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

data class ScannerUiState(
    val placaDetectada: String = "",
    val tipoVehiculo: String = "Carro",
    val escaneando: Boolean = true,
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)

sealed class ScannerUiEvent {
    object IngresoRegistrado : ScannerUiEvent()
}

class ScannerViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ScannerUiState())
    val uiState: StateFlow<ScannerUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<ScannerUiEvent>()
    val uiEvent: SharedFlow<ScannerUiEvent> = _uiEvent.asSharedFlow()

    fun onPlacaChange(value: String) {
        if (value.length <= 7) _uiState.update { it.copy(placaDetectada = value.uppercase(), errorMessage = "") }
    }

    fun onTipoVehiculoChange(tipo: String) {
        _uiState.update { it.copy(tipoVehiculo = tipo) }
    }

    fun onSimularEscaneo() {
        _uiState.update { it.copy(escaneando = false) }
    }

    fun onVolverAEscanear() {
        _uiState.update { it.copy(escaneando = true, placaDetectada = "") }
    }

    fun registrarIngreso() {
        val s = _uiState.value
        if (s.placaDetectada.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Ingresa la placa del vehículo") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = "") }
            VehicleRepository.registrarIngreso(s.placaDetectada, s.tipoVehiculo).fold(
                onSuccess = { _uiEvent.emit(ScannerUiEvent.IngresoRegistrado) },
                onFailure = { _uiState.update { it.copy(isLoading = false, errorMessage = "Error al registrar ingreso") } }
            )
        }
    }
}
