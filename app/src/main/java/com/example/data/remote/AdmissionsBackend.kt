package com.example.data.remote

import android.content.Context
import android.util.Log
import com.example.data.model.CampusVisit
import com.example.data.model.EventRegistration
import com.example.data.model.Lead
import com.example.data.model.LeadStatus
import com.example.data.model.VisitStatus
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import org.json.JSONArray
import org.json.JSONObject

/** La cuenta inició sesión pero no tiene el documento admins/{uid}. */
class NotAdminException : Exception("Tu cuenta no tiene permisos de Admisiones")

/** Mensaje en español para mostrar en el acceso administrativo. */
fun adminSignInErrorMessage(error: Throwable): String = when (error) {
    is NotAdminException -> "Tu cuenta no tiene permisos de Admisiones."
    is FirebaseAuthInvalidUserException,
    is FirebaseAuthInvalidCredentialsException -> "Correo o contraseña incorrectos."
    is FirebaseNetworkException -> "Sin conexión a internet. Intenta de nuevo."
    else -> "No se pudo iniciar sesión. Intenta de nuevo."
}

/**
 * Puente entre la app y Firestore.
 *
 * Aspirantes: leads, visitas e inscripciones entran a una bandeja de salida persistente
 * (SharedPreferences) y se envían con una sesión anónima; se reintenta al abrir la app,
 * así nada se pierde sin conexión. El ID del documento es el `remoteId` local, por lo que
 * reenviar nunca duplica. El aspirante solo puede crear documentos (ver firestore.rules).
 *
 * Admisiones: inicia sesión con correo/contraseña, escucha las colecciones en tiempo real
 * y escribe de vuelta los cambios de estado, asistencia y borrados.
 */
class AdmissionsBackend(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences("admissions_outbox", Context.MODE_PRIVATE)
    private val lock = Any()
    private val inFlight = mutableSetOf<String>()
    private var signingIn = false

    private val firebaseReady: Boolean by lazy {
        runCatching { FirebaseAuth.getInstance(); FirebaseFirestore.getInstance() }
            .onFailure { Log.w(TAG, "Firebase no disponible (¿falta google-services.json?)", it) }
            .isSuccess
    }

    // ---------------------------------------------------------------- Aspirantes

    fun pushLead(lead: Lead, source: String = "app") {
        val id = lead.remoteId ?: return
        enqueue(
            "leads", id,
            mapOf(
                "fullName" to lead.fullName.trim().take(120),
                "phone" to lead.phone.trim().take(20),
                "email" to lead.email.trim().take(160),
                "cityOrSchool" to lead.cityOrSchool.take(160),
                "programInterest" to lead.programInterest.take(160),
                "degreeLevel" to lead.degreeLevel.take(40),
                "status" to lead.status.name,
                "notes" to lead.notes.take(1000),
                "source" to source.take(40),
                "createdAt" to lead.createdAt
            )
        )
    }

    fun pushVisit(visit: CampusVisit) {
        val id = visit.remoteId ?: return
        enqueue(
            "visits", id,
            mapOf(
                "studentName" to visit.studentName.trim().take(120),
                "phone" to visit.phone.trim().take(20),
                "email" to visit.email.trim().take(160),
                "campus" to visit.campus.take(120),
                "dateText" to visit.dateText.take(80),
                "timeSlot" to visit.timeSlot.take(60),
                "programInterest" to visit.programInterest.take(160),
                "visitorCount" to visit.visitorCount.coerceIn(1, 20),
                "school" to visit.school.take(160),
                "status" to visit.status.name,
                "createdAt" to visit.createdAt
            )
        )
    }

    fun pushRegistration(reg: EventRegistration) {
        val id = reg.remoteId ?: return
        enqueue(
            "event_registrations", id,
            mapOf(
                "eventId" to reg.eventId,
                "eventTitle" to reg.eventTitle.take(160),
                "fullName" to reg.fullName.trim().take(120),
                "phone" to reg.phone.trim().take(20),
                "email" to reg.email.trim().take(160),
                "schoolOrOrigin" to reg.schoolOrOrigin.take(160),
                "ticketCode" to reg.ticketCode.take(30),
                "attended" to false,
                "createdAt" to reg.registeredAt
            )
        )
    }

    /** Reintenta lo pendiente. Llamar al abrir la app y después de cada registro nuevo. */
    fun flush() {
        if (!firebaseReady) return
        if (synchronized(lock) { readOutbox().length() == 0 }) return
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            sendPending()
            return
        }
        synchronized(lock) {
            if (signingIn) return
            signingIn = true
        }
        auth.signInAnonymously()
            .addOnSuccessListener { sendPending() }
            .addOnFailureListener { Log.w(TAG, "Sin sesión anónima, se reintenta luego: ${it.message}") }
            .addOnCompleteListener { synchronized(lock) { signingIn = false } }
    }

    // ---------------------------------------------------------------- Admisiones

    /** Al abrir la app nadie queda como administrador: siempre se vuelve a pedir la contraseña. */
    fun resetToAspirante() {
        if (!firebaseReady) return
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser?.isAnonymous == false) auth.signOut()
    }

    fun adminSignIn(email: String, password: String, onResult: (Result<Unit>) -> Unit) {
        if (!firebaseReady) {
            onResult(Result.failure(IllegalStateException("Firebase no está configurado")))
            return
        }
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email.trim(), password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid
                if (uid == null) {
                    onResult(Result.failure(NotAdminException()))
                    return@addOnSuccessListener
                }
                FirebaseFirestore.getInstance().collection("admins").document(uid).get()
                    .addOnSuccessListener { doc ->
                        if (doc.exists()) {
                            onResult(Result.success(Unit))
                        } else {
                            auth.signOut()
                            onResult(Result.failure(NotAdminException()))
                        }
                    }
                    .addOnFailureListener { e ->
                        auth.signOut()
                        onResult(Result.failure(e))
                    }
            }
            .addOnFailureListener { onResult(Result.failure(it)) }
    }

    fun adminSignOut() {
        if (!firebaseReady) return
        FirebaseAuth.getInstance().signOut()
        flush()
    }

    /** Escucha las tres colecciones en tiempo real. Devuelve la función para detener la escucha. */
    fun listenAdmin(
        onLeads: (List<Lead>) -> Unit,
        onVisits: (List<CampusVisit>) -> Unit,
        onRegistrations: (List<EventRegistration>) -> Unit
    ): () -> Unit {
        if (!firebaseReady) return {}
        val db = FirebaseFirestore.getInstance()
        val registrations = listOf(
            db.collection("leads").addSnapshotListener { snap, error ->
                if (error != null) Log.w(TAG, "leads: ${error.message}")
                else if (snap != null) onLeads(snap.documents.map(::toLead))
            },
            db.collection("visits").addSnapshotListener { snap, error ->
                if (error != null) Log.w(TAG, "visits: ${error.message}")
                else if (snap != null) onVisits(snap.documents.map(::toVisit))
            },
            db.collection("event_registrations").addSnapshotListener { snap, error ->
                if (error != null) Log.w(TAG, "event_registrations: ${error.message}")
                else if (snap != null) onRegistrations(snap.documents.map(::toRegistration))
            }
        )
        return { registrations.forEach { it.remove() } }
    }

    /** Escritura de personal: cambia campos de un documento existente. */
    fun updateRemote(collection: String, remoteId: String?, fields: Map<String, Any>) {
        if (remoteId == null || !firebaseReady) return
        FirebaseFirestore.getInstance().collection(collection).document(remoteId).update(fields)
            .addOnFailureListener { Log.w(TAG, "No se pudo actualizar $collection/$remoteId: ${it.message}") }
    }

    fun deleteRemote(collection: String, remoteId: String?) {
        if (remoteId == null || !firebaseReady) return
        FirebaseFirestore.getInstance().collection(collection).document(remoteId).delete()
            .addOnFailureListener { Log.w(TAG, "No se pudo borrar $collection/$remoteId: ${it.message}") }
    }

    // ---------------------------------------------------------------- Mapeo

    private fun toLead(d: DocumentSnapshot) = Lead(
        fullName = d.getString("fullName").orEmpty(),
        phone = d.getString("phone").orEmpty(),
        email = d.getString("email").orEmpty(),
        cityOrSchool = d.getString("cityOrSchool").orEmpty(),
        programInterest = d.getString("programInterest").orEmpty(),
        degreeLevel = d.getString("degreeLevel") ?: "Pregrado",
        status = LeadStatus.entries.firstOrNull { it.name == d.getString("status") } ?: LeadStatus.NUEVO,
        notes = d.getString("notes").orEmpty(),
        createdAt = d.getLong("createdAt") ?: 0L,
        remoteId = d.id
    )

    private fun toVisit(d: DocumentSnapshot) = CampusVisit(
        studentName = d.getString("studentName").orEmpty(),
        phone = d.getString("phone").orEmpty(),
        email = d.getString("email").orEmpty(),
        campus = d.getString("campus").orEmpty(),
        dateText = d.getString("dateText").orEmpty(),
        timeSlot = d.getString("timeSlot").orEmpty(),
        programInterest = d.getString("programInterest").orEmpty(),
        visitorCount = d.getLong("visitorCount")?.toInt() ?: 1,
        school = d.getString("school").orEmpty(),
        status = VisitStatus.entries.firstOrNull { it.name == d.getString("status") } ?: VisitStatus.AGENDADA,
        createdAt = d.getLong("createdAt") ?: 0L,
        remoteId = d.id
    )

    private fun toRegistration(d: DocumentSnapshot) = EventRegistration(
        eventId = d.getLong("eventId") ?: 0L,
        eventTitle = d.getString("eventTitle").orEmpty(),
        fullName = d.getString("fullName").orEmpty(),
        phone = d.getString("phone").orEmpty(),
        email = d.getString("email").orEmpty(),
        schoolOrOrigin = d.getString("schoolOrOrigin").orEmpty(),
        attended = d.getBoolean("attended") ?: false,
        registeredAt = d.getLong("createdAt") ?: 0L,
        ticketCode = d.getString("ticketCode").orEmpty(),
        remoteId = d.id
    )

    // ---------------------------------------------------------------- Bandeja de salida

    private fun enqueue(collection: String, id: String, fields: Map<String, Any>) {
        synchronized(lock) {
            val outbox = readOutbox()
            outbox.put(
                JSONObject()
                    .put("id", id)
                    .put("collection", collection)
                    .put("fields", JSONObject(fields))
            )
            writeOutbox(outbox)
        }
        flush()
    }

    private fun sendPending() {
        val items = synchronized(lock) {
            val outbox = readOutbox()
            (0 until outbox.length()).map { outbox.getJSONObject(it) }
                .filter { inFlight.add(it.getString("id")) }
        }
        val db = FirebaseFirestore.getInstance()
        for (item in items) {
            val id = item.getString("id")
            val collection = item.getString("collection")
            val fields = item.getJSONObject("fields").let { json ->
                json.keys().asSequence().associateWith { json.get(it) }
            }
            db.collection(collection).document(id).set(fields)
                .addOnSuccessListener { remove(id) }
                .addOnFailureListener { e ->
                    // Las reglas rechazaron el dato o ya existía: reintentar no sirve.
                    val code = (e as? FirebaseFirestoreException)?.code
                    if (code == FirebaseFirestoreException.Code.PERMISSION_DENIED) remove(id)
                    Log.w(TAG, "No se pudo enviar $collection/$id: ${e.message}")
                }
                .addOnCompleteListener { synchronized(lock) { inFlight.remove(id) } }
        }
    }

    private fun remove(id: String) = synchronized(lock) {
        val outbox = readOutbox()
        val kept = JSONArray()
        for (i in 0 until outbox.length()) {
            if (outbox.getJSONObject(i).getString("id") != id) kept.put(outbox.get(i))
        }
        writeOutbox(kept)
    }

    private fun readOutbox(): JSONArray =
        runCatching { JSONArray(prefs.getString(KEY, "[]")) }.getOrDefault(JSONArray())

    private fun writeOutbox(outbox: JSONArray) {
        prefs.edit().putString(KEY, outbox.toString()).apply()
    }

    private companion object {
        const val TAG = "AdmissionsBackend"
        const val KEY = "pending"
    }
}
