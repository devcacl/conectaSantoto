package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.SantotoDatabase
import com.example.data.model.AcademicProgram
import com.example.data.model.CampusEvent
import com.example.data.model.CampusTourLocation
import com.example.data.model.CampusVisit
import com.example.data.model.ChatMessage
import com.example.data.model.CampusNews
import com.example.data.model.EventRegistration
import com.example.data.model.FacultyCategory
import com.example.data.model.Lead
import com.example.data.model.LeadStatus
import com.example.data.model.ProgramLevel
import com.example.data.model.RagDocument
import com.example.data.model.VisitStatus
import com.example.data.model.VocationalResult
import com.example.data.rag.CampusTourData
import com.example.data.rag.SantotoKnowledgeBase
import com.example.data.rag.SantotoRagEngine
import com.example.data.rag.VocationalTestData
import com.example.data.remote.AdmissionsBackend
import com.example.data.repository.SantotoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class UserRole {
    ASPIRANTE,
    PROFESIONAL_ADMISIONES
}

sealed class Screen {
    object Home : Screen()
    object Chatbot : Screen()
    object Explora360 : Screen()
    object VocationalTest : Screen()
    object ScheduleVisit : Screen()
    object ProgramsCatalog : Screen()
    object Events : Screen()
    object News : Screen()
    data class ProgramDetail(val program: AcademicProgram) : Screen()
    data class EventConfirmRegistration(val event: CampusEvent) : Screen()
    data class EventTicket(val registration: EventRegistration, val event: CampusEvent) : Screen()
    data class NewsDetail(val news: CampusNews) : Screen()
    object AdmissionsPanel : Screen()
}

class SantotoViewModel(application: Application) : AndroidViewModel(application) {

    private val backend = AdmissionsBackend(application)
    private val repository: SantotoRepository
    private var stopAdminSync: (() -> Unit)? = null

    init {
        val database = SantotoDatabase.getDatabase(application)
        repository = SantotoRepository(database.santotoDao(), backend)
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
        backend.resetToAspirante()
        repository.syncPending()
    }

    override fun onCleared() {
        stopAdminSync?.invoke()
        super.onCleared()
    }

    /** Acceso de Admisiones con Firebase Auth; solo autoriza si existe admins/{uid}. */
    fun adminSignIn(email: String, password: String, onResult: (Result<Unit>) -> Unit) =
        backend.adminSignIn(email, password, onResult)

    // Role and Navigation
    private val _currentRole = MutableStateFlow(UserRole.ASPIRANTE)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val screenBackStack = mutableListOf<Screen>()

    fun setRole(role: UserRole) {
        _currentRole.value = role
        stopAdminSync?.invoke()
        stopAdminSync = null
        if (role == UserRole.PROFESIONAL_ADMISIONES) {
            stopAdminSync = repository.startAdminSync()
            navigateTo(Screen.AdmissionsPanel)
        } else {
            backend.adminSignOut()
            navigateTo(Screen.Home)
        }
    }

    fun navigateTo(screen: Screen) {
        if (_currentScreen.value != screen) {
            screenBackStack.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        if (screenBackStack.isNotEmpty()) {
            _currentScreen.value = screenBackStack.removeAt(screenBackStack.lastIndex)
            return true
        } else if (_currentScreen.value !is Screen.Home) {
            _currentScreen.value = if (_currentRole.value == UserRole.PROFESIONAL_ADMISIONES) Screen.AdmissionsPanel else Screen.Home
            return true
        }
        return false
    }

    // Leads & Visits from Repository
    val leads: StateFlow<List<Lead>> = repository.allLeads
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val campusVisits: StateFlow<List<CampusVisit>> = repository.allVisits
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val events: StateFlow<List<CampusEvent>> = repository.allEvents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val news: StateFlow<List<CampusNews>> = repository.allNews
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allRegistrations: StateFlow<List<EventRegistration>> = repository.allRegistrations
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun getRegistrationsForEvent(eventId: Long): Flow<List<EventRegistration>> =
        repository.getRegistrationsForEvent(eventId)

    fun createEvent(
        title: String,
        dateBadge: String,
        dateText: String,
        time: String,
        location: String,
        category: String,
        description: String,
        imageUrl: String = "https://images.unsplash.com/photo-1541339907198-e08756dedf3f?w=800&auto=format&fit=crop&q=80",
        capacity: Int = 60,
        availableSpots: Int = 45,
        isFeatured: Boolean = true
    ) {
        viewModelScope.launch {
            repository.insertEvent(
                CampusEvent(
                    title = title,
                    dateBadge = dateBadge,
                    dateText = dateText,
                    time = time,
                    location = location,
                    category = category,
                    description = description,
                    imageUrl = imageUrl,
                    capacity = capacity,
                    availableSpots = availableSpots,
                    isFeatured = isFeatured
                )
            )
        }
    }

    fun updateEvent(event: CampusEvent) {
        viewModelScope.launch {
            repository.updateEvent(event)
        }
    }

    fun deleteEvent(event: CampusEvent) {
        viewModelScope.launch {
            repository.deleteEvent(event)
        }
    }

    // --- News CRUD ---
    fun createNews(
        title: String,
        summary: String,
        content: String,
        category: String,
        dateText: String,
        imageUrl: String = "https://images.unsplash.com/photo-1523240795612-9a054b0db644?w=800&auto=format&fit=crop&q=80",
        author: String = "Dirección de Comunicaciones USTA Tunja",
        readTimeMinutes: Int = 3,
        isFeatured: Boolean = false
    ) {
        viewModelScope.launch {
            repository.insertNews(
                CampusNews(
                    title = title,
                    summary = summary,
                    content = content,
                    category = category,
                    dateText = dateText,
                    imageUrl = imageUrl,
                    author = author,
                    readTimeMinutes = readTimeMinutes,
                    isFeatured = isFeatured
                )
            )
        }
    }

    fun updateNews(newsItem: CampusNews) {
        viewModelScope.launch {
            repository.updateNews(newsItem)
        }
    }

    fun deleteNews(newsItem: CampusNews) {
        viewModelScope.launch {
            repository.deleteNews(newsItem)
        }
    }

    // --- Event Registration & Attendance ---
    fun updateAttendance(registrationId: Long, attended: Boolean) {
        viewModelScope.launch {
            repository.updateAttendance(registrationId, attended)
        }
    }

    fun registerForEvent(
        event: CampusEvent,
        fullName: String,
        phone: String,
        email: String,
        school: String,
        onComplete: (EventRegistration) -> Unit
    ) {
        viewModelScope.launch {
            val registration = repository.registerForEvent(
                event = event,
                fullName = fullName,
                phone = phone,
                email = email,
                school = school
            )
            onComplete(registration)
        }
    }

    // Dynamic RAG Knowledge Base Documents (.md)
    val ragDocuments: StateFlow<List<RagDocument>> = repository.allRagDocuments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun createRagDocument(title: String, category: String, content: String) {
        viewModelScope.launch {
            repository.insertRagDocument(
                RagDocument(
                    title = title,
                    category = category,
                    content = content,
                    lastUpdated = System.currentTimeMillis()
                )
            )
        }
    }

    fun updateRagDocument(doc: RagDocument) {
        viewModelScope.launch {
            repository.updateRagDocument(doc.copy(lastUpdated = System.currentTimeMillis()))
        }
    }

    fun deleteRagDocument(doc: RagDocument) {
        viewModelScope.launch {
            repository.deleteRagDocument(doc)
        }
    }

    fun resetDefaultRagDocuments() {
        viewModelScope.launch {
            repository.resetDefaultRagDocuments()
        }
    }

    fun updateLeadStatus(leadId: Long, newStatus: LeadStatus) {
        viewModelScope.launch {
            repository.updateLeadStatus(leadId, newStatus)
        }
    }

    fun updateLead(lead: Lead) {
        viewModelScope.launch {
            repository.updateLead(lead)
        }
    }

    fun deleteLead(lead: Lead) {
        viewModelScope.launch {
            repository.deleteLead(lead)
        }
    }

    fun captureLead(
        fullName: String,
        phone: String,
        email: String,
        cityOrSchool: String,
        programInterest: String,
        degreeLevel: String = "Pregrado",
        notes: String = ""
    ) {
        viewModelScope.launch {
            val newLead = Lead(
                fullName = fullName,
                phone = phone,
                email = email,
                cityOrSchool = cityOrSchool,
                programInterest = programInterest,
                degreeLevel = degreeLevel,
                status = LeadStatus.NUEVO,
                notes = notes
            )
            repository.insertLead(newLead)
        }
    }

    fun scheduleCampusVisit(
        studentName: String,
        phone: String,
        email: String,
        campus: String,
        dateText: String,
        timeSlot: String,
        programInterest: String,
        visitorCount: Int,
        school: String = ""
    ) {
        viewModelScope.launch {
            val visit = CampusVisit(
                studentName = studentName,
                phone = phone,
                email = email,
                campus = campus,
                dateText = dateText,
                timeSlot = timeSlot,
                programInterest = programInterest,
                visitorCount = visitorCount,
                school = school,
                status = VisitStatus.AGENDADA
            )
            repository.insertVisit(visit)

            // Also capture or update as Lead in pipeline
            val lead = Lead(
                fullName = studentName,
                phone = phone,
                email = email,
                cityOrSchool = school.ifBlank { "Aspirante Visita" },
                programInterest = programInterest,
                status = LeadStatus.VISITA_AGENDADA,
                notes = "Agendó visita al $campus para el $dateText ($timeSlot) con $visitorCount personas."
            )
            repository.insertLead(lead)
        }
    }

    fun updateVisitStatus(visitId: Long, status: VisitStatus) {
        viewModelScope.launch {
            repository.updateVisitStatus(visitId, status)
        }
    }

    fun updateVisit(visit: CampusVisit) {
        viewModelScope.launch {
            repository.updateVisit(visit)
        }
    }

    fun deleteVisit(visit: CampusVisit) {
        viewModelScope.launch {
            repository.deleteVisit(visit)
        }
    }

    // Chatbot State
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                text = "¡Hola! Soy **Tommy Bot**, tu asesor virtual oficial de Admisiones y Marketing de la Universidad Santo Tomás Seccional Tunja 🏛️.\n\nEstoy aquí para orientarte y acompañarte en tu elección profesional con nuestras carreras de pregrado, posgrados, microcredenciales de grado 11° y convenios de homologación SENA.\n\n¿En qué carrera o proceso de admisión te gustaría que te oriente hoy?",
                isFromUser = false,
                sources = listOf("Universidad Santo Tomás Tunja"),
                suggestedPrompts = listOf(
                    "¿Qué carreras ofrece la Santoto Tunja?",
                    "¿Cuánto cuesta Ingeniería de Sistemas?",
                    "Microcredenciales Grado 11° ($100.000)",
                    "Hablar con un asesor por WhatsApp"
                )
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading: StateFlow<Boolean> = _isChatLoading.asStateFlow()

    // Analytics Tracker for Chatbot
    private val _totalQueriesCount = MutableStateFlow(0)
    val totalQueriesCount: StateFlow<Int> = _totalQueriesCount.asStateFlow()

    private val _queriedProgramsStats = MutableStateFlow<Map<String, Int>>(emptyMap())
    val queriedProgramsStats: StateFlow<Map<String, Int>> = _queriedProgramsStats.asStateFlow()

    fun sendChatMessage(text: String) {
        if (text.isBlank() || _isChatLoading.value) return

        val userMessage = ChatMessage(text = text.trim(), isFromUser = true)
        val currentHistory = _chatMessages.value
        _chatMessages.value = currentHistory + userMessage
        _isChatLoading.value = true
        _totalQueriesCount.value += 1

        viewModelScope.launch {
            try {
                val dynamicDocs = repository.getAllRagDocumentsList()
                val ragResult = SantotoRagEngine.query(text, currentHistory, dynamicDocs)
                val botMessage = ChatMessage(
                    text = ragResult.answer,
                    isFromUser = false,
                    sources = ragResult.sources,
                    suggestedPrompts = ragResult.suggestedActions
                )
                _chatMessages.value = _chatMessages.value + botMessage

                // Track queried program
                SantotoKnowledgeBase.PREGRADOS.firstOrNull {
                    text.contains(it.name, ignoreCase = true)
                }?.let { prog ->
                    val currentStats = _queriedProgramsStats.value.toMutableMap()
                    currentStats[prog.name] = (currentStats[prog.name] ?: 0) + 1
                    _queriedProgramsStats.value = currentStats
                }
            } catch (e: Exception) {
                _chatMessages.value = _chatMessages.value + ChatMessage(
                    text = "Estimado aspirante, en este momento puedes contactar directamente a nuestros asesores en Tunja al WhatsApp 317 510 5383 o al correo admisiones@ustatunja.edu.co para brindarte atención inmediata.",
                    isFromUser = false,
                    sources = listOf("Directorio Admisiones Tunja")
                )
            } finally {
                _isChatLoading.value = false
            }
        }
    }

    // Vocational Test
    private val _testCurrentQuestion = MutableStateFlow(0)
    val testCurrentQuestion: StateFlow<Int> = _testCurrentQuestion.asStateFlow()

    private val _testAnswers = MutableStateFlow<List<FacultyCategory>>(emptyList())
    val testAnswers: StateFlow<List<FacultyCategory>> = _testAnswers.asStateFlow()

    private val _testResult = MutableStateFlow<VocationalResult?>(null)
    val testResult: StateFlow<VocationalResult?> = _testResult.asStateFlow()

    fun answerVocationalQuestion(faculty: FacultyCategory) {
        val updatedAnswers = _testAnswers.value + faculty
        _testAnswers.value = updatedAnswers
        if (updatedAnswers.size < VocationalTestData.QUESTIONS.size) {
            _testCurrentQuestion.value += 1
        } else {
            val result = VocationalTestData.calculateResult(updatedAnswers)
            _testResult.value = result
        }
    }

    fun resetVocationalTest() {
        _testCurrentQuestion.value = 0
        _testAnswers.value = emptyList()
        _testResult.value = null
    }

    // Explora 360 Tour State
    private val _selectedLocationIndex = MutableStateFlow(0)
    val selectedLocationIndex: StateFlow<Int> = _selectedLocationIndex.asStateFlow()

    private val _isVrModeEnabled = MutableStateFlow(false)
    val isVrModeEnabled: StateFlow<Boolean> = _isVrModeEnabled.asStateFlow()

    private val _currentYawAngle = MutableStateFlow(0f)
    val currentYawAngle: StateFlow<Float> = _currentYawAngle.asStateFlow()

    fun selectTourLocation(index: Int) {
        if (index in CampusTourData.LOCATIONS.indices) {
            _selectedLocationIndex.value = index
            _currentYawAngle.value = 0f
        }
    }

    fun toggleVrMode() {
        _isVrModeEnabled.value = !_isVrModeEnabled.value
    }

    fun rotateYaw(delta: Float) {
        var newAngle = (_currentYawAngle.value + delta) % 360f
        if (newAngle < 0) newAngle += 360f
        _currentYawAngle.value = newAngle
    }

    // Catalog Filters
    private val _selectedLevelFilter = MutableStateFlow<ProgramLevel?>(null)
    val selectedLevelFilter: StateFlow<ProgramLevel?> = _selectedLevelFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun setLevelFilter(level: ProgramLevel?) {
        _selectedLevelFilter.value = level
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
}
