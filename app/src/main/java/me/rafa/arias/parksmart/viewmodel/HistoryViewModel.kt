package me.rafa.arias.parksmart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.rafa.arias.parksmart.repository.VehicleItem
import me.rafa.arias.parksmart.repository.VehicleRepository

data class HistoryUiState(
    val isLoading: Boolean = true,
    val filtroSeleccionado: String = "Todos",
    val todosVehiculos: List<VehicleItem> = emptyList(),
    val vehiculosFiltrados: List<VehicleItem> = emptyList(),
    val totalIngresos: Int = 0,
    val totalSalidas: Int = 0,
    val totalRecaudado: String = "\$0"
)

class HistoryViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            VehicleRepository.observeTodayVehiculos().collect { vehiculos ->
                val salidas = vehiculos.filter { it.estado == "Salió" }
                val recaudado = salidas.sumOf { it.total }
                val filtro = _uiState.value.filtroSeleccionado
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        todosVehiculos = vehiculos,
                        vehiculosFiltrados = filtrar(vehiculos, filtro),
                        totalIngresos = vehiculos.size,
                        totalSalidas = salidas.size,
                        totalRecaudado = formatPesos(recaudado)
                    )
                }
            }
        }
    }

    fun onFiltroChange(filtro: String) {
        _uiState.update {
            val filtered = filtrar(it.todosVehiculos, filtro)
            it.copy(
                filtroSeleccionado = filtro,
                vehiculosFiltrados = filtered,
                totalIngresos = filtered.size
            )
        }
    }

    private fun filtrar(lista: List<VehicleItem>, filtro: String) = when (filtro) {
        "Carros" -> lista.filter { it.tipo.contains("Carro") }
        "Motos" -> lista.filter { it.tipo.contains("Moto") }
        "Salidas" -> lista.filter { it.estado == "Salió" }
        else -> lista
    }

    private fun formatPesos(amount: Int): String =
        "\$${"%,d".format(amount).replace(",", ".")}"
}
