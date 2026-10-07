package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
    val ragDocuments by viewModel.ragDocuments.collectAsStateWithLifecycle()
    val totalQueries by viewModel.totalQueriesCount.collectAsStateWithLifecycle()
    val programStats by viewModel.queriedProgramsStats.collectAsStateWithLifecycle()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Pipeline Leads", "Calendario Visitas", "Gestión Eventos", "Base RAG (.md)", "Estadísticas")

    var selectedStatusFilter by remember { mutableStateOf<LeadStatus?>(null) }
    var showCreateLeadDialog by remember { mutableStateOf(false) }
    var showCreateEventDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SantotoBg)
    ) {
        // Portal Banner
        Surface(
            color = SantotoNavyDark,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = SantotoGold,
                            modifier = Modifier.size(34.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.AdminPanelSettings,
                                    contentDescription = null,
                                    tint = SantotoNavyDark,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Panel de Admisiones & Marketing",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "${leads.size} Aspirantes • ${visits.size} Visitas • ${events.size} Eventos • ${ragDocuments.size} Docs .md",
                                fontSize = 10.sp,
                                color = SantotoGold
                            )
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Button(
                            onClick = { showCreateLeadDialog = true },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SantotoGold),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("add_lead_button")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = SantotoNavyDark, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("+ Lead", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SantotoNavyDark)
                        }

                        Button(
                            onClick = { showCreateEventDialog = true },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SantotoCyan),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("add_event_button")
                        ) {
                            Icon(Icons.Default.Event, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("+ Evento", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }

        // Sub Tabs
        TabRow(
            selectedTabIndex = selectedTabIndex,
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
                            fontSize = 11.sp,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedTabIndex == index) SantotoGold else Color.LightGray
                        )
                    }
                )
            }
        }

        // Tab Content
        when (selectedTabIndex) {
            0 -> LeadsPipelineTab(
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
            1 -> AdminVisitsCalendarTab(
                visits = visits,
                onUpdateStatus = { visitId, status -> viewModel.updateVisitStatus(visitId, status) },
                onContactStudent = { phone ->
                    val cleanPhone = phone.replace(Regex("[^0-9]"), "")
                    val url = "https://api.whatsapp.com/send?phone=57$cleanPhone&text=Hola! Te contactamos para confirmar tu visita al campus de la Santoto Tunja."
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    try { context.startActivity(intent) } catch (e: Exception) { /* ignore */ }
                }
            )
            2 -> AdminEventsManagementTab(
                events = events,
                onDeleteEvent = { event -> viewModel.deleteEvent(event) }
            )
            3 -> AdminRagDocumentsTab(
                ragDocuments = ragDocuments,
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
            4 -> ChatbotAnalyticsTab(
                totalQueries = totalQueries,
                programStats = programStats,
                leads = leads,
                visits = visits
            )
        }
    }

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
                        modifier = Modifier.fillMaxWidth().testTag("modal_lead_name")
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newPhone,
                        onValueChange = { newPhone = it },
                        label = { Text("Teléfono / WhatsApp *") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("modal_lead_phone")
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newEmail,
                        onValueChange = { newEmail = it },
                        label = { Text("Correo electrónico") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newSchool,
                        onValueChange = { newSchool = it },
                        label = { Text("Colegio / Municipio") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newProgram,
                        onValueChange = { newProgram = it },
                        label = { Text("Programa de interés *") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newNotes,
                        onValueChange = { newNotes = it },
                        label = { Text("Observaciones / Estado inicial") },
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
                            colors = ButtonDefaults.buttonColors(containerColor = SantotoNavy),
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
                        modifier = Modifier.fillMaxWidth().testTag("modal_event_title")
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = evtBadge,
                            onValueChange = { evtBadge = it },
                            label = { Text("Badge (ej. 28 OCT)") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = evtDateText,
                            onValueChange = { evtDateText = it },
                            label = { Text("Fecha YYYY-MM-DD") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = evtTime,
                        onValueChange = { evtTime = it },
                        label = { Text("Horario") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = evtLocation,
                        onValueChange = { evtLocation = it },
                        label = { Text("Lugar / Campus") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = evtDescription,
                        onValueChange = { evtDescription = it },
                        label = { Text("Descripción del evento *") },
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
                            colors = ButtonDefaults.buttonColors(containerColor = SantotoNavy),
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
                        containerColor = if (lead.status == LeadStatus.ADMITIDO) SantotoSuccess else SantotoNavy
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
                        fontSize = 11.sp
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
    onUpdateStatus: (Long, VisitStatus) -> Unit,
    onContactStudent: (String) -> Unit
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

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
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
                        color = SantotoCyan,
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
                            colors = ButtonDefaults.buttonColors(containerColor = SantotoNavy)
                        ) {
                            Text("Ver todas las fechas", fontSize = 11.sp)
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
                                        VisitStatus.CONFIRMADA -> SantotoCyan.copy(alpha = 0.2f)
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
                                            VisitStatus.CONFIRMADA -> SantotoCyan
                                            VisitStatus.REALIZADA -> SantotoSuccess
                                            VisitStatus.CANCELADA -> Color.Red
                                        },
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(6.dp))

                                IconButton(
                                    onClick = { onContactStudent(visit.phone) },
                                    modifier = Modifier.size(30.dp)
                                ) {
                                    Icon(Icons.Default.Call, contentDescription = "WhatsApp", tint = SantotoSuccess, modifier = Modifier.size(16.dp))
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
                                    colors = ButtonDefaults.buttonColors(containerColor = SantotoNavy),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Confirmar", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            if (visit.status != VisitStatus.REALIZADA) {
                                Button(
                                    onClick = { onUpdateStatus(visit.id, VisitStatus.REALIZADA) },
                                    shape = RoundedCornerShape(6.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = SantotoSuccess),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Realizada", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
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
    onDeleteEvent: (CampusEvent) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SantotoNavy),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Event, contentDescription = null, tint = SantotoGold, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("Gestión de Eventos & Open Days", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                        Text("Los eventos publicados se muestran directamente en la pantalla de inicio", color = SantotoGold, fontSize = 10.sp)
                    }
                }
            }
        }

        items(events, key = { it.id }) { event ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SantotoNavy,
                        modifier = Modifier.size(46.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = event.dateBadge,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = SantotoGold,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = event.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = SantotoNavyDark
                        )
                        Text(
                            text = "${event.location} • ${event.time}",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = event.description,
                            fontSize = 10.sp,
                            color = Color.DarkGray,
                            maxLines = 2
                        )
                    }

                    IconButton(onClick = { onDeleteEvent(event) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red.copy(alpha = 0.7f), modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}

// 4. CHATBOT ANALYTICS TAB
@Composable
private fun ChatbotAnalyticsTab(
    totalQueries: Int,
    programStats: Map<String, Int>,
    leads: List<Lead>,
    visits: List<CampusVisit>
) {
    val totalLeads = leads.size
    val totalMatriculados = leads.count { it.status == LeadStatus.MATRICULADO }
    val conversionRate = if (totalLeads > 0) ((totalMatriculados.toFloat() / totalLeads.toFloat()) * 100).toInt() else 0

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // High-level KPIs Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                KpiCard(
                    title = "Consultas Bot",
                    value = "$totalQueries",
                    subtitle = "Cohere + RAG",
                    color = SantotoNavy,
                    modifier = Modifier.weight(1f)
                )
                KpiCard(
                    title = "Leads Activos",
                    value = "$totalLeads",
                    subtitle = "${visits.size} con visita",
                    color = SantotoRoyal,
                    modifier = Modifier.weight(1f)
                )
                KpiCard(
                    title = "Matriculados",
                    value = "$totalMatriculados",
                    subtitle = "$conversionRate% conversión",
                    color = SantotoSuccess,
                    modifier = Modifier.weight(1f)
                )
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
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Analytics, contentDescription = null, tint = SantotoNavy, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Programas Más Consultados en el Chatbot",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = SantotoNavyDark
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    val maxVal = programStats.values.maxOrNull() ?: 1
                    programStats.entries.sortedByDescending { it.value }.take(6).forEach { (program, count) ->
                        val ratio = count.toFloat() / maxVal.toFloat()
                        Column(modifier = Modifier.padding(vertical = 3.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(program, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = SantotoNavyDark)
                                Text("$count preguntas", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                            }
                            Spacer(modifier = Modifier.height(2.dp))
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

        // Funnel Pipeline Breakdown
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Embudo de Admisiones 2026 - 2027",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = SantotoNavyDark
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    LeadStatus.values().forEach { st ->
                        val count = leads.count { it.status == st }
                        val ratio = if (totalLeads > 0) count.toFloat() / totalLeads.toFloat() else 0f
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = st.label,
                                fontSize = 10.sp,
                                modifier = Modifier.width(120.dp),
                                color = Color(st.colorHex),
                                fontWeight = FontWeight.Bold
                            )
                            LinearProgressIndicator(
                                progress = { ratio },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = Color(st.colorHex),
                                trackColor = SantotoBg
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "$count",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = SantotoNavyDark
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
    onCreateDocument: (title: String, category: String, content: String) -> Unit,
    onUpdateDocument: (RagDocument) -> Unit,
    onDeleteDocument: (RagDocument) -> Unit,
    onResetDefaults: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryFilter by remember { mutableStateOf<String?>(null) }
    var showCreateDialog by remember { mutableStateOf(false) }
    var docToEdit by remember { mutableStateOf<RagDocument?>(null) }
    var docToDelete by remember { mutableStateOf<RagDocument?>(null) }
    var showResetConfirm by remember { mutableStateOf(false) }

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
                                    text = "Base de Conocimiento Tommy Bot (.md)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "${ragDocuments.size} archivos Markdown activos en tiempo real",
                                    fontSize = 10.sp,
                                    color = SantotoYellow
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "El chatbot consume estos archivos directamente. Puedes editar las carreras, costos, requisitos o agregar nuevos documentos .md y Tommy Bot se actualizará inmediatamente sin reiniciar.",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.85f),
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { showCreateDialog = true },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SantotoYellow),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("new_rag_doc_button")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = SantotoNavy, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("+ Nuevo Archivo .md", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SantotoNavy)
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
                placeholder = { Text("Buscar en archivos .md o carreras...", fontSize = 12.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
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

        androidx.compose.ui.window.Dialog(onDismissRequest = { showCreateDialog = false }) {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Crear Nuevo Archivo .md",
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
                        label = { Text("Nombre del archivo (ej. Pregrados Tunja.md)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("input_new_doc_title")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = newCategory,
                        onValueChange = { newCategory = it },
                        label = { Text("Categoría (Pregrados, Posgrados, Directorio...)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("input_new_doc_category")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = newContent,
                        onValueChange = { newContent = it },
                        label = { Text("Contenido en Markdown") },
                        minLines = 8,
                        maxLines = 14,
                        modifier = Modifier.fillMaxWidth().testTag("input_new_doc_content")
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = { showCreateDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9))
                        ) {
                            Text("Cancelar", color = Color(0xFF475569), fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (newTitle.isNotBlank() && newContent.isNotBlank()) {
                                    onCreateDocument(newTitle.trim(), newCategory.trim(), newContent.trim())
                                    showCreateDialog = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SantotoNavy),
                            modifier = Modifier.testTag("submit_create_doc_btn")
                        ) {
                            Text("Guardar Archivo", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
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
                        text = "Editar Archivo Markdown",
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
                        label = { Text("Título del archivo") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("input_edit_doc_title")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = editCategory,
                        onValueChange = { editCategory = it },
                        label = { Text("Categoría") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("input_edit_doc_category")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = editContent,
                        onValueChange = { editContent = it },
                        label = { Text("Contenido en Markdown (${editContent.length} caracteres)") },
                        minLines = 9,
                        maxLines = 14,
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
                            colors = ButtonDefaults.buttonColors(containerColor = SantotoNavy),
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
