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

data class ProfileUiState(
    val nombre: String = "Carlos Gómez",
    val rol: String = "Operario",
    val sede: String = "Cabecera, Bucaramanga",
    val turno: String = "7:00 AM - 3:00 PM",
    val vehiculosHoy: String = "47 registrados",
    val recaudadoHoy: String = "\$94.200",
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
