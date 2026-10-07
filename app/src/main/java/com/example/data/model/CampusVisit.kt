package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class VisitStatus(val label: String) {
    AGENDADA("Agendada"),
    CONFIRMADA("Confirmada"),
    REALIZADA("Realizada"),
    CANCELADA("Cancelada")
}

@Entity(tableName = "campus_visits")
data class CampusVisit(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val studentName: String,
    val phone: String,
    val email: String,
    val campus: String, // "Campus Av. Universitaria" o "Campus Centro Histórico"
    val dateText: String, // ej. "2026-10-15"
    val timeSlot: String, // ej. "09:00 AM - 11:00 AM"
    val programInterest: String,
    val visitorCount: Int = 1,
    val school: String = "",
    val status: VisitStatus = VisitStatus.AGENDADA,
    val confirmedByStaff: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
