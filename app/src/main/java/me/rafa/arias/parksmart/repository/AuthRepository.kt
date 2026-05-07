package me.rafa.arias.parksmart.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun isLoggedIn(): Boolean = auth.currentUser != null

    fun signOut() = auth.signOut()

    suspend fun signIn(email: String, password: String): Result<Unit> =
        suspendCoroutine { cont ->
            auth.signInWithEmailAndPassword(email.trim(), password)
                .addOnSuccessListener { cont.resume(Result.success(Unit)) }
                .addOnFailureListener { cont.resume(Result.failure(it)) }
        }

    suspend fun register(
        email: String,
        password: String,
        nombre: String,
        cedula: String,
        telefono: String,
        nombreParqueadero: String,
        direccion: String,
        ciudad: String,
        nit: String,
        cuposCarros: Int,
        cuposMotos: Int,
        tarifaCarro: Int,
        tarifaMoto: Int,
        colorMarca: String
    ): Result<Unit> {
        val createResult = suspendCoroutine<Result<String>> { cont ->
            auth.createUserWithEmailAndPassword(email.trim(), password)
                .addOnSuccessListener { result ->
                    cont.resume(Result.success(result.user!!.uid))
                }
                .addOnFailureListener { cont.resume(Result.failure(it)) }
        }

        val uid = createResult.getOrElse { return Result.failure(it) }

        val operatorData = hashMapOf(
            "id" to uid,
            "nombre" to nombre.trim(),
            "cedula" to cedula.trim(),
            "email" to email.trim().lowercase(),
            "telefono" to telefono.trim(),
            "rol" to "Propietario",
            "parqueaderoId" to uid
        )

        val lotData = hashMapOf(
            "id" to uid,
            "nombre" to nombreParqueadero.trim(),
            "direccion" to direccion.trim(),
            "ciudad" to ciudad.trim(),
            "nit" to nit.trim(),
            "cuposCarros" to cuposCarros,
            "cuposMotos" to cuposMotos,
            "tarifaCarro" to tarifaCarro,
            "tarifaMoto" to tarifaMoto,
            "colorMarca" to colorMarca,
            "propietarioId" to uid,
            "activo" to true
        )

        return suspendCoroutine { cont ->
            val batch = db.batch()
            batch.set(db.collection("operators").document(uid), operatorData)
            batch.set(db.collection("parqueaderos").document(uid), lotData)
            batch.commit()
                .addOnSuccessListener { cont.resume(Result.success(Unit)) }
                .addOnFailureListener { cont.resume(Result.failure(it)) }
        }
    }
}
