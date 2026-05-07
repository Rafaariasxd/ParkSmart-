package me.rafa.arias.parksmart.repository

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.ceil

data class VehicleSearchResult(
    val id: String,
    val placa: String,
    val tipo: String,
    val entryTimestampMs: Long,
    val tarifaCarro: Int,
    val tarifaMoto: Int
)

data class CheckoutCalculo(
    val horaIngreso: String,
    val horaSalida: String,
    val tiempoTotal: String,
    val total: String,
    val tarifa: String,
    val totalInt: Int
)

object VehicleRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val timeFmt = SimpleDateFormat("hh:mm a", Locale("es", "CO"))

    private fun uid(): String = auth.currentUser?.uid ?: ""

    fun calcularCheckout(
        entryMs: Long,
        tipo: String,
        tarifaCarro: Int,
        tarifaMoto: Int
    ): CheckoutCalculo {
        val ahora = System.currentTimeMillis()
        val diffMin = ((ahora - entryMs) / 60000).toInt().coerceAtLeast(1)
        val horas = diffMin / 60
        val mins = diffMin % 60
        val horasTotal = ceil(diffMin / 60.0).toInt().coerceAtLeast(1)
        val tarifa = if (tipo == "Carro") tarifaCarro else tarifaMoto
        val total = tarifa * horasTotal

        return CheckoutCalculo(
            horaIngreso = timeFmt.format(Date(entryMs)),
            horaSalida = timeFmt.format(Date(ahora)),
            tiempoTotal = if (horas > 0) "${horas}h ${mins}min" else "${mins}min",
            total = formatPesos(total),
            tarifa = "${formatPesos(tarifa)}/hora",
            totalInt = total
        )
    }

    private fun formatPesos(amount: Int): String =
        "\$${"%,d".format(amount).replace(",", ".")}"

    suspend fun registrarIngreso(placa: String, tipo: String): Result<Unit> =
        suspendCoroutine { cont ->
            val doc = hashMapOf(
                "placa" to placa.trim().uppercase(),
                "tipo" to tipo,
                "horaIngreso" to Timestamp.now(),
                "horaSalida" to null,
                "estado" to "Adentro",
                "operarioId" to uid(),
                "parqueaderoId" to uid(),
                "total" to 0
            )
            db.collection("vehiculos").add(doc)
                .addOnSuccessListener { cont.resume(Result.success(Unit)) }
                .addOnFailureListener { cont.resume(Result.failure(it)) }
        }

    suspend fun buscarVehiculo(placa: String): Result<VehicleSearchResult?> {
        val uid = uid()
        val lotResult = suspendCoroutine<Result<Pair<Int, Int>>> { cont ->
            db.collection("parqueaderos").document(uid).get()
                .addOnSuccessListener { doc ->
                    val tarifaCarro = (doc.getLong("tarifaCarro") ?: 2000).toInt()
                    val tarifaMoto = (doc.getLong("tarifaMoto") ?: 1000).toInt()
                    cont.resume(Result.success(tarifaCarro to tarifaMoto))
                }
                .addOnFailureListener { cont.resume(Result.failure(it)) }
        }
        val (tarifaCarro, tarifaMoto) = lotResult.getOrElse { return Result.failure(it) }

        return suspendCoroutine { cont ->
            db.collection("vehiculos")
                .whereEqualTo("parqueaderoId", uid)
                .whereEqualTo("estado", "Adentro")
                .get()
                .addOnSuccessListener { result ->
                    val doc = result.documents.firstOrNull {
                        it.getString("placa") == placa.trim().uppercase()
                    }
                    if (doc == null) {
                        cont.resume(Result.success(null))
                    } else {
                        val entryTs = doc.getTimestamp("horaIngreso") ?: Timestamp.now()
                        cont.resume(Result.success(
                            VehicleSearchResult(
                                id = doc.id,
                                placa = doc.getString("placa") ?: placa,
                                tipo = doc.getString("tipo") ?: "Carro",
                                entryTimestampMs = entryTs.toDate().time,
                                tarifaCarro = tarifaCarro,
                                tarifaMoto = tarifaMoto
                            )
                        ))
                    }
                }
                .addOnFailureListener { cont.resume(Result.failure(it)) }
        }
    }

    suspend fun registrarSalida(
        vehiculoId: String,
        entryMs: Long,
        tipo: String,
        tarifaCarro: Int,
        tarifaMoto: Int
    ): Result<Unit> {
        val calculo = calcularCheckout(entryMs, tipo, tarifaCarro, tarifaMoto)
        return suspendCoroutine { cont ->
            db.collection("vehiculos").document(vehiculoId).update(
                mapOf(
                    "horaSalida" to Timestamp.now(),
                    "estado" to "Salió",
                    "total" to calculo.totalInt
                )
            )
                .addOnSuccessListener { cont.resume(Result.success(Unit)) }
                .addOnFailureListener { cont.resume(Result.failure(it)) }
        }
    }
}
