package me.rafa.arias.parksmart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.rafa.arias.parksmart.repository.VehicleItem
import me.rafa.arias.parksmart.repository.VehicleRepository
import java.util.Calendar

data class ReportsUiState(
    val isLoading: Boolean = true,
    val periodoSeleccionado: String = "Hoy",
    val ingresos: String = "\$0",
    val vehiculos: String = "0",
    val carros: String = "0",
    val motos: String = "0",
    val ocupacion: String = "0%",
    val barrasPorHora: List<Pair<String, Float>> = emptyList(),
    val horaPico: String = "--"
)

class ReportsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ReportsUiState())
    val uiState: StateFlow<ReportsUiState> = _uiState.asStateFlow()

    private var capacidadTotal: Int = 1
    private var collectJob: Job? = null

    init {
        viewModelScope.launch {
            VehicleRepository.loadParkingLotInfo().onSuccess { info ->
                capacidadTotal = (info.cuposCarros + info.cuposMotos).coerceAtLeast(1)
            }
        }
        cargarPeriodo("Hoy")
    }

    fun onPeriodoChange(periodo: String) = cargarPeriodo(periodo)

    private fun cargarPeriodo(periodo: String) {
        _uiState.update { it.copy(isLoading = true, periodoSeleccionado = periodo) }
        collectJob?.cancel()
        collectJob = viewModelScope.launch {
            VehicleRepository.observeVehiculosDesde(startMsParaPeriodo(periodo)).collect { vehiculos ->
                val salidas = vehiculos.filter { it.estado == "Salió" }
                val carros = vehiculos.count { it.tipo.contains("Carro") }
                val motos = vehiculos.count { it.tipo.contains("Moto") }
                val (barras, horaPico) = calcularBarras(vehiculos)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        ingresos = VehicleRepository.formatPesos(salidas.sumOf { v -> v.total }),
                        vehiculos = vehiculos.size.toString(),
                        carros = carros.toString(),
                        motos = motos.toString(),
                        ocupacion = calcularOcupacion(vehiculos, periodo),
                        barrasPorHora = barras,
                        horaPico = horaPico
                    )
                }
            }
        }
    }

    private fun startMsParaPeriodo(periodo: String): Long {
        val cal = Calendar.getInstance()
        when (periodo) {
            "Semana" -> cal.add(Calendar.DAY_OF_YEAR, -6)
            "Mes" -> cal.add(Calendar.DAY_OF_YEAR, -29)
        }
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private fun calcularOcupacion(vehiculos: List<VehicleItem>, periodo: String): String {
        if (vehiculos.isEmpty()) return "0%"
        val dias = when (periodo) { "Semana" -> 7; "Mes" -> 30; else -> 1 }
        val pct = (vehiculos.size.toFloat() / dias / capacidadTotal * 100)
            .toInt().coerceAtMost(100)
        return "$pct%"
    }

    private fun calcularBarras(vehiculos: List<VehicleItem>): Pair<List<Pair<String, Float>>, String> {
        if (vehiculos.isEmpty()) return Pair((7..16).map { Pair(it.toString(), 0f) }, "--")
        val cal = Calendar.getInstance()
        val porHora = vehiculos.groupBy { v ->
            cal.timeInMillis = v.entryMs
            cal.get(Calendar.HOUR_OF_DAY)
        }
        val horas = porHora.keys.sorted().take(10).ifEmpty { (7..16).toList() }
        val maxCount = porHora.values.maxOf { it.size }.toFloat().coerceAtLeast(1f)
        val barras = horas.map { h -> Pair(h.toString(), (porHora[h]?.size ?: 0) / maxCount) }
        val horaPico = porHora.maxByOrNull { it.value.size }?.key?.let { h ->
            val display = if (h == 0) 12 else if (h > 12) h - 12 else h
            "$display:00 ${if (h >= 12) "PM" else "AM"}"
        } ?: "--"
        return Pair(barras, horaPico)
    }
}
