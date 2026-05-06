package me.rafa.arias.parksmart.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ReportsUiState(
    val periodoSeleccionado: String = "Hoy",
    val datos: Map<String, String> = mapOf(
        "ingresos" to "$94.200",
        "vehiculos" to "47",
        "carros" to "31",
        "motos" to "16",
        "ocupacion" to "80%"
    ),
    val barrasPorHora: List<Pair<String, Float>> = listOf(
        Pair("7", 0.3f),  Pair("8", 0.5f),  Pair("9", 0.7f),
        Pair("10", 1.0f), Pair("11", 0.6f), Pair("12", 0.8f),
        Pair("13", 0.4f), Pair("14", 0.55f),Pair("15", 0.75f),
        Pair("16", 0.45f)
    )
)

class ReportsViewModel : ViewModel() {

    private val datosPorPeriodo = mapOf(
        "Hoy" to mapOf(
            "ingresos" to "$94.200", "vehiculos" to "47",
            "carros" to "31", "motos" to "16", "ocupacion" to "80%"
        ),
        "Semana" to mapOf(
            "ingresos" to "$580.400", "vehiculos" to "312",
            "carros" to "198", "motos" to "114", "ocupacion" to "74%"
        ),
        "Mes" to mapOf(
            "ingresos" to "$2.340.000", "vehiculos" to "1.248",
            "carros" to "820", "motos" to "428", "ocupacion" to "78%"
        )
    )

    private val _uiState = MutableStateFlow(ReportsUiState())
    val uiState: StateFlow<ReportsUiState> = _uiState.asStateFlow()

    fun onPeriodoChange(periodo: String) {
        _uiState.update {
            it.copy(
                periodoSeleccionado = periodo,
                datos = datosPorPeriodo[periodo] ?: datosPorPeriodo["Hoy"]!!
            )
        }
    }
}
