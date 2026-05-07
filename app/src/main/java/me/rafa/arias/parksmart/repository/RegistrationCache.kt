package me.rafa.arias.parksmart.repository

object RegistrationCache {
    var email: String = ""
    var password: String = ""
    var nombre: String = ""
    var cedula: String = ""
    var telefono: String = ""

    fun clear() {
        email = ""; password = ""; nombre = ""; cedula = ""; telefono = ""
    }
}
