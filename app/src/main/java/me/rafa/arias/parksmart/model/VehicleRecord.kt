package me.rafa.arias.parksmart.model

data class VehicleRecord(
    val placa: String,
    val tipo: String,
    val horaIngreso: String,
    val horaSalida: String?,
    val estado: String
)
