package com.example.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

enum class LeadStatus(val label: String, val colorHex: Long) {
    NUEVO("Nuevo Lead", 0xFF0072CE),
    CONTACTADO("Contactado", 0xFF6200EE),
    VISITA_AGENDADA("Visita Agendada", 0xFFF9AB00),
    DOCUMENTOS_PRESENTADOS("Documentos Presentados", 0xFFE65100),
    ADMITIDO("Admitido", 0xFF00897B),
    MATRICULADO("Matriculado", 0xFF1E8E3E)
}

@Entity(tableName = "leads", indices = [Index(value = ["remoteId"], unique = true)])
data class Lead(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fullName: String,
    val phone: String,
    val email: String,
    val cityOrSchool: String,
    val programInterest: String,
    val degreeLevel: String = "Pregrado",
    val status: LeadStatus = LeadStatus.NUEVO,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    /** ID del documento en Firestore; null si el lead es solo local (datos de ejemplo). */
    val remoteId: String? = null
)
