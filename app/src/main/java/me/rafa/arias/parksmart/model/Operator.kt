package me.rafa.arias.parksmart.model

class Operator {

    private var id: String = ""
    private var nombre: String = ""
    private var cedula: String = ""
    private var email: String = ""
    private var telefono: String = ""
    private var rol: String = "Operario"   // "Propietario u Operario"
    private var parqueaderoId: String = ""

    // Constructor vacío
    constructor()

    constructor(
        id: String = "",
        nombre: String,
        cedula: String,
        email: String,
        telefono: String,
        rol: String = "Operario",
        parqueaderoId: String = ""
    ) {
        this.id = id
        this.nombre = nombre
        this.cedula = cedula
        this.email = email
        this.telefono = telefono
        this.rol = rol
        this.parqueaderoId = parqueaderoId
    }

    // Getters
    fun getId(): String = id
    fun getNombre(): String = nombre
    fun getCedula(): String = cedula
    fun getEmail(): String = email
    fun getTelefono(): String = telefono
    fun getRol(): String = rol
    fun getParqueaderoId(): String = parqueaderoId

    // Setters
    fun setId(value: String) { id = value }
    fun setNombre(value: String) { nombre = value }
    fun setCedula(value: String) { cedula = value }
    fun setEmail(value: String) { email = value.trim().lowercase() }
    fun setTelefono(value: String) { telefono = value }
    fun setRol(value: String) { rol = value }
    fun setParqueaderoId(value: String) { parqueaderoId = value }

    // Lógica de negocio
    fun esPropietario(): Boolean = rol == "Propietario"

    override fun toString(): String =
        "Operator(nombre=$nombre, rol=$rol, parqueaderoId=$parqueaderoId)"
}
