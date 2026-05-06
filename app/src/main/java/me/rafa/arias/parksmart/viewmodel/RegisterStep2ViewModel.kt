package me.rafa.arias.parksmart.viewmodel

import androidx.compose.ui.graphics.Color
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

data class RegisterStep2UiState(
    val nombreParqueadero: String = "",
    val direccion: String = "",
    val ciudad: String = "",
    val nit: String = "",
    val cupos: String = "",
    val cuposMotos: String = "",
    val tarifaCarro: String = "",
    val tarifaMoto: String = "",
    val colorSeleccionado: Color = Color(0xFF90C749),
    val errorMessage: String = ""
)

sealed class RegisterStep2UiEvent {
    data class NavigateToSuccess(val nombre: String, val cupos: String) : RegisterStep2UiEvent()
}

class RegisterStep2ViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterStep2UiState())
    val uiState: StateFlow<RegisterStep2UiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<RegisterStep2UiEvent>()
    val uiEvent: SharedFlow<RegisterStep2UiEvent> = _uiEvent.asSharedFlow()

    fun onNombreParqueaderoChange(value: String) { _uiState.update { it.copy(nombreParqueadero = value) } }
    fun onDireccionChange(value: String) { _uiState.update { it.copy(direccion = value) } }
    fun onCiudadChange(value: String) { _uiState.update { it.copy(ciudad = value) } }
    fun onNitChange(value: String) { _uiState.update { it.copy(nit = value) } }
    fun onCuposChange(value: String) { _uiState.update { it.copy(cupos = value) } }
    fun onCuposMotosChange(value: String) { _uiState.update { it.copy(cuposMotos = value) } }
    fun onTarifaCarroChange(value: String) { _uiState.update { it.copy(tarifaCarro = value) } }
    fun onTarifaMotoChange(value: String) { _uiState.update { it.copy(tarifaMoto = value) } }
    fun onColorChange(color: Color) { _uiState.update { it.copy(colorSeleccionado = color) } }

    fun onRegisterClick() {
        val s = _uiState.value
        val error = when {
            s.nombreParqueadero.isBlank() || s.direccion.isBlank() ||
                    s.ciudad.isBlank() || s.nit.isBlank() ->
                "Por favor completa todos los campos del negocio"
            s.cupos.isBlank() || s.tarifaCarro.isBlank() || s.tarifaMoto.isBlank() ->
                "Completa la capacidad y tarifas"
            s.nit.length < 9 ->
                "El NIT debe tener al menos 9 dígitos"
            s.cupos.toIntOrNull() == null || s.cupos.toInt() <= 0 ->
                "El número de cupos debe ser mayor a 0"
            else -> ""
        }
        if (error.isNotEmpty()) {
            _uiState.update { it.copy(errorMessage = error) }
            return
        }
        _uiState.update { it.copy(errorMessage = "") }
        // Aquí después se conecta Firestore
        viewModelScope.launch {
            _uiEvent.emit(RegisterStep2UiEvent.NavigateToSuccess(s.nombreParqueadero, s.cupos))
        }
    }
}
