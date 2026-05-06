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

data class RegisterStep1UiState(
    val nombre: String = "",
    val cedula: String = "",
    val email: String = "",
    val telefono: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordVisible: Boolean = false,
    val confirmPasswordVisible: Boolean = false,
    val errorMessage: String = ""
)

sealed class RegisterStep1UiEvent {
    object NavigateToStep2 : RegisterStep1UiEvent()
}

class RegisterStep1ViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterStep1UiState())
    val uiState: StateFlow<RegisterStep1UiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<RegisterStep1UiEvent>()
    val uiEvent: SharedFlow<RegisterStep1UiEvent> = _uiEvent.asSharedFlow()

    fun onNombreChange(value: String) { _uiState.update { it.copy(nombre = value) } }
    fun onCedulaChange(value: String) { _uiState.update { it.copy(cedula = value) } }
    fun onEmailChange(value: String) { _uiState.update { it.copy(email = value) } }
    fun onTelefonoChange(value: String) { _uiState.update { it.copy(telefono = value) } }
    fun onPasswordChange(value: String) { _uiState.update { it.copy(password = value) } }
    fun onConfirmPasswordChange(value: String) { _uiState.update { it.copy(confirmPassword = value) } }
    fun togglePasswordVisibility() { _uiState.update { it.copy(passwordVisible = !it.passwordVisible) } }
    fun toggleConfirmPasswordVisibility() { _uiState.update { it.copy(confirmPasswordVisible = !it.confirmPasswordVisible) } }

    fun onNextClick() {
        val s = _uiState.value
        val error = when {
            s.nombre.isBlank() || s.cedula.isBlank() || s.email.isBlank() || s.telefono.isBlank() ->
                "Por favor completa todos los campos"
            s.password.length < 6 ->
                "La contraseña debe tener al menos 6 caracteres"
            s.password != s.confirmPassword ->
                "Las contraseñas no coinciden"
            else -> ""
        }
        if (error.isNotEmpty()) {
            _uiState.update { it.copy(errorMessage = error) }
            return
        }
        _uiState.update { it.copy(errorMessage = "") }
        viewModelScope.launch { _uiEvent.emit(RegisterStep1UiEvent.NavigateToStep2) }
    }
}
