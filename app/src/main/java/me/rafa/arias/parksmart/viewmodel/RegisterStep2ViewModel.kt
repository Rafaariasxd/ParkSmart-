package me.rafa.arias.parksmart.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.rafa.arias.parksmart.repository.AuthRepository
import me.rafa.arias.parksmart.repository.RegistrationCache

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
    val isLoading: Boolean = false,
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

    fun onNombreParqueaderoChange(value: String) { _uiState.update { it.copy(nombreParqueadero = value, errorMessage = "") } }
    fun onDireccionChange(value: String) { _uiState.update { it.copy(direccion = value, errorMessage = "") } }
    fun onCiudadChange(value: String) { _uiState.update { it.copy(ciudad = value, errorMessage = "") } }
    fun onNitChange(value: String) { _uiState.update { it.copy(nit = value, errorMessage = "") } }
    fun onCuposChange(value: String) { _uiState.update { it.copy(cupos = value, errorMessage = "") } }
    fun onCuposMotosChange(value: String) { _uiState.update { it.copy(cuposMotos = value, errorMessage = "") } }
    fun onTarifaCarroChange(value: String) { _uiState.update { it.copy(tarifaCarro = value, errorMessage = "") } }
    fun onTarifaMotoChange(value: String) { _uiState.update { it.copy(tarifaMoto = value, errorMessage = "") } }
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

        val colorHex = "#%06X".format(0xFFFFFF and s.colorSeleccionado.toArgb())

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = "") }
            AuthRepository.register(
                email = RegistrationCache.email,
                password = RegistrationCache.password,
                nombre = RegistrationCache.nombre,
                cedula = RegistrationCache.cedula,
                telefono = RegistrationCache.telefono,
                nombreParqueadero = s.nombreParqueadero,
                direccion = s.direccion,
                ciudad = s.ciudad,
                nit = s.nit,
                cuposCarros = s.cupos.toInt(),
                cuposMotos = s.cuposMotos.toIntOrNull() ?: 0,
                tarifaCarro = s.tarifaCarro.toIntOrNull() ?: 0,
                tarifaMoto = s.tarifaMoto.toIntOrNull() ?: 0,
                colorMarca = colorHex
            ).fold(
                onSuccess = {
                    RegistrationCache.clear()
                    _uiEvent.emit(RegisterStep2UiEvent.NavigateToSuccess(s.nombreParqueadero, s.cupos))
                },
                onFailure = { e ->
                    val msg = when (e) {
                        is FirebaseAuthUserCollisionException -> "Ya existe una cuenta con ese correo"
                        else -> "Error al registrar. Intenta de nuevo"
                    }
                    _uiState.update { it.copy(isLoading = false, errorMessage = msg) }
                }
            )
        }
    }
}
