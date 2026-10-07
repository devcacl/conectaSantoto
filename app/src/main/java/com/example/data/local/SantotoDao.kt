package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.CampusEvent
import com.example.data.model.CampusVisit
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
}
