package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.example.data.model.CampusNews
import com.example.data.model.EventRegistration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.CampusEvent
import com.example.data.model.CampusVisit
import com.example.data.model.Lead
import com.example.data.model.LeadStatus
import com.example.data.model.RagDocument
import com.example.data.model.VisitStatus
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.ui.components.InteractiveCalendarView
import com.example.ui.theme.SantotoBg
import com.example.ui.theme.SantotoCyan
import com.example.ui.theme.SantotoGold
import com.example.ui.theme.SantotoNavy
import com.example.ui.theme.SantotoNavyDark
import com.example.ui.theme.SantotoRoyal
import com.example.ui.theme.SantotoSuccess
import com.example.ui.theme.SantotoWarning
import com.example.ui.theme.SantotoYellow
import com.example.viewmodel.SantotoViewModel
import com.example.viewmodel.UserRole

@Composable
fun AdmissionsDashboardScreen(
    viewModel: SantotoViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.setRole(UserRole.ASPIRANTE)
    }

    val context = LocalContext.current
    val leads by viewModel.leads.collectAsStateWithLifecycle()
    val visits by viewModel.campusVisits.collectAsStateWithLifecycle()
    val events by viewModel.events.collectAsStateWithLifecycle()
    val news by viewModel.news.collectAsStateWithLifecycle()
    val allRegistrations by viewModel.allRegistrations.collectAsStateWithLifecycle()
    val ragDocuments by viewModel.ragDocuments.collectAsStateWithLifecycle()
    val totalQueries by viewModel.totalQueriesCount.collectAsStateWithLifecycle()
    val programStats by viewModel.queriedProgramsStats.collectAsStateWithLifecycle()

    // 0: Estadísticas / Dashboard first as requested!
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Métricas & Dashboard", "Pipeline Leads", "Calendario Visitas", "Gestión Eventos", "Noticias USTA", "Conocimiento Tommy")

    var selectedStatusFilter by remember { mutableStateOf<LeadStatus?>(null) }
    var showCreateLeadDialog by remember { mutableStateOf(false) }

    // Dialogs state for Visits (Create, Edit, Delete)
    var showCreateVisitDialog by remember { mutableStateOf(false) }
    var prefillVisitName by remember { mutableStateOf("") }
    var prefillVisitPhone by remember { mutableStateOf("") }
    var prefillVisitEmail by remember { mutableStateOf("") }
    var prefillVisitProgram by remember { mutableStateOf("") }
    var prefillVisitSchool by remember { mutableStateOf("") }
    var visitToEdit by remember { mutableStateOf<CampusVisit?>(null) }
    var visitToDelete by remember { mutableStateOf<CampusVisit?>(null) }

    // Dialogs state for Events (Create, Edit, Delete, Attendance)
    var showCreateEventDialog by remember { mutableStateOf(false) }
    var eventToEdit by remember { mutableStateOf<CampusEvent?>(null) }
    var eventToDelete by remember { mutableStateOf<CampusEvent?>(null) }
    var eventForAttendance by remember { mutableStateOf<CampusEvent?>(null) }

    // Dialogs state for News CRUD
    var showCreateNewsDialog by remember { mutableStateOf(false) }
    var newsToEdit by remember { mutableStateOf<CampusNews?>(null) }
    var newsToDelete by remember { mutableStateOf<CampusNews?>(null) }

    // Dialog state for Tommy Knowledge RAG Document creation
    var showCreateRagDocDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SantotoBg)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Scrollable Navbar: text continues on one line without breaking into two rows
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                edgePadding = 14.dp,
                containerColor = SantotoNavy,
                contentColor = Color.White,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = SantotoGold,
                        height = 3.dp
                    )
                }
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                maxLines = 1,
                                softWrap = false,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium,
                                color = if (selectedTabIndex == index) SantotoGold else Color(0xFFCBD5E1)
                            )
                        }
                    )
                }
            }

            // Tab Content with Dashboard as primary tab
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                when (selectedTabIndex) {
                    0 -> ChatbotAnalyticsTab(
                        totalQueries = totalQueries,
                        programStats = programStats,
                        leads = leads,
                        visits = visits,
                        onNavigateToPipeline = { selectedTabIndex = 1 },
                        onNavigateToCalendar = { selectedTabIndex = 2 }
                    )
                    1 -> LeadsPipelineTab(
                        leads = leads,
                        selectedStatusFilter = selectedStatusFilter,
                        onFilterSelect = { selectedStatusFilter = it },
                        onAdvanceStatus = { lead ->
                            val nextStatus = when (lead.status) {
                                LeadStatus.NUEVO -> LeadStatus.CONTACTADO
                                LeadStatus.CONTACTADO -> LeadStatus.VISITA_AGENDADA
                                LeadStatus.VISITA_AGENDADA -> LeadStatus.DOCUMENTOS_PRESENTADOS
                                LeadStatus.DOCUMENTOS_PRESENTADOS -> LeadStatus.ADMITIDO
                                LeadStatus.ADMITIDO -> LeadStatus.MATRICULADO
                                LeadStatus.MATRICULADO -> LeadStatus.MATRICULADO
                            }
                            viewModel.updateLeadStatus(lead.id, nextStatus)
                        },
                        onSetStatus = { leadId, newStatus ->
                            viewModel.updateLeadStatus(leadId, newStatus)
                        },
                        onContactLead = { phone ->
                            val cleanPhone = phone.replace(Regex("[^0-9]"), "")
                            val url = "https://api.whatsapp.com/send?phone=57$cleanPhone&text=Hola! Te contactamos de la Dirección de Admisiones de la Santoto Tunja."
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            try {
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$cleanPhone"))
                                context.startActivity(dialIntent)
                            }
                        }
                    )
                    2 -> AdminVisitsCalendarTab(
                        visits = visits,
                        leads = leads,
                        onUpdateStatus = { visitId, status -> viewModel.updateVisitStatus(visitId, status) },
                        onContactStudent = { phone ->
                            val cleanPhone = phone.replace(Regex("[^0-9]"), "")
                            val url = "https://api.whatsapp.com/send?phone=57$cleanPhone&text=Hola! Te contactamos para confirmar tu visita al campus de la Santoto Tunja."
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            try { context.startActivity(intent) } catch (e: Exception) { /* ignore */ }
                        },
                        onCreateVisitClick = {
                            prefillVisitName = ""
                            prefillVisitPhone = ""
                            prefillVisitEmail = ""
                            prefillVisitProgram = ""
                            prefillVisitSchool = ""
                            showCreateVisitDialog = true
                        },
                        onEditVisitClick = { visit ->
                            visitToEdit = visit
                        },
                        onDeleteVisitClick = { visit ->
                            visitToDelete = visit
                        },
                        onScheduleLeadVisit = { lead ->
                            prefillVisitName = lead.fullName
                            prefillVisitPhone = lead.phone
                            prefillVisitEmail = lead.email
                            prefillVisitProgram = lead.programInterest
                            prefillVisitSchool = lead.cityOrSchool
                            showCreateVisitDialog = true
                        }
                    )
                    3 -> AdminEventsManagementTab(
                        events = events,
                        registrations = allRegistrations,
                        onCreateEventClick = { showCreateEventDialog = true },
                        onEditEventClick = { event -> eventToEdit = event },
                        onDeleteEventClick = { event -> eventToDelete = event },
                        onViewAttendanceClick = { event -> eventForAttendance = event }
                    )
                    4 -> AdminNewsManagementTab(
                        newsList = news,
                        onCreateNewsClick = { showCreateNewsDialog = true },
                        onEditNewsClick = { item -> newsToEdit = item },
                        onDeleteNewsClick = { item -> newsToDelete = item }
                    )
                    5 -> AdminRagDocumentsTab(
                        ragDocuments = ragDocuments,
                        externalShowCreateDialog = showCreateRagDocDialog,
                        onOpenCreateDialog = { showCreateRagDocDialog = true },
                        onDismissCreateDialog = { showCreateRagDocDialog = false },
                        onCreateDocument = { title, category, content ->
                            viewModel.createRagDocument(title, category, content)
                        },
                        onUpdateDocument = { doc ->
                            viewModel.updateRagDocument(doc)
                        },
                        onDeleteDocument = { doc ->
                            viewModel.deleteRagDocument(doc)
                        },
                        onResetDefaults = {
                            viewModel.resetDefaultRagDocuments()
                        }
                    )
                }
            }
        }

        // Floating Action Button with only '+' icon, changing dynamically by active screen tab
        FloatingActionButton(
            onClick = {
                when (selectedTabIndex) {
                    0, 1 -> showCreateLeadDialog = true
                    2 -> {
                        prefillVisitName = ""
                        prefillVisitPhone = ""
                        prefillVisitEmail = ""
                        prefillVisitProgram = ""
                        prefillVisitSchool = ""
                        showCreateVisitDialog = true
                    }
                    3 -> showCreateEventDialog = true
                    4 -> showCreateNewsDialog = true
                    5 -> showCreateRagDocDialog = true
                }
            },
            containerColor = SantotoNavy,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(56.dp)
                .testTag("fab_context_add")
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = when (selectedTabIndex) {
                    0, 1 -> "Agregar Lead"
                    2 -> "Agregar Visita"
                    3 -> "Crear Evento"
                    4 -> "Crear Noticia"
                    5 -> "Agregar Documento Tommy .md"
                    else -> "Agregar"
                },
                tint = SantotoGold,
                modifier = Modifier.size(26.dp)
            )
        }
    }

    // High contrast dark text field colors for all dialogs (never white text on white bg)
    val adminFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color(0xFF0F172A),
        unfocusedTextColor = Color(0xFF0F172A),
        focusedLabelColor = SantotoNavy,
        unfocusedLabelColor = Color(0xFF475569),
        focusedBorderColor = SantotoNavy,
        unfocusedBorderColor = Color(0xFFCBD5E1),
        cursorColor = SantotoNavy,
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White
    )

    // Modal Create Lead Dialog
    if (showCreateLeadDialog) {
        var newName by remember { mutableStateOf("") }
        var newPhone by remember { mutableStateOf("") }
        var newEmail by remember { mutableStateOf("") }
        var newSchool by remember { mutableStateOf("") }
        var newProgram by remember { mutableStateOf("Ingeniería de Sistemas") }
        var newNotes by remember { mutableStateOf("") }

        androidx.compose.ui.window.Dialog(onDismissRequest = { showCreateLeadDialog = false }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Registrar Nuevo Aspirante (Lead)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SantotoNavy
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text("Nombre completo *") },
                        singleLine = true,
                        colors = adminFieldColors,
                        modifier = Modifier.fillMaxWidth().testTag("modal_lead_name")
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newPhone,
                        onValueChange = { newPhone = it },
                        label = { Text("Teléfono / WhatsApp *") },
                        singleLine = true,
                        colors = adminFieldColors,
                        modifier = Modifier.fillMaxWidth().testTag("modal_lead_phone")
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newEmail,
                        onValueChange = { newEmail = it },
                        label = { Text("Correo electrónico") },
                        singleLine = true,
                        colors = adminFieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newSchool,
                        onValueChange = { newSchool = it },
                        label = { Text("Colegio / Municipio") },
                        singleLine = true,
                        colors = adminFieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newProgram,
                        onValueChange = { newProgram = it },
                        label = { Text("Programa de interés *") },
                        singleLine = true,
                        colors = adminFieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newNotes,
                        onValueChange = { newNotes = it },
                        label = { Text("Observaciones / Estado inicial") },
                        colors = adminFieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { showCreateLeadDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancelar", color = Color.DarkGray)
                        }

                        Button(
                            onClick = {
                                if (newName.isNotBlank() && newPhone.isNotBlank()) {
                                    viewModel.captureLead(
                                        fullName = newName,
                                        phone = newPhone,
                                        email = newEmail,
                                        cityOrSchool = newSchool,
                                        programInterest = newProgram,
                                        notes = newNotes
                                    )
                                    showCreateLeadDialog = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SantotoNavy, contentColor = Color.White),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f).testTag("modal_lead_save")
                        ) {
                            Text("Guardar Lead", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }

    // Modal Create Visit Dialog (Allows admin to schedule visits, including for pending leads)
    if (showCreateVisitDialog) {
        var vName by remember { mutableStateOf(prefillVisitName) }
        var vPhone by remember { mutableStateOf(prefillVisitPhone) }
        var vEmail by remember { mutableStateOf(prefillVisitEmail) }
        var vProgram by remember { mutableStateOf(if (prefillVisitProgram.isNotBlank()) prefillVisitProgram else "Ingeniería de Sistemas") }
        var vSchool by remember { mutableStateOf(prefillVisitSchool) }
        var vCampus by remember { mutableStateOf("Campus Av. Universitaria") }
        var vDateText by remember { mutableStateOf("2026-10-22") }
        var vTimeSlot by remember { mutableStateOf("09:00 AM - 11:00 AM") }
        var vVisitors by remember { mutableIntStateOf(1) }

        androidx.compose.ui.window.Dialog(onDismissRequest = { showCreateVisitDialog = false }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth().padding(10.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Agendar Visita al Campus",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SantotoNavy
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = vName,
                        onValueChange = { vName = it },
                        label = { Text("Nombre del Aspirante *") },
                        singleLine = true,
                        colors = adminFieldColors,
                        modifier = Modifier.fillMaxWidth().testTag("modal_visit_name")
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = vPhone,
                            onValueChange = { vPhone = it },
                            label = { Text("Teléfono *") },
                            singleLine = true,
                            colors = adminFieldColors,
                            modifier = Modifier.weight(1f).testTag("modal_visit_phone")
                        )
                        OutlinedTextField(
                            value = vDateText,
                            onValueChange = { vDateText = it },
                            label = { Text("Fecha YYYY-MM-DD") },
                            singleLine = true,
                            colors = adminFieldColors,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = vCampus,
                            onValueChange = { vCampus = it },
                            label = { Text("Campus") },
                            singleLine = true,
                            colors = adminFieldColors,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = vTimeSlot,
                            onValueChange = { vTimeSlot = it },
                            label = { Text("Horario") },
                            singleLine = true,
                            colors = adminFieldColors,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = vProgram,
                        onValueChange = { vProgram = it },
                        label = { Text("Programa de interés *") },
                        singleLine = true,
                        colors = adminFieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { showCreateVisitDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancelar", color = Color.DarkGray)
                        }

                        Button(
                            onClick = {
                                if (vName.isNotBlank() && vPhone.isNotBlank()) {
                                    viewModel.scheduleCampusVisit(
                                        studentName = vName.trim(),
                                        phone = vPhone.trim(),
                                        email = vEmail.trim(),
                                        campus = vCampus.trim(),
                                        dateText = vDateText.trim(),
                                        timeSlot = vTimeSlot.trim(),
                                        programInterest = vProgram.trim(),
                                        visitorCount = vVisitors,
                                        school = vSchool.trim()
                                    )
                                    showCreateVisitDialog = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SantotoNavy, contentColor = Color.White),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f).testTag("modal_visit_save")
                        ) {
                            Text("Agendar Visita", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }

    // Modal Edit Visit Dialog (allows updating visit details)
    visitToEdit?.let { visit ->
        var editName by remember(visit) { mutableStateOf(visit.studentName) }
        var editPhone by remember(visit) { mutableStateOf(visit.phone) }
        var editCampus by remember(visit) { mutableStateOf(visit.campus) }
        var editDate by remember(visit) { mutableStateOf(visit.dateText) }
        var editTime by remember(visit) { mutableStateOf(visit.timeSlot) }
        var editProgram by remember(visit) { mutableStateOf(visit.programInterest) }
        var editStatus by remember(visit) { mutableStateOf(visit.status) }

        androidx.compose.ui.window.Dialog(onDismissRequest = { visitToEdit = null }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth().padding(10.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Actualizar Visita #${visit.id}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SantotoNavy
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Nombre del Aspirante") },
                        singleLine = true,
                        colors = adminFieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = editPhone,
                            onValueChange = { editPhone = it },
                            label = { Text("Teléfono") },
                            singleLine = true,
                            colors = adminFieldColors,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = editDate,
                            onValueChange = { editDate = it },
                            label = { Text("Fecha") },
                            singleLine = true,
                            colors = adminFieldColors,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = editCampus,
                            onValueChange = { editCampus = it },
                            label = { Text("Campus") },
                            singleLine = true,
                            colors = adminFieldColors,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = editTime,
                            onValueChange = { editTime = it },
                            label = { Text("Horario") },
                            singleLine = true,
                            colors = adminFieldColors,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = editProgram,
                        onValueChange = { editProgram = it },
                        label = { Text("Programa") },
                        singleLine = true,
                        colors = adminFieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Estado de la Visita:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SantotoNavy)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        VisitStatus.values().forEach { st ->
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (editStatus == st) SantotoNavy else Color(0xFFF1F5F9),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { editStatus = st }
                            ) {
                                Text(
                                    text = st.label,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (editStatus == st) SantotoGold else Color(0xFF475569),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { visitToEdit = null },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancelar", color = Color.DarkGray)
                        }

                        Button(
                            onClick = {
                                viewModel.updateVisit(
                                    visit.copy(
                                        studentName = editName.trim(),
                                        phone = editPhone.trim(),
                                        campus = editCampus.trim(),
                                        dateText = editDate.trim(),
                                        timeSlot = editTime.trim(),
                                        programInterest = editProgram.trim(),
                                        status = editStatus
                                    )
                                )
                                visitToEdit = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SantotoNavy, contentColor = Color.White),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Guardar", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }

    // Modal Delete Visit Confirmation
    visitToDelete?.let { visit ->
        AlertDialog(
            onDismissRequest = { visitToDelete = null },
            title = { Text("¿Eliminar visita?", fontWeight = FontWeight.Bold, color = SantotoNavy) },
            text = { Text("Se cancelará la visita de ${visit.studentName} fijada para el ${visit.dateText}.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteVisit(visit)
                        visitToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626), contentColor = Color.White)
                ) {
                    Text("Eliminar", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                Button(
                    onClick = { visitToDelete = null },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9))
                ) {
                    Text("Cancelar", color = Color(0xFF475569))
                }
            }
        )
    }

    // Modal Create Event Dialog (requested by user)
    if (showCreateEventDialog) {
        var evtTitle by remember { mutableStateOf("") }
        var evtBadge by remember { mutableStateOf("28 OCT") }
        var evtDateText by remember { mutableStateOf("2026-10-28") }
        var evtTime by remember { mutableStateOf("09:00 AM - 12:00 PM") }
        var evtLocation by remember { mutableStateOf("Campus Av. Universitaria") }
        var evtCategory by remember { mutableStateOf("Feria Vocacional") }
        var evtDescription by remember { mutableStateOf("") }

        androidx.compose.ui.window.Dialog(onDismissRequest = { showCreateEventDialog = false }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Publicar Nuevo Evento Oficial",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SantotoNavy
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = evtTitle,
                        onValueChange = { evtTitle = it },
                        label = { Text("Título del Evento *") },
                        singleLine = true,
                        colors = adminFieldColors,
                        modifier = Modifier.fillMaxWidth().testTag("modal_event_title")
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = evtBadge,
                            onValueChange = { evtBadge = it },
                            label = { Text("Badge (ej. 28 OCT)") },
                            singleLine = true,
                            colors = adminFieldColors,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = evtDateText,
                            onValueChange = { evtDateText = it },
                            label = { Text("Fecha YYYY-MM-DD") },
                            singleLine = true,
                            colors = adminFieldColors,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = evtCategory,
                            onValueChange = { evtCategory = it },
                            label = { Text("Categoría") },
                            singleLine = true,
                            colors = adminFieldColors,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = evtTime,
                            onValueChange = { evtTime = it },
                            label = { Text("Horario") },
                            singleLine = true,
                            colors = adminFieldColors,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = evtLocation,
                        onValueChange = { evtLocation = it },
                        label = { Text("Lugar / Campus") },
                        singleLine = true,
                        colors = adminFieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = evtDescription,
                        onValueChange = { evtDescription = it },
                        label = { Text("Descripción del evento *") },
                        colors = adminFieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { showCreateEventDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancelar", color = Color.DarkGray)
                        }

                        Button(
                            onClick = {
                                if (evtTitle.isNotBlank() && evtDescription.isNotBlank()) {
                                    viewModel.createEvent(
                                        title = evtTitle,
                                        dateBadge = evtBadge,
                                        dateText = evtDateText,
                                        time = evtTime,
                                        location = evtLocation,
                                        category = evtCategory,
                                        description = evtDescription,
                                        isFeatured = true
                                    )
                                    showCreateEventDialog = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SantotoNavy, contentColor = Color.White),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f).testTag("modal_event_save")
                        ) {
                            Text("Publicar Evento", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }

    // Modal Edit Event Dialog (Allows updating events as requested)
    eventToEdit?.let { evt ->
        var editEvtTitle by remember(evt) { mutableStateOf(evt.title) }
        var editEvtBadge by remember(evt) { mutableStateOf(evt.dateBadge) }
        var editEvtDate by remember(evt) { mutableStateOf(evt.dateText) }
        var editEvtTime by remember(evt) { mutableStateOf(evt.time) }
        var editEvtLocation by remember(evt) { mutableStateOf(evt.location) }
        var editEvtCategory by remember(evt) { mutableStateOf(evt.category) }
        var editEvtDesc by remember(evt) { mutableStateOf(evt.description) }

        androidx.compose.ui.window.Dialog(onDismissRequest = { eventToEdit = null }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth().padding(10.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Editar Evento Oficial",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SantotoNavy
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = editEvtTitle,
                        onValueChange = { editEvtTitle = it },
                        label = { Text("Título del Evento") },
                        singleLine = true,
                        colors = adminFieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = editEvtBadge,
                            onValueChange = { editEvtBadge = it },
                            label = { Text("Badge (ej. 28 OCT)") },
                            singleLine = true,
                            colors = adminFieldColors,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = editEvtDate,
                            onValueChange = { editEvtDate = it },
                            label = { Text("Fecha YYYY-MM-DD") },
                            singleLine = true,
                            colors = adminFieldColors,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = editEvtCategory,
                            onValueChange = { editEvtCategory = it },
                            label = { Text("Categoría") },
                            singleLine = true,
                            colors = adminFieldColors,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = editEvtTime,
                            onValueChange = { editEvtTime = it },
                            label = { Text("Horario") },
                            singleLine = true,
                            colors = adminFieldColors,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = editEvtLocation,
                        onValueChange = { editEvtLocation = it },
                        label = { Text("Lugar / Campus") },
                        singleLine = true,
                        colors = adminFieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = editEvtDesc,
                        onValueChange = { editEvtDesc = it },
                        label = { Text("Descripción") },
                        colors = adminFieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { eventToEdit = null },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancelar", color = Color.DarkGray)
                        }

                        Button(
                            onClick = {
                                viewModel.updateEvent(
                                    evt.copy(
                                        title = editEvtTitle.trim(),
                                        dateBadge = editEvtBadge.trim(),
                                        dateText = editEvtDate.trim(),
                                        time = editEvtTime.trim(),
                                        location = editEvtLocation.trim(),
                                        category = editEvtCategory.trim(),
                                        description = editEvtDesc.trim()
                                    )
                                )
                                eventToEdit = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SantotoNavy, contentColor = Color.White),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Guardar Cambios", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }

    // Modal Delete Event Confirmation
    eventToDelete?.let { evt ->
        AlertDialog(
            onDismissRequest = { eventToDelete = null },
            title = { Text("¿Eliminar evento?", fontWeight = FontWeight.Bold, color = SantotoNavy) },
            text = { Text("Se eliminará definitivamente el evento '${evt.title}'.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteEvent(evt)
                        eventToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626), contentColor = Color.White)
                ) {
                    Text("Eliminar", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                Button(
                    onClick = { eventToDelete = null },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9))
                ) {
                    Text("Cancelar", color = Color(0xFF475569))
                }
            }
        )
    }

    // Modal Attendance Management & Check-in for Events (Requested by user)
    eventForAttendance?.let { evt ->
        val eventRegs = allRegistrations.filter { it.eventId == evt.id }
        val totalReg = eventRegs.size
        val attendedCount = eventRegs.count { it.attended }
        val attendancePct = if (totalReg > 0) ((attendedCount.toFloat() / totalReg.toFloat()) * 100).toInt() else 0

        var attendanceFilter by remember { mutableStateOf("Todos") } // "Todos", "Asistieron", "Pendientes"
        var showAddWalkIn by remember { mutableStateOf(false) }
        var walkInName by remember { mutableStateOf("") }
        var walkInPhone by remember { mutableStateOf("") }
        var walkInEmail by remember { mutableStateOf("") }
        var walkInSchool by remember { mutableStateOf("") }

        val displayedRegs = remember(eventRegs, attendanceFilter) {
            when (attendanceFilter) {
                "Asistieron" -> eventRegs.filter { it.attended }
                "Pendientes" -> eventRegs.filter { !it.attended }
                else -> eventRegs
            }
        }

        androidx.compose.ui.window.Dialog(
            onDismissRequest = { eventForAttendance = null },
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SantotoBg),
                color = SantotoBg
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Top Bar
                    Surface(
                        color = SantotoNavy,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                IconButton(
                                    onClick = { eventForAttendance = null },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.15f))
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White, modifier = Modifier.size(18.dp))
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Control de Asistencia",
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = evt.title,
                                        color = SantotoGold,
                                        fontSize = 11.sp,
                                        maxLines = 1
                                    )
                                }
                            }

                            Button(
                                onClick = { showAddWalkIn = !showAddWalkIn },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (showAddWalkIn) Color.White.copy(alpha = 0.2f) else SantotoGold,
                                    contentColor = if (showAddWalkIn) Color.White else SantotoNavy
                                ),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (showAddWalkIn) "Cerrar" else "+ En Puerta",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Summary Metrics Banner
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text("RESUMEN DE ASISTENCIA EN TIEMPO REAL", fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = SantotoNavy)
                                            Text(
                                                text = "$attendedCount llegaron de $totalReg inscritos ($attendancePct%)",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = SantotoNavyDark
                                            )
                                        }

                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = if (attendancePct >= 50) Color(0xFF10B981).copy(alpha = 0.15f) else SantotoYellow.copy(alpha = 0.2f)
                                        ) {
                                            Text(
                                                text = "${evt.availableSpots} cupos libres",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = SantotoNavy,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    val ratio = if (totalReg > 0) (attendedCount.toFloat() / totalReg.toFloat()).coerceIn(0f, 1f) else 0f
                                    LinearProgressIndicator(
                                        progress = { ratio },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(8.dp)
                                            .clip(RoundedCornerShape(4.dp)),
                                        color = Color(0xFF10B981),
                                        trackColor = Color(0xFFE2E8F0)
                                    )
                                }
                            }
                        }

                        // Walk-in attendee form (if expanded)
                        if (showAddWalkIn) {
                            item {
                                Card(
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF86EFAC)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(
                                            text = "Registro Rápido de Asistente en Puerta",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = Color(0xFF166534)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))

                                        OutlinedTextField(
                                            value = walkInName,
                                            onValueChange = { walkInName = it },
                                            label = { Text("Nombre Completo *") },
                                            singleLine = true,
                                            colors = adminFieldColors,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))

                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            OutlinedTextField(
                                                value = walkInPhone,
                                                onValueChange = { walkInPhone = it },
                                                label = { Text("Teléfono *") },
                                                singleLine = true,
                                                colors = adminFieldColors,
                                                modifier = Modifier.weight(1f)
                                            )
                                            OutlinedTextField(
                                                value = walkInEmail,
                                                onValueChange = { walkInEmail = it },
                                                label = { Text("Correo Electrónico") },
                                                singleLine = true,
                                                colors = adminFieldColors,
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))

                                        OutlinedTextField(
                                            value = walkInSchool,
                                            onValueChange = { walkInSchool = it },
                                            label = { Text("Colegio / Procedencia") },
                                            singleLine = true,
                                            colors = adminFieldColors,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))

                                        Button(
                                            onClick = {
                                                if (walkInName.isNotBlank() && walkInPhone.isNotBlank()) {
                                                    viewModel.registerForEvent(
                                                        event = evt,
                                                        fullName = walkInName.trim(),
                                                        phone = walkInPhone.trim(),
                                                        email = walkInEmail.trim().ifBlank { "walkin@santoto.edu.co" },
                                                        school = walkInSchool.trim().ifBlank { "Asistente Presencial" }
                                                    ) { newReg ->
                                                        // Automatically mark as attended since they registered in person at the door!
                                                        viewModel.updateAttendance(newReg.id, true)
                                                        walkInName = ""
                                                        walkInPhone = ""
                                                        walkInEmail = ""
                                                        walkInSchool = ""
                                                        showAddWalkIn = false
                                                    }
                                                }
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF166534), contentColor = Color.White),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Registrar e Ingresar (Marcar Asistencia)", fontWeight = FontWeight.Bold, fontSize = 11.5.sp)
                                        }
                                    }
                                }
                            }
                        }

                        // Filter Chips
                        item {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf(
                                    "Todos" to totalReg,
                                    "Asistieron" to attendedCount,
                                    "Pendientes" to (totalReg - attendedCount)
                                ).forEach { (label, count) ->
                                    val isSelected = attendanceFilter == label
                                    Surface(
                                        shape = RoundedCornerShape(16.dp),
                                        color = if (isSelected) SantotoNavy else Color.White,
                                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) SantotoNavy else Color(0xFFCBD5E1)),
                                        modifier = Modifier.clickable { attendanceFilter = label }
                                    ) {
                                        Text(
                                            text = "$label ($count)",
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) SantotoGold else Color(0xFF334155),
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }
                        }

                        if (displayedRegs.isEmpty()) {
                            item {
                                Card(
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth().padding(24.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Icon(Icons.Default.People, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(36.dp))
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text("No hay asistentes con este filtro", fontWeight = FontWeight.Bold, color = SantotoNavyDark)
                                    }
                                }
                            }
                        }

                        // List of Attendees with check-in toggle
                        items(displayedRegs, key = { it.id }) { reg ->
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (reg.attended) Color(0xFFF0FDF4) else Color.White
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (reg.attended) Color(0xFF86EFAC) else Color(0xFFE2E8F0)
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = if (reg.attended) Color(0xFF166534) else SantotoNavy.copy(alpha = 0.1f),
                                        modifier = Modifier.size(40.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = if (reg.attended) Icons.Default.CheckCircle else Icons.Default.Person,
                                                contentDescription = null,
                                                tint = if (reg.attended) Color.White else SantotoNavy,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = reg.fullName,
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = SantotoNavyDark
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = SantotoNavy
                                            ) {
                                                Text(
                                                    text = reg.ticketCode,
                                                    fontSize = 8.5.sp,
                                                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                                    fontWeight = FontWeight.Bold,
                                                    color = SantotoGold,
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(2.dp))

                                        Text(
                                            text = "Tel: ${reg.phone}" + if (reg.schoolOrOrigin.isNotBlank()) " • ${reg.schoolOrOrigin}" else "",
                                            fontSize = 10.5.sp,
                                            color = Color(0xFF475569)
                                        )

                                        Spacer(modifier = Modifier.height(6.dp))

                                        // Direct contact actions (WhatsApp & Phone)
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            val cleanPhone = reg.phone.replace(Regex("[^0-9]"), "")
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = Color(0xFF25D366).copy(alpha = 0.15f),
                                                modifier = Modifier.clickable {
                                                    val url = "https://api.whatsapp.com/send?phone=57$cleanPhone&text=Hola ${reg.fullName}! Te confirmamos tu asistencia al evento ${evt.title} en la Universidad Santo Tomás Tunja. Tu código de acceso es ${reg.ticketCode}."
                                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                                    try { context.startActivity(intent) } catch (e: Exception) {}
                                                }
                                            ) {
                                                Text(
                                                    text = "WhatsApp",
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF15803D),
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }

                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = SantotoNavy.copy(alpha = 0.08f),
                                                modifier = Modifier.clickable {
                                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$cleanPhone"))
                                                    try { context.startActivity(intent) } catch (e: Exception) {}
                                                }
                                            ) {
                                                Text(
                                                    text = "Llamar",
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = SantotoNavy,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    // Registrar si llegó o no
                                    Button(
                                        onClick = {
                                            viewModel.updateAttendance(reg.id, !reg.attended)
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (reg.attended) Color(0xFF10B981) else Color(0xFFE2E8F0),
                                            contentColor = if (reg.attended) Color.White else Color(0xFF1E293B)
                                        ),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (reg.attended) Icons.Default.CheckCircle else Icons.Default.HowToReg,
                                            contentDescription = null,
                                            tint = if (reg.attended) Color.White else SantotoNavy,
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (reg.attended) "¡Llegó!" else "Marcar Llegada",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal Create News Dialog (CRUD Completo)
    if (showCreateNewsDialog) {
        var newsTitle by remember { mutableStateOf("") }
        var newsSummary by remember { mutableStateOf("") }
        var newsContent by remember { mutableStateOf("") }
        var newsCategory by remember { mutableStateOf("Admisiones") }
        var newsDate by remember { mutableStateOf("19 OCT 2026") }
        var newsAuthor by remember { mutableStateOf("Dirección de Comunicaciones USTA Tunja") }
        var newsImageUrl by remember { mutableStateOf("https://images.unsplash.com/photo-1523240795612-9a054b0db644?w=800&auto=format&fit=crop&q=80") }
        var newsReadTime by remember { mutableStateOf("3") }
        var newsIsFeatured by remember { mutableStateOf(false) }

        val newsCategories = listOf("Admisiones", "Colegios", "Innovación & IA", "Académico", "Internacional", "Institucional")

        androidx.compose.ui.window.Dialog(onDismissRequest = { showCreateNewsDialog = false }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "Publicar Noticia Oficial USTA",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SantotoNavy
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = newsTitle,
                        onValueChange = { newsTitle = it },
                        label = { Text("Título de la Noticia *") },
                        singleLine = true,
                        colors = adminFieldColors,
                        modifier = Modifier.fillMaxWidth().testTag("modal_news_title")
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    // Category selection chips
                    Text("Categoría:", fontSize = 11.sp, color = Color.Gray)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        newsCategories.take(4).forEach { cat ->
                            val isSel = newsCategory == cat
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (isSel) SantotoNavy else Color.LightGray.copy(alpha = 0.3f),
                                modifier = Modifier.clickable { newsCategory = cat }
                            ) {
                                Text(
                                    text = cat,
                                    fontSize = 10.sp,
                                    color = if (isSel) SantotoGold else Color.DarkGray,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = newsDate,
                            onValueChange = { newsDate = it },
                            label = { Text("Fecha (ej. 19 OCT 2026)") },
                            singleLine = true,
                            colors = adminFieldColors,
                            modifier = Modifier.weight(1.2f)
                        )
                        OutlinedTextField(
                            value = newsReadTime,
                            onValueChange = { newsReadTime = it },
                            label = { Text("Lectura (min)") },
                            singleLine = true,
                            colors = adminFieldColors,
                            modifier = Modifier.weight(0.8f)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = newsAuthor,
                        onValueChange = { newsAuthor = it },
                        label = { Text("Autor / Dependencia") },
                        singleLine = true,
                        colors = adminFieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = newsImageUrl,
                        onValueChange = { newsImageUrl = it },
                        label = { Text("URL de la Imagen") },
                        singleLine = true,
                        colors = adminFieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = newsSummary,
                        onValueChange = { newsSummary = it },
                        label = { Text("Resumen / Copete *") },
                        colors = adminFieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = newsContent,
                        onValueChange = { newsContent = it },
                        label = { Text("Contenido Completo del Artículo *") },
                        minLines = 3,
                        colors = adminFieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { showCreateNewsDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancelar", color = Color.DarkGray)
                        }

                        Button(
                            onClick = {
                                if (newsTitle.isNotBlank() && newsSummary.isNotBlank()) {
                                    viewModel.createNews(
                                        title = newsTitle.trim(),
                                        summary = newsSummary.trim(),
                                        content = newsContent.trim().ifBlank { newsSummary.trim() },
                                        category = newsCategory,
                                        dateText = newsDate.trim(),
                                        imageUrl = newsImageUrl.trim(),
                                        author = newsAuthor.trim(),
                                        readTimeMinutes = newsReadTime.toIntOrNull() ?: 3,
                                        isFeatured = newsIsFeatured
                                    )
                                    showCreateNewsDialog = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SantotoNavy, contentColor = Color.White),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Publicar Noticia", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }

    // Modal Edit News Dialog
    newsToEdit?.let { item ->
        var editTitle by remember(item) { mutableStateOf(item.title) }
        var editSummary by remember(item) { mutableStateOf(item.summary) }
        var editContent by remember(item) { mutableStateOf(item.content) }
        var editCategory by remember(item) { mutableStateOf(item.category) }
        var editDate by remember(item) { mutableStateOf(item.dateText) }
        var editAuthor by remember(item) { mutableStateOf(item.author) }
        var editImageUrl by remember(item) { mutableStateOf(item.imageUrl) }
        var editReadTime by remember(item) { mutableStateOf(item.readTimeMinutes.toString()) }

        androidx.compose.ui.window.Dialog(onDismissRequest = { newsToEdit = null }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "Editar Noticia",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SantotoNavy
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = editTitle,
                        onValueChange = { editTitle = it },
                        label = { Text("Título de la Noticia") },
                        singleLine = true,
                        colors = adminFieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = editCategory,
                            onValueChange = { editCategory = it },
                            label = { Text("Categoría") },
                            singleLine = true,
                            colors = adminFieldColors,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = editDate,
                            onValueChange = { editDate = it },
                            label = { Text("Fecha") },
                            singleLine = true,
                            colors = adminFieldColors,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = editImageUrl,
                        onValueChange = { editImageUrl = it },
                        label = { Text("URL de la Imagen") },
                        singleLine = true,
                        colors = adminFieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = editSummary,
                        onValueChange = { editSummary = it },
                        label = { Text("Resumen") },
                        colors = adminFieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = editContent,
                        onValueChange = { editContent = it },
                        label = { Text("Contenido") },
                        minLines = 3,
                        colors = adminFieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { newsToEdit = null },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancelar", color = Color.DarkGray)
                        }

                        Button(
                            onClick = {
                                viewModel.updateNews(
                                    item.copy(
                                        title = editTitle.trim(),
                                        summary = editSummary.trim(),
                                        content = editContent.trim(),
                                        category = editCategory.trim(),
                                        dateText = editDate.trim(),
                                        imageUrl = editImageUrl.trim(),
                                        readTimeMinutes = editReadTime.toIntOrNull() ?: item.readTimeMinutes
                                    )
                                )
                                newsToEdit = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SantotoNavy, contentColor = Color.White),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Guardar Cambios", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }

    // Modal Delete News Confirmation
    newsToDelete?.let { item ->
        AlertDialog(
            onDismissRequest = { newsToDelete = null },
            title = { Text("¿Eliminar noticia?", fontWeight = FontWeight.Bold, color = SantotoNavy) },
            text = { Text("Se eliminará definitivamente la noticia '${item.title}'.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteNews(item)
                        newsToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626), contentColor = Color.White)
                ) {
                    Text("Eliminar", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                Button(
                    onClick = { newsToDelete = null },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9))
                ) {
                    Text("Cancelar", color = Color(0xFF475569))
                }
            }
        )
    }
}

@Composable
private fun LeadsPipelineTab(
    leads: List<Lead>,
    selectedStatusFilter: LeadStatus?,
    onFilterSelect: (LeadStatus?) -> Unit,
    onAdvanceStatus: (Lead) -> Unit,
    onSetStatus: (Long, LeadStatus) -> Unit,
    onContactLead: (String) -> Unit
) {
    val filteredLeads = remember(leads, selectedStatusFilter) {
        if (selectedStatusFilter == null) leads else leads.filter { it.status == selectedStatusFilter }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Pipeline Stats Summary Chips
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                item {
                    StatusFilterChip(
                        label = "Todos (${leads.size})",
                        isSelected = selectedStatusFilter == null,
                        color = SantotoNavy,
                        onClick = { onFilterSelect(null) }
                    )
                }
                items(LeadStatus.values()) { status ->
                    val count = leads.count { it.status == status }
                    StatusFilterChip(
                        label = "${status.label} ($count)",
                        isSelected = selectedStatusFilter == status,
                        color = Color(status.colorHex),
                        onClick = { onFilterSelect(status) }
                    )
                }
            }
        }

        // List of Leads
        items(filteredLeads, key = { it.id }) { lead ->
            LeadCard(
                lead = lead,
                onAdvanceStatus = { onAdvanceStatus(lead) },
                onSetStatus = { newStatus -> onSetStatus(lead.id, newStatus) },
                onContactLead = { onContactLead(lead.phone) }
            )
        }
    }
}

@Composable
private fun LeadCard(
    lead: Lead,
    onAdvanceStatus: () -> Unit,
    onSetStatus: (LeadStatus) -> Unit,
    onContactLead: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth().testTag("lead_card_${lead.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Status Badge with selector
                Box {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(lead.status.colorHex).copy(alpha = 0.18f),
                        modifier = Modifier
                            .clickable { showMenu = true }
                            .testTag("lead_status_badge_${lead.id}")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Color(lead.status.colorHex))
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = lead.status.label,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(lead.status.colorHex)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        LeadStatus.values().forEach { st ->
                            DropdownMenuItem(
                                text = { Text(st.label, fontWeight = if (st == lead.status) FontWeight.Bold else FontWeight.Normal) },
                                onClick = {
                                    onSetStatus(st)
                                    showMenu = false
                                }
                            )
                        }
                    }
                }

                // WhatsApp / Call button
                IconButton(
                    onClick = onContactLead,
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(SantotoSuccess.copy(alpha = 0.15f))
                        .testTag("contact_lead_${lead.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Contactar",
                        tint = SantotoSuccess,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = lead.fullName,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = SantotoNavyDark
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.School, contentDescription = null, tint = SantotoNavy, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = lead.programInterest,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SantotoNavy
                )
            }

            if (lead.cityOrSchool.isNotBlank()) {
                Text(
                    text = "Origen: ${lead.cityOrSchool}",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = "Cel: ${lead.phone}" + if (lead.email.isNotBlank()) " • ${lead.email}" else "",
                fontSize = 10.sp,
                color = Color.DarkGray
            )

            if (lead.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = SantotoBg,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Nota: ${lead.notes}",
                        fontSize = 10.sp,
                        color = Color(0xFF475569),
                        modifier = Modifier.padding(6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action to advance status towards MATRICULADO
            if (lead.status != LeadStatus.MATRICULADO) {
                val nextLabel = when (lead.status) {
                    LeadStatus.NUEVO -> "Marcar como Contactado"
                    LeadStatus.CONTACTADO -> "Agendar Visita"
                    LeadStatus.VISITA_AGENDADA -> "Documentos Presentados"
                    LeadStatus.DOCUMENTOS_PRESENTADOS -> "Aprobar y Admitir"
                    LeadStatus.ADMITIDO -> "🎉 Finalizar Matrícula"
                    LeadStatus.MATRICULADO -> "Matriculado"
                }

                Button(
                    onClick = onAdvanceStatus,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (lead.status == LeadStatus.ADMITIDO) SantotoSuccess else SantotoNavy,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("advance_status_${lead.id}")
                ) {
                    Icon(
                        imageVector = if (lead.status == LeadStatus.ADMITIDO) Icons.Default.CheckCircle else Icons.AutoMirrored.Filled.TrendingUp,
                        contentDescription = null,
                        tint = SantotoGold,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Avanzar: $nextLabel",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = Color.White
                    )
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SantotoSuccess.copy(alpha = 0.12f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = SantotoSuccess,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "¡Estudiante Matriculado Oficialmente!",
                            fontWeight = FontWeight.Bold,
                            color = SantotoSuccess,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

// 2. ADMIN INTUITIVE CALENDAR VIEW FOR VISITS (Requested by user)
@Composable
private fun AdminVisitsCalendarTab(
    visits: List<CampusVisit>,
    leads: List<Lead>,
    onUpdateStatus: (Long, VisitStatus) -> Unit,
    onContactStudent: (String) -> Unit,
    onCreateVisitClick: () -> Unit,
    onEditVisitClick: (CampusVisit) -> Unit,
    onDeleteVisitClick: (CampusVisit) -> Unit,
    onScheduleLeadVisit: (Lead) -> Unit
) {
    var selectedCalendarDate by remember { mutableStateOf("2026-10-15") }
    var filterBySelectedDateOnly by remember { mutableStateOf(false) }

    val visitDates = remember(visits) { visits.map { it.dateText }.toSet() }

    val displayedVisits = remember(visits, selectedCalendarDate, filterBySelectedDateOnly) {
        if (filterBySelectedDateOnly) {
            visits.filter { it.dateText.startsWith(selectedCalendarDate) }
        } else {
            visits
        }
    }

    // Leads that have not completed a visit yet (NUEVO or CONTACTADO)
    val pendingLeads = remember(leads, visits) {
        val visitedPhones = visits.map { it.phone.replace(Regex("[^0-9]"), "") }.toSet()
        leads.filter { lead ->
            val cleanLeadPhone = lead.phone.replace(Regex("[^0-9]"), "")
            lead.status == LeadStatus.NUEVO || lead.status == LeadStatus.CONTACTADO || !visitedPhones.contains(cleanLeadPhone)
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Quick Action & Header Banner
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SantotoNavy),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Calendario de Visitas Guiadas",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "${visits.size} citas registradas • ${pendingLeads.size} leads por agendar",
                            color = SantotoGold,
                            fontSize = 10.sp
                        )
                    }

                    Button(
                        onClick = onCreateVisitClick,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SantotoGold, contentColor = SantotoNavy),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = SantotoNavy, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("+ Visita", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SantotoNavy)
                    }
                }
            }
        }

        // Visual Month Calendar for Admin
        item {
            InteractiveCalendarView(
                selectedDateText = selectedCalendarDate,
                onDateSelected = { date ->
                    selectedCalendarDate = date
                    filterBySelectedDateOnly = true
                },
                highlightedDates = visitDates
            )
        }

        // Calendar filter banner
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (filterBySelectedDateOnly) "Visitas del $selectedCalendarDate (${displayedVisits.size})" else "Todas las Visitas Agendadas (${visits.size})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SantotoNavy
                )

                if (filterBySelectedDateOnly) {
                    Text(
                        text = "Ver todas",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SantotoNavy,
                        modifier = Modifier.clickable { filterBySelectedDateOnly = false }
                    )
                }
            }
        }

        if (displayedVisits.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No hay visitas programadas para el $selectedCalendarDate",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { filterBySelectedDateOnly = false },
                            colors = ButtonDefaults.buttonColors(containerColor = SantotoNavy, contentColor = Color.White)
                        ) {
                            Text("Ver todas las fechas", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            items(displayedVisits, key = { it.id }) { visit ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = SantotoNavy
                            ) {
                                Text(
                                    text = visit.dateText,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SantotoGold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = when (visit.status) {
                                        VisitStatus.AGENDADA -> SantotoWarning.copy(alpha = 0.2f)
                                        VisitStatus.CONFIRMADA -> SantotoNavy.copy(alpha = 0.12f)
                                        VisitStatus.REALIZADA -> SantotoSuccess.copy(alpha = 0.2f)
                                        VisitStatus.CANCELADA -> Color.Red.copy(alpha = 0.2f)
                                    }
                                ) {
                                    Text(
                                        text = visit.status.label,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = when (visit.status) {
                                            VisitStatus.AGENDADA -> Color(0xFFB45309)
                                            VisitStatus.CONFIRMADA -> SantotoNavy
                                            VisitStatus.REALIZADA -> SantotoSuccess
                                            VisitStatus.CANCELADA -> Color.Red
                                        },
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(6.dp))

                                // Contact WhatsApp
                                IconButton(
                                    onClick = { onContactStudent(visit.phone) },
                                    modifier = Modifier.size(30.dp)
                                ) {
                                    Icon(Icons.Default.Call, contentDescription = "WhatsApp", tint = SantotoSuccess, modifier = Modifier.size(16.dp))
                                }

                                // Edit Visit
                                IconButton(
                                    onClick = { onEditVisitClick(visit) },
                                    modifier = Modifier.size(30.dp)
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = SantotoNavy, modifier = Modifier.size(16.dp))
                                }

                                // Delete Visit
                                IconButton(
                                    onClick = { onDeleteVisitClick(visit) },
                                    modifier = Modifier.size(30.dp)
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red.copy(alpha = 0.7f), modifier = Modifier.size(16.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = visit.studentName,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = SantotoNavyDark
                        )

                        Text(
                            text = "Carrera: ${visit.programInterest}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SantotoNavy
                        )

                        Text(
                            text = "${visit.campus} • ${visit.timeSlot}",
                            fontSize = 11.sp,
                            color = Color.DarkGray
                        )

                        Text(
                            text = "Acompañantes: ${visit.visitorCount} • Tel: ${visit.phone}",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (visit.status != VisitStatus.CONFIRMADA && visit.status != VisitStatus.REALIZADA) {
                                Button(
                                    onClick = { onUpdateStatus(visit.id, VisitStatus.CONFIRMADA) },
                                    shape = RoundedCornerShape(6.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = SantotoNavy, contentColor = Color.White),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Confirmar", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }

                            if (visit.status != VisitStatus.REALIZADA) {
                                Button(
                                    onClick = { onUpdateStatus(visit.id, VisitStatus.REALIZADA) },
                                    shape = RoundedCornerShape(6.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = SantotoSuccess, contentColor = Color.White),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Realizada", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section requested by user: Leads pending visit scheduling underneath calendar
        item {
            Spacer(modifier = Modifier.height(14.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.People, contentDescription = null, tint = SantotoNavy, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Aspirantes pendientes por agendar visita (${pendingLeads.size})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SantotoNavy
                )
            }
            Text(
                text = "Agrega y agenda rápidamente una visita personalizada con un solo toque",
                fontSize = 10.sp,
                color = Color(0xFF64748B)
            )
        }

        if (pendingLeads.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Todos los aspirantes registrados ya tienen visita agendada.",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(14.dp)
                    )
                }
            }
        } else {
            items(pendingLeads, key = { "lead_to_visit_${it.id}" }) { lead ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = lead.fullName, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = SantotoNavy)
                            Text(text = "Interés: ${lead.programInterest}", fontSize = 10.sp, color = Color(0xFF334155))
                            Text(text = "Tel: ${lead.phone}" + if (lead.cityOrSchool.isNotBlank()) " • ${lead.cityOrSchool}" else "", fontSize = 10.sp, color = Color.Gray)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { onScheduleLeadVisit(lead) },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SantotoNavy, contentColor = Color.White),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = SantotoGold, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Agendar Visita", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

// 3. ADMIN EVENTS MANAGEMENT TAB (Requested by user)
@Composable
private fun AdminEventsManagementTab(
    events: List<CampusEvent>,
    registrations: List<EventRegistration>,
    onCreateEventClick: () -> Unit,
    onEditEventClick: (CampusEvent) -> Unit,
    onDeleteEventClick: (CampusEvent) -> Unit,
    onViewAttendanceClick: (CampusEvent) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SantotoNavy),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Event, contentDescription = null, tint = SantotoGold, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Gestión de Eventos & Asistencia", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                            Text("Cupos en tiempo real, inscritos y control de asistencia", color = Color(0xFFCBD5E1), fontSize = 10.sp)
                        }
                    }

                    Button(
                        onClick = onCreateEventClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SantotoGold,
                            contentColor = SantotoNavy
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = SantotoNavy, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("+ Evento", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = SantotoNavy)
                    }
                }
            }
        }

        if (events.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Event, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No hay eventos registrados", fontWeight = FontWeight.Bold, color = SantotoNavyDark)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Haz clic en '+ Evento' para publicar el primer evento.", fontSize = 11.sp, color = Color.Gray)
                    }
                }
            }
        }

        items(events, key = { it.id }) { event ->
            val eventRegs = registrations.filter { it.eventId == event.id }
            val attendedCount = eventRegs.count { it.attended }

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        // Event Image Thumbnail
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.size(68.dp)
                        ) {
                            AsyncImage(
                                model = event.imageUrl,
                                contentDescription = event.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = SantotoYellow
                                ) {
                                    Text(
                                        text = event.dateBadge,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SantotoNavy,
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = SantotoNavy.copy(alpha = 0.08f)
                                ) {
                                    Text(
                                        text = event.category,
                                        fontSize = 8.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SantotoNavy,
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = event.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = SantotoNavyDark,
                                maxLines = 1
                            )

                            Text(
                                text = "${event.location} • ${event.time}",
                                fontSize = 10.sp,
                                color = Color.Gray,
                                maxLines = 1
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // Real-time cupos
                            Text(
                                text = "Cupos: ${event.availableSpots} disp. de ${event.capacity} total",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (event.availableSpots <= 5) Color.Red else SantotoNavy
                            )
                        }

                        Row {
                            IconButton(onClick = { onEditEventClick(event) }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = SantotoNavy, modifier = Modifier.size(18.dp))
                            }
                            IconButton(onClick = { onDeleteEventClick(event) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red.copy(alpha = 0.7f), modifier = Modifier.size(18.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Attendees and Attendance Check-in Action Bar
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = SantotoBg,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Inscritos: ${eventRegs.size} personas",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SantotoNavy
                                )
                                Text(
                                    text = "$attendedCount llegaron • ${eventRegs.size - attendedCount} pendientes",
                                    fontSize = 9.5.sp,
                                    color = Color(0xFF475569)
                                )
                            }

                            Button(
                                onClick = { onViewAttendanceClick(event) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = SantotoNavy,
                                    contentColor = Color.White
                                ),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Icon(Icons.Default.HowToReg, contentDescription = null, tint = SantotoGold, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Asistencia (${eventRegs.size})",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// 4. ADMIN NEWS MANAGEMENT TAB (CRUD Completo de Noticias)
@Composable
private fun AdminNewsManagementTab(
    newsList: List<CampusNews>,
    onCreateNewsClick: () -> Unit,
    onEditNewsClick: (CampusNews) -> Unit,
    onDeleteNewsClick: (CampusNews) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredNews = remember(newsList, searchQuery) {
        if (searchQuery.isBlank()) newsList
        else newsList.filter {
            it.title.contains(searchQuery, true) ||
            it.summary.contains(searchQuery, true) ||
            it.category.contains(searchQuery, true)
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SantotoNavy),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Newspaper, contentDescription = null, tint = SantotoGold, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Administración de Noticias USTA", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                            Text("CRUD completo de artículos, boletines y comunicados", color = Color(0xFFCBD5E1), fontSize = 10.sp)
                        }
                    }

                    Button(
                        onClick = onCreateNewsClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SantotoGold,
                            contentColor = SantotoNavy
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = SantotoNavy, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("+ Noticia", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = SantotoNavy)
                    }
                }
            }
        }

        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar en noticias...", fontSize = 12.sp, color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = SantotoNavy, modifier = Modifier.size(18.dp)) },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color(0xFF0F172A),
                    unfocusedTextColor = Color(0xFF0F172A),
                    focusedBorderColor = SantotoNavy,
                    unfocusedBorderColor = Color(0xFFCBD5E1),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (filteredNews.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Newspaper, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No hay noticias registradas", fontWeight = FontWeight.Bold, color = SantotoNavyDark)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Crea la primera noticia con el botón '+ Noticia'.", fontSize = 11.sp, color = Color.Gray)
                    }
                }
            }
        }

        items(filteredNews, key = { it.id }) { newsItem ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.size(60.dp)
                    ) {
                        AsyncImage(
                            model = newsItem.imageUrl,
                            contentDescription = newsItem.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = SantotoNavy.copy(alpha = 0.08f)
                            ) {
                                Text(
                                    text = newsItem.category,
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SantotoNavy,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(newsItem.dateText, fontSize = 9.sp, color = Color.Gray)
                        }

                        Spacer(modifier = Modifier.height(3.dp))

                        Text(
                            text = newsItem.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = SantotoNavyDark,
                            maxLines = 1
                        )

                        Text(
                            text = newsItem.summary,
                            fontSize = 9.5.sp,
                            color = Color.DarkGray,
                            maxLines = 2,
                            lineHeight = 13.sp
                        )
                    }

                    IconButton(onClick = { onEditNewsClick(newsItem) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = SantotoNavy, modifier = Modifier.size(18.dp))
                    }

                    IconButton(onClick = { onDeleteNewsClick(newsItem) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red.copy(alpha = 0.7f), modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}

// 4. CHATBOT ANALYTICS & METRICS TAB (Neat, clean, and well-organized)
@Composable
private fun ChatbotAnalyticsTab(
    totalQueries: Int,
    programStats: Map<String, Int>,
    leads: List<Lead>,
    visits: List<CampusVisit>,
    onNavigateToPipeline: () -> Unit,
    onNavigateToCalendar: () -> Unit
) {
    val totalLeads = leads.size
    val totalMatriculados = leads.count { it.status == LeadStatus.MATRICULADO }
    val totalVisitas = visits.size
    val conversionRate = if (totalLeads > 0) ((totalMatriculados.toFloat() / totalLeads.toFloat()) * 100).toInt() else 0

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // High-level KPIs Grid (2x2 for clean readability on mobile screens)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "RESUMEN DE ADMISIONES & MARKETING",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SantotoNavy
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    KpiCard(
                        title = "Consultas Tommy Bot",
                        value = "$totalQueries",
                        subtitle = "Interacciones IA",
                        color = SantotoNavy,
                        modifier = Modifier.weight(1f)
                    )
                    KpiCard(
                        title = "Leads Capturados",
                        value = "$totalLeads",
                        subtitle = "Aspirantes en embudo",
                        color = SantotoRoyal,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    KpiCard(
                        title = "Visitas Agendadas",
                        value = "$totalVisitas",
                        subtitle = "Campus Tunja",
                        color = SantotoGold,
                        modifier = Modifier.weight(1f)
                    )
                    KpiCard(
                        title = "Matriculados",
                        value = "$totalMatriculados",
                        subtitle = "$conversionRate% conversión total",
                        color = SantotoSuccess,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Quick Navigation Actions
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onNavigateToPipeline,
                    modifier = Modifier.weight(1f).height(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SantotoNavy,
                        contentColor = Color.White
                    )
                ) {
                    Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = SantotoGold, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Ver Pipeline", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                Button(
                    onClick = onNavigateToCalendar,
                    modifier = Modifier.weight(1f).height(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SantotoRoyal,
                        contentColor = Color.White
                    )
                ) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = SantotoGold, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Ver Calendario", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }

        // Funnel Pipeline Breakdown (Clean & orderly)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Analytics, contentDescription = null, tint = SantotoNavy, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Embudo de Conversión de Admisiones",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = SantotoNavyDark
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    LeadStatus.values().forEach { st ->
                        val count = leads.count { it.status == st }
                        val ratio = if (totalLeads > 0) count.toFloat() / totalLeads.toFloat() else 0f
                        val pct = (ratio * 100).toInt()
                        Column(modifier = Modifier.padding(vertical = 4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = CircleShape,
                                        color = Color(st.colorHex),
                                        modifier = Modifier.size(8.dp)
                                    ) {}
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = st.label,
                                        fontSize = 11.sp,
                                        color = SantotoNavyDark,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Text(
                                    text = "$count ($pct%)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(st.colorHex)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { ratio },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = Color(st.colorHex),
                                trackColor = SantotoBg
                            )
                        }
                    }
                }
            }
        }

        // Most Consulted Academic Programs
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Analytics, contentDescription = null, tint = SantotoNavy, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Programas Más Consultados en Tommy Bot",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = SantotoNavyDark
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val maxVal = programStats.values.maxOrNull() ?: 1
                    programStats.entries.sortedByDescending { it.value }.take(6).forEachIndexed { idx, (program, count) ->
                        val ratio = count.toFloat() / maxVal.toFloat()
                        Column(modifier = Modifier.padding(vertical = 4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = if (idx == 0) SantotoGold else SantotoNavy.copy(alpha = 0.1f),
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "${idx + 1}",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (idx == 0) SantotoNavy else SantotoNavyDark
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(program, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = SantotoNavyDark)
                                }
                                Text("$count consultas", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SantotoNavy)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { ratio },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(5.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = SantotoGold,
                                trackColor = SantotoBg
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun KpiCard(
    title: String,
    value: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(title, fontSize = 9.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = color)
            Spacer(modifier = Modifier.height(1.dp))
            Text(subtitle, fontSize = 8.sp, color = SantotoNavyDark)
        }
    }
}

@Composable
private fun StatusFilterChip(
    label: String,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) color else Color.White,
        modifier = Modifier
            .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color.White else color,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun AdminRagDocumentsTab(
    ragDocuments: List<RagDocument>,
    externalShowCreateDialog: Boolean = false,
    onOpenCreateDialog: () -> Unit = {},
    onDismissCreateDialog: () -> Unit = {},
    onCreateDocument: (title: String, category: String, content: String) -> Unit,
    onUpdateDocument: (RagDocument) -> Unit,
    onDeleteDocument: (RagDocument) -> Unit,
    onResetDefaults: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryFilter by remember { mutableStateOf<String?>(null) }
    var localShowCreateDialog by remember { mutableStateOf(false) }
    val showCreateDialog = localShowCreateDialog || externalShowCreateDialog
    var docToEdit by remember { mutableStateOf<RagDocument?>(null) }
    var docToDelete by remember { mutableStateOf<RagDocument?>(null) }
    var showResetConfirm by remember { mutableStateOf(false) }

    val docFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color(0xFF0F172A),
        unfocusedTextColor = Color(0xFF0F172A),
        focusedLabelColor = SantotoNavy,
        unfocusedLabelColor = Color(0xFF475569),
        focusedBorderColor = SantotoNavy,
        unfocusedBorderColor = Color(0xFF94A3B8),
        cursorColor = SantotoNavy,
        focusedContainerColor = Color(0xFFF8FAFC),
        unfocusedContainerColor = Color.White
    )

    val categories = listOf("Todos", "Pregrados", "Posgrados", "Microcredenciales", "Directorio", "Requisitos", "Inversión")

    val filteredDocs = ragDocuments.filter { doc ->
        val matchesCategory = selectedCategoryFilter == null || selectedCategoryFilter == "Todos" || doc.category.equals(selectedCategoryFilter, ignoreCase = true)
        val matchesSearch = searchQuery.isBlank() || doc.title.contains(searchQuery, ignoreCase = true) || doc.content.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Banner Header
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SantotoNavy),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Description, contentDescription = null, tint = SantotoYellow, modifier = Modifier.size(22.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Conocimiento Tommy",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "${ragDocuments.size} documentos de conocimiento activos en tiempo real",
                                    fontSize = 10.sp,
                                    color = SantotoYellow
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tommy Bot consulta esta información oficial para responder a los aspirantes sobre carreras, valores de matrícula, convenios y requisitos. Modifica o crea documentos y se actualizará al instante.",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.85f),
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {
                                onOpenCreateDialog()
                                localShowCreateDialog = true
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SantotoYellow),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("new_rag_doc_button")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = SantotoNavy, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("+ Nuevo Documento", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SantotoNavy)
                        }

                        Button(
                            onClick = { showResetConfirm = true },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.15f)),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.RestartAlt, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Restablecer Oficiales", fontSize = 11.sp, color = Color.White)
                        }
                    }
                }
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar en documentos o carreras...", fontSize = 12.sp, color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = docFieldColors
            )
        }

        // Category Filter Chips
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { cat ->
                    val isSelected = (selectedCategoryFilter == null && cat == "Todos") || (selectedCategoryFilter == cat)
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) SantotoNavy else Color.White,
                        modifier = Modifier
                            .border(1.dp, if (isSelected) SantotoNavy else Color(0xFFCBD5E1), RoundedCornerShape(10.dp))
                            .clickable {
                                selectedCategoryFilter = if (cat == "Todos") null else cat
                            }
                    ) {
                        Text(
                            text = cat,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) SantotoYellow else SantotoNavyDark,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }
        }

        // Documents List
        if (filteredDocs.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("No se encontraron archivos .md con ese criterio", color = Color.Gray, fontSize = 12.sp)
                    }
                }
            }
        } else {
            items(filteredDocs, key = { it.id }) { doc ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier.fillMaxWidth().testTag("rag_doc_card_${doc.id}")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = when (doc.category) {
                                        "Pregrados" -> SantotoNavy
                                        "Posgrados" -> SantotoRoyal
                                        "Microcredenciales" -> SantotoCyan
                                        "Directorio" -> SantotoSuccess
                                        else -> SantotoGold
                                    }
                                ) {
                                    Text(
                                        text = doc.category.uppercase(),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (doc.category == "Inversión") SantotoNavy else Color.White,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = doc.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = SantotoNavyDark
                                )
                            }

                            Text(
                                text = "${doc.content.length} caracteres",
                                fontSize = 10.sp,
                                color = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Actualizado: " + dateFormat.format(Date(doc.lastUpdated)),
                            fontSize = 10.sp,
                            color = Color(0xFF64748B)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Preview of content in styled container
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFF8FAFC),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = doc.content.take(220) + if (doc.content.length > 220) "..." else "",
                                fontSize = 11.sp,
                                color = Color(0xFF334155),
                                modifier = Modifier.padding(10.dp),
                                lineHeight = 16.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { docToDelete = doc },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEE2E2)),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                modifier = Modifier.testTag("delete_doc_btn_${doc.id}")
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(3.dp))
                                Text("Eliminar", fontSize = 11.sp, color = Color(0xFFDC2626), fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = { docToEdit = doc },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SantotoNavy),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.testTag("edit_doc_btn_${doc.id}")
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = null, tint = SantotoYellow, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Ver / Editar", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal Create RAG Doc Dialog
    if (showCreateDialog) {
        var newTitle by remember { mutableStateOf("") }
        var newCategory by remember { mutableStateOf("Pregrados") }
        var newContent by remember { mutableStateOf("") }

        androidx.compose.ui.window.Dialog(onDismissRequest = {
            localShowCreateDialog = false
            onDismissCreateDialog()
        }) {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Crear Documento de Conocimiento",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SantotoNavy
                    )
                    Text(
                        text = "El contenido se integrará inmediatamente al conocimiento de Tommy Bot",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = newTitle,
                        onValueChange = { newTitle = it },
                        label = { Text("Nombre del documento (ej. Pregrados Tunja.md)", fontWeight = FontWeight.Medium) },
                        singleLine = true,
                        colors = docFieldColors,
                        modifier = Modifier.fillMaxWidth().testTag("input_new_doc_title")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = newCategory,
                        onValueChange = { newCategory = it },
                        label = { Text("Categoría (Pregrados, Posgrados, Sedes...)", fontWeight = FontWeight.Medium) },
                        singleLine = true,
                        colors = docFieldColors,
                        modifier = Modifier.fillMaxWidth().testTag("input_new_doc_category")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = newContent,
                        onValueChange = { newContent = it },
                        label = { Text("Contenido informativo", fontWeight = FontWeight.Medium) },
                        minLines = 8,
                        maxLines = 14,
                        colors = docFieldColors,
                        modifier = Modifier.fillMaxWidth().testTag("input_new_doc_content")
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                localShowCreateDialog = false
                                onDismissCreateDialog()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9))
                        ) {
                            Text("Cancelar", color = Color(0xFF475569), fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (newTitle.isNotBlank() && newContent.isNotBlank()) {
                                    onCreateDocument(newTitle.trim(), newCategory.trim(), newContent.trim())
                                    localShowCreateDialog = false
                                    onDismissCreateDialog()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SantotoNavy, contentColor = Color.White),
                            modifier = Modifier.testTag("submit_create_doc_btn")
                        ) {
                            Text("Guardar Documento", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }

    // Modal Edit RAG Doc Dialog
    docToEdit?.let { doc ->
        var editTitle by remember(doc) { mutableStateOf(doc.title) }
        var editCategory by remember(doc) { mutableStateOf(doc.category) }
        var editContent by remember(doc) { mutableStateOf(doc.content) }

        androidx.compose.ui.window.Dialog(onDismissRequest = { docToEdit = null }) {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Editar Documento de Conocimiento",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SantotoNavy
                    )
                    Text(
                        text = "Cualquier modificación actualizará las respuestas de Tommy Bot en tiempo real",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = editTitle,
                        onValueChange = { editTitle = it },
                        label = { Text("Título del documento", fontWeight = FontWeight.Medium) },
                        singleLine = true,
                        colors = docFieldColors,
                        modifier = Modifier.fillMaxWidth().testTag("input_edit_doc_title")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = editCategory,
                        onValueChange = { editCategory = it },
                        label = { Text("Categoría", fontWeight = FontWeight.Medium) },
                        singleLine = true,
                        colors = docFieldColors,
                        modifier = Modifier.fillMaxWidth().testTag("input_edit_doc_category")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = editContent,
                        onValueChange = { editContent = it },
                        label = { Text("Contenido (${editContent.length} caracteres)", fontWeight = FontWeight.Medium) },
                        minLines = 9,
                        maxLines = 14,
                        colors = docFieldColors,
                        modifier = Modifier.fillMaxWidth().testTag("input_edit_doc_content")
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = { docToEdit = null },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9))
                        ) {
                            Text("Cancelar", color = Color(0xFF475569), fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (editTitle.isNotBlank() && editContent.isNotBlank()) {
                                    onUpdateDocument(
                                        doc.copy(
                                            title = editTitle.trim(),
                                            category = editCategory.trim(),
                                            content = editContent.trim()
                                        )
                                    )
                                    docToEdit = null
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SantotoNavy, contentColor = Color.White),
                            modifier = Modifier.testTag("submit_edit_doc_btn")
                        ) {
                            Text("Guardar Cambios", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }

    // Modal Delete Confirmation Dialog
    docToDelete?.let { doc ->
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { docToDelete = null },
            title = { Text("¿Eliminar archivo ${doc.title}?", fontWeight = FontWeight.Bold, color = SantotoNavy) },
            text = { Text("Este archivo dejará de ser consultado por Tommy Bot al responder a los aspirantes.", fontSize = 12.sp) },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteDocument(doc)
                        docToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Text("Eliminar", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                Button(
                    onClick = { docToDelete = null },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9))
                ) {
                    Text("Cancelar", color = Color(0xFF475569))
                }
            }
        )
    }

    // Modal Reset Confirmation Dialog
    if (showResetConfirm) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showResetConfirm = false },
            title = { Text("¿Restablecer archivos originales?", fontWeight = FontWeight.Bold, color = SantotoNavy) },
            text = { Text("Se reestablecerán los archivos oficiales por defecto (Pregrados, Posgrados, Directorio, Microcredenciales y Requisitos).", fontSize = 12.sp) },
            confirmButton = {
                Button(
                    onClick = {
                        onResetDefaults()
                        showResetConfirm = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SantotoNavy)
                ) {
                    Text("Restablecer", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showResetConfirm = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9))
                ) {
                    Text("Cancelar", color = Color(0xFF475569))
                }
            }
        )
    }
}
