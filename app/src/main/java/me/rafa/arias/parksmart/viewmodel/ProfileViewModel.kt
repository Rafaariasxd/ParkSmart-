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
import me.rafa.arias.parksmart.repository.AuthRepository
import me.rafa.arias.parksmart.repository.VehicleRepository

data class ProfileUiState(
    val isLoading: Boolean = true,
    val nombre: String = "",
    val rol: String = "",
    val sede: String = "",
    val vehiculosHoy: String = "–",
    val recaudadoHoy: String = "\$0",
    val showLogoutDialog: Boolean = false
)

sealed class ProfileUiEvent {
    object NavigateToLogin : ProfileUiEvent()
}

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<ProfileUiEvent>()
    val uiEvent: SharedFlow<ProfileUiEvent> = _uiEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            VehicleRepository.loadProfileInfo()
                .onSuccess { info ->
                    val sede = listOf(info.nombreParqueadero, info.ciudad)
                        .filter { it.isNotBlank() }
                        .joinToString(", ")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            nombre = info.nombre,
                            rol = info.rol,
                            sede = sede
                        )
                    }
                }
                .onFailure { _uiState.update { it.copy(isLoading = false) } }
        }

        viewModelScope.launch {
            VehicleRepository.observeTodayVehiculos().collect { vehiculos ->
                val recaudado = vehiculos.filter { it.estado == "Salió" }.sumOf { it.total }
                _uiState.update {
                    it.copy(
                        vehiculosHoy = "${vehiculos.size} registrados",
                        recaudadoHoy = VehicleRepository.formatPesos(recaudado)
                    )
                }
            }
        }
    }

    fun onShowLogoutDialog() {
        _uiState.update { it.copy(showLogoutDialog = true) }
    }

    fun onDismissLogoutDialog() {
        _uiState.update { it.copy(showLogoutDialog = false) }
    }

    fun onLogoutConfirm() {
        _uiState.update { it.copy(showLogoutDialog = false) }
        AuthRepository.signOut()
        viewModelScope.launch {
            _uiEvent.emit(ProfileUiEvent.NavigateToLogin)
        }
    }
}
