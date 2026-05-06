package me.rafa.arias.parksmart.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ScannerUiState(
    val placaDetectada: String = "ABC-123",
    val tipoVehiculo: String = "Carro",
    val escaneando: Boolean = true
)

class ScannerViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ScannerUiState())
    val uiState: StateFlow<ScannerUiState> = _uiState.asStateFlow()

    fun onPlacaChange(value: String) {
        if (value.length <= 7) {
            _uiState.update { it.copy(placaDetectada = value.uppercase()) }
        }
    }

    fun onTipoVehiculoChange(tipo: String) {
        _uiState.update { it.copy(tipoVehiculo = tipo) }
    }

    fun onSimularEscaneo() {
        _uiState.update { it.copy(escaneando = false) }
    }

    fun onVolverAEscanear() {
        _uiState.update { it.copy(escaneando = true) }
    }
}
