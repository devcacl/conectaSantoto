package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.CampusEvent
import com.example.data.model.CampusNews
import com.example.data.model.CampusVisit
import com.example.data.model.EventRegistration
import com.example.data.model.Lead
import com.example.data.model.LeadStatus
import com.example.data.model.RagDocument
import com.example.data.model.VisitStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface SantotoDao {

    // --- LEADS ---
    @Query("SELECT * FROM leads ORDER BY createdAt DESC")
    fun getAllLeads(): Flow<List<Lead>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLead(lead: Lead): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeads(leads: List<Lead>)

    @Update
    suspend fun updateLead(lead: Lead)

    @Query("UPDATE leads SET status = :status WHERE id = :leadId")
    suspend fun updateLeadStatus(leadId: Long, status: LeadStatus)

    @Delete
    suspend fun deleteLead(lead: Lead)

    @Query("SELECT COUNT(*) FROM leads")
    fun getLeadCount(): Flow<Int>

    // --- CAMPUS VISITS ---
    @Query("SELECT * FROM campus_visits ORDER BY dateText ASC, timeSlot ASC")
    fun getAllVisits(): Flow<List<CampusVisit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisit(visit: CampusVisit): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisits(visits: List<CampusVisit>)

    @Update
    suspend fun updateVisit(visit: CampusVisit)

    @Query("UPDATE campus_visits SET status = :status WHERE id = :visitId")
    suspend fun updateVisitStatus(visitId: Long, status: VisitStatus)

    @Delete
    suspend fun deleteVisit(visit: CampusVisit)

    @Query("SELECT COUNT(*) FROM campus_visits")
    fun getVisitCount(): Flow<Int>

    // --- CAMPUS EVENTS ---
    @Query("SELECT * FROM events ORDER BY dateText ASC")
    fun getAllEvents(): Flow<List<CampusEvent>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: CampusEvent): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<CampusEvent>)

    @Update
    suspend fun updateEvent(event: CampusEvent)

    @Delete
    suspend fun deleteEvent(event: CampusEvent)

    @Query("SELECT COUNT(*) FROM events")
    fun getEventCount(): Flow<Int>

    // --- RAG DOCUMENTS (.md Knowledge Base) ---
    @Query("SELECT * FROM rag_documents ORDER BY id ASC")
    fun getAllRagDocuments(): Flow<List<RagDocument>>

    @Query("SELECT * FROM rag_documents ORDER BY id ASC")
    suspend fun getAllRagDocumentsList(): List<RagDocument>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRagDocument(doc: RagDocument): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRagDocuments(docs: List<RagDocument>)

    @Update
    suspend fun updateRagDocument(doc: RagDocument)

    @Delete
    suspend fun deleteRagDocument(doc: RagDocument)

    @Query("DELETE FROM rag_documents")
    suspend fun deleteAllRagDocuments()

    @Query("SELECT COUNT(*) FROM rag_documents")
    fun getRagDocumentCount(): Flow<Int>

    // --- CAMPUS NEWS (NOTICIAS) ---
    @Query("SELECT * FROM news ORDER BY createdAt DESC")
    fun getAllNews(): Flow<List<CampusNews>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: CampusNews): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewsList(newsList: List<CampusNews>)

    @Update
    suspend fun updateNews(news: CampusNews)

    @Delete
    suspend fun deleteNews(news: CampusNews)

    @Query("SELECT COUNT(*) FROM news")
    fun getNewsCount(): Flow<Int>

    // --- EVENT REGISTRATIONS (INSCRIPCIONES Y ASISTENCIA) ---
    @Query("SELECT * FROM event_registrations ORDER BY registeredAt DESC")
    fun getAllRegistrations(): Flow<List<EventRegistration>>

    @Query("SELECT * FROM event_registrations WHERE eventId = :eventId ORDER BY registeredAt DESC")
    fun getRegistrationsForEvent(eventId: Long): Flow<List<EventRegistration>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegistration(registration: EventRegistration): Long

    @Update
    suspend fun updateRegistration(registration: EventRegistration)

    @Delete
    suspend fun deleteRegistration(registration: EventRegistration)

    @Query("UPDATE event_registrations SET attended = :attended WHERE id = :registrationId")
    suspend fun updateAttendance(registrationId: Long, attended: Boolean)

    @Query("SELECT COUNT(*) FROM event_registrations WHERE eventId = :eventId")
    fun getRegistrationCountForEvent(eventId: Long): Flow<Int>
}
