package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event_registrations")
data class EventRegistration(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val eventId: Long,
    val eventTitle: String,
    val fullName: String,
    val phone: String,
    val email: String,
    val schoolOrOrigin: String = "",
    val attended: Boolean = false, // Registrar si asistió o no (check-in)
    val registeredAt: Long = System.currentTimeMillis(),
    val ticketCode: String = "" // e.g. "USTA-OPENDAY-7341"
)
