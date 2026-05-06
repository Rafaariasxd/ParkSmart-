package me.rafa.arias.parksmart.model

class Vehicle {

    private var id: String = ""
    private var placa: String = ""
    private var tipo: String = ""        // "Carro o Moto"
    private var horaIngreso: String = ""
    private var horaSalida: String? = null
    private var estado: String = "Adentro" // "entro  o Salió"
    private var operarioId: String = ""
    private var parqueaderoId: String = ""
    private var total: Int = 0

    // Constructor vacío
    constructor()

    constructor(
        id: String = "",
        placa: String,
        tipo: String,
        horaIngreso: String,
        horaSalida: String? = null,
        estado: String = "Adentro",
        operarioId: String = "",
        parqueaderoId: String = "",
        total: Int = 0
    ) {
        this.id = id
        this.placa = placa
        this.tipo = tipo
        this.horaIngreso = horaIngreso
        this.horaSalida = horaSalida
        this.estado = estado
        this.operarioId = operarioId
        this.parqueaderoId = parqueaderoId
        this.total = total
    }

    // Getters
    fun getId(): String = id
    fun getPlaca(): String = placa
    fun getTipo(): String = tipo
    fun getHoraIngreso(): String = horaIngreso
    fun getHoraSalida(): String? = horaSalida
    fun getEstado(): String = estado
    fun getOperarioId(): String = operarioId
    fun getParqueaderoId(): String = parqueaderoId
    fun getTotal(): Int = total

    // Setters
    fun setId(value: String) { id = value }
    fun setPlaca(value: String) { placa = value.uppercase() }
    fun setTipo(value: String) { tipo = value }
    fun setHoraIngreso(value: String) { horaIngreso = value }
    fun setHoraSalida(value: String?) { horaSalida = value }
    fun setEstado(value: String) { estado = value }
    fun setOperarioId(value: String) { operarioId = value }
    fun setParqueaderoId(value: String) { parqueaderoId = value }
    fun setTotal(value: Int) { total = value }

    // Lógica de negocio
    fun estaAdentro(): Boolean = estado == "Adentro"

    fun registrarSalida(hora: String, montoTotal: Int) {
        horaSalida = hora
        estado = "Salió"
        total = montoTotal
    }

    override fun toString(): String =
        "Vehicle(placa=$placa, tipo=$tipo, ingreso=$horaIngreso, estado=$estado)"
}
