package me.rafa.arias.parksmart.model

class ParkingLot {

    private var id: String = ""
    private var nombre: String = ""
    private var direccion: String = ""
    private var ciudad: String = ""
    private var nit: String = ""
    private var cuposCarros: Int = 0
    private var cuposMotos: Int = 0
    private var tarifaCarro: Int = 0   // precio por hora en pesos
    private var tarifaMoto: Int = 0    // precio por hora en pesos
    private var colorMarca: String = "#90C749"
    private var propietarioId: String = ""
    private var activo: Boolean = true

    // Constructor vacío requerido por Firebase Firestore
    constructor()

    constructor(
        id: String = "",
        nombre: String,
        direccion: String,
        ciudad: String,
        nit: String,
        cuposCarros: Int,
        cuposMotos: Int,
        tarifaCarro: Int,
        tarifaMoto: Int,
        colorMarca: String = "#90C749",
        propietarioId: String = ""
    ) {
        this.id = id
        this.nombre = nombre
        this.direccion = direccion
        this.ciudad = ciudad
        this.nit = nit
        this.cuposCarros = cuposCarros
        this.cuposMotos = cuposMotos
        this.tarifaCarro = tarifaCarro
        this.tarifaMoto = tarifaMoto
        this.colorMarca = colorMarca
        this.propietarioId = propietarioId
    }

    // Getters
    fun getId(): String = id
    fun getNombre(): String = nombre
    fun getDireccion(): String = direccion
    fun getCiudad(): String = ciudad
    fun getNit(): String = nit
    fun getCuposCarros(): Int = cuposCarros
    fun getCuposMotos(): Int = cuposMotos
    fun getTarifaCarro(): Int = tarifaCarro
    fun getTarifaMoto(): Int = tarifaMoto
    fun getColorMarca(): String = colorMarca
    fun getPropietarioId(): String = propietarioId
    fun isActivo(): Boolean = activo

    // Setters
    fun setId(value: String) { id = value }
    fun setNombre(value: String) { nombre = value }
    fun setDireccion(value: String) { direccion = value }
    fun setCiudad(value: String) { ciudad = value }
    fun setNit(value: String) { nit = value }
    fun setCuposCarros(value: Int) { cuposCarros = value }
    fun setCuposMotos(value: Int) { cuposMotos = value }
    fun setTarifaCarro(value: Int) { tarifaCarro = value }
    fun setTarifaMoto(value: Int) { tarifaMoto = value }
    fun setColorMarca(value: String) { colorMarca = value }
    fun setPropietarioId(value: String) { propietarioId = value }
    fun setActivo(value: Boolean) { activo = value }

    // Lógica de negocio
    fun getCuposTotales(): Int = cuposCarros + cuposMotos

    fun calcularCobro(tipo: String, horas: Int): Int {
        return when (tipo) {
            "Carro" -> tarifaCarro * horas
            "Moto"  -> tarifaMoto * horas
            else -> 0
        }
    }

    override fun toString(): String =
        "ParkingLot(nombre=$nombre, ciudad=$ciudad, cupos=${getCuposTotales()})"
}
