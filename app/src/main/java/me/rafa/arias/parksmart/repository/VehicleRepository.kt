package me.rafa.arias.parksmart.repository

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.ceil

data class VehicleItem(
    val id: String,
    val placa: String,
    val tipo: String,
    val horaIngreso: String,
    val horaSalida: String?,
    val estado: String,
    val total: Int,
    val entryMs: Long
)

data class ParkingLotInfo(
    val nombre: String,
    val cuposCarros: Int,
    val cuposMotos: Int,
    val tarifaCarro: Int,
    val tarifaMoto: Int
)

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

    private fun normalizePlaca(placa: String): String =
        placa.trim().uppercase().replace("-", "")

    private fun formatPlacaDisplay(raw: String): String {
        val clean = raw.replace("-", "")
        return if (clean.length > 3) "${clean.take(3)}-${clean.drop(3)}" else clean
    }

    private fun startOfTodayMs(): Long = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    private fun docToVehicleItem(doc: DocumentSnapshot): VehicleItem {
        val tipoRaw = doc.getString("tipo") ?: "Carro"
        val entryTs = doc.getTimestamp("horaIngreso") ?: Timestamp.now()
        val entryMs = entryTs.toDate().time
        val exitTs = doc.getTimestamp("horaSalida")
        return VehicleItem(
            id = doc.id,
            placa = formatPlacaDisplay(doc.getString("placa") ?: ""),
            tipo = if (tipoRaw == "Carro") "🚗 Carro" else "🏍️ Moto",
            horaIngreso = timeFmt.format(Date(entryMs)),
            horaSalida = exitTs?.let { timeFmt.format(it.toDate()) },
            estado = doc.getString("estado") ?: "Adentro",
            total = (doc.getLong("total") ?: 0).toInt(),
            entryMs = entryMs
        )
    }

    fun observeVehiculosDesde(startMs: Long): Flow<List<VehicleItem>> = callbackFlow {
        val uid = uid()
        val listener = db.collection("vehiculos")
            .whereEqualTo("parqueaderoId", uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) { close(error); return@addSnapshotListener }
                val items = snapshot?.documents
                    ?.filter { (it.getTimestamp("horaIngreso")?.toDate()?.time ?: 0L) >= startMs }
                    ?.map { docToVehicleItem(it) }
                    ?.sortedByDescending { it.entryMs }
                    ?: emptyList()
                trySend(items)
            }
        awaitClose { listener.remove() }
    }

    fun observeTodayVehiculos(): Flow<List<VehicleItem>> = callbackFlow {
        val uid = uid()
        val startOfDay = startOfTodayMs()
        val listener = db.collection("vehiculos")
            .whereEqualTo("parqueaderoId", uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) { close(error); return@addSnapshotListener }
                val items = snapshot?.documents
                    ?.filter { (it.getTimestamp("horaIngreso")?.toDate()?.time ?: 0L) >= startOfDay }
                    ?.map { docToVehicleItem(it) }
                    ?.sortedByDescending { it.entryMs }
                    ?: emptyList()
                trySend(items)
            }
        awaitClose { listener.remove() }
    }

    fun observeVehiculosAdentro(): Flow<Int> = callbackFlow {
        val uid = uid()
        val listener = db.collection("vehiculos")
            .whereEqualTo("parqueaderoId", uid)
            .whereEqualTo("estado", "Adentro")
            .addSnapshotListener { snapshot, error ->
                if (error != null) { close(error); return@addSnapshotListener }
                trySend(snapshot?.documents?.size ?: 0)
            }
        awaitClose { listener.remove() }
    }

    suspend fun loadParkingLotInfo(): Result<ParkingLotInfo> = suspendCoroutine { cont ->
        db.collection("parqueaderos").document(uid()).get()
            .addOnSuccessListener { doc ->
                cont.resume(Result.success(ParkingLotInfo(
                    nombre = doc.getString("nombre") ?: "Mi Parqueadero",
                    cuposCarros = (doc.getLong("cuposCarros") ?: 0).toInt(),
                    cuposMotos = (doc.getLong("cuposMotos") ?: 0).toInt(),
                    tarifaCarro = (doc.getLong("tarifaCarro") ?: 2000).toInt(),
                    tarifaMoto = (doc.getLong("tarifaMoto") ?: 1000).toInt()
                )))
            }
            .addOnFailureListener { cont.resume(Result.failure(it)) }
    }

    suspend fun loadOperatorName(): Result<String> = suspendCoroutine { cont ->
        db.collection("operators").document(uid()).get()
            .addOnSuccessListener { doc ->
                cont.resume(Result.success(doc.getString("nombre") ?: "Operario"))
            }
            .addOnFailureListener { cont.resume(Result.failure(it)) }
    }

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

    fun formatPesos(amount: Int): String =
        "\$${"%,d".format(amount).replace(",", ".")}"

    suspend fun registrarIngreso(placa: String, tipo: String): Result<Unit> =
        suspendCoroutine { cont ->
            val doc = hashMapOf(
                "placa" to normalizePlaca(placa),
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
                    val placaNorm = normalizePlaca(placa)
                    val doc = result.documents.firstOrNull {
                        normalizePlaca(it.getString("placa") ?: "") == placaNorm
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
