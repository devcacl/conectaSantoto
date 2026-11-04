package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.window.Dialog
import com.example.data.model.AcademicProgram
import com.example.data.model.FacultyCategory
import com.example.data.model.ProgramLevel
import com.example.data.rag.SantotoKnowledgeBase
import com.example.ui.theme.SantotoBg
import com.example.ui.theme.SantotoGold
import com.example.ui.theme.SantotoNavy
import com.example.ui.theme.SantotoOutline
import com.example.ui.theme.SantotoSurfaceVariant
import com.example.ui.theme.SantotoTextBody
import com.example.ui.theme.SantotoTextSecondary
import com.example.ui.theme.SantotoWhatsApp
import com.example.ui.theme.SantotoYellow
import com.example.viewmodel.SantotoViewModel
import com.example.viewmodel.Screen

private fun FacultyCategory.label() = when (this) {
    FacultyCategory.INGENIERIAS_Y_ARQUITECTURA -> "Ingenierías y Arquitectura"
    FacultyCategory.CIENCIAS_ECONOMICAS_Y_ADMINISTRATIVAS -> "Ciencias Económicas y Administrativas"
    FacultyCategory.CIENCIAS_JURIDICAS_Y_POLITICAS -> "Ciencias Jurídicas y Políticas"
    FacultyCategory.CIENCIAS_DE_LA_SALUD_Y_DEPORTE -> "Salud y Deporte"
    FacultyCategory.EDUCACION_Y_HUMANIDADES -> "Educación y Humanidades"
}

@Composable
fun ProgramsCatalogScreen(
    viewModel: SantotoViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    val selectedLevelFilter by viewModel.selectedLevelFilter.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    var selectedProgramForModal by remember { mutableStateOf<AcademicProgram?>(null) }

    val filteredPrograms = remember(selectedLevelFilter, searchQuery) {
        SantotoKnowledgeBase.PREGRADOS.filter { prog ->
            val matchesLevel = selectedLevelFilter == null || selectedLevelFilter == ProgramLevel.PREGRADO
            val matchesSearch = searchQuery.isBlank() ||
                    prog.name.contains(searchQuery, ignoreCase = true) ||
                    prog.diferenciador.contains(searchQuery, ignoreCase = true) ||
                    prog.titulo.contains(searchQuery, ignoreCase = true)
            matchesLevel && matchesSearch
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SantotoBg)
    ) {
        CatalogHeader(
            searchQuery = searchQuery,
            onSearchChange = viewModel::setSearchQuery,
            selectedLevel = selectedLevelFilter,
            onLevelChange = viewModel::setLevelFilter
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            when (selectedLevelFilter) {
                ProgramLevel.MICROCREDENCIAL -> {
                    items(SantotoKnowledgeBase.MICROCREDENCIALES_GRADO_11) { microText ->
                        SimpleOfferCard(
                            badge = "Microcredencial grado 11°",
                            trailing = "\$100.000",
                            text = microText,
                            actionLabel = "Consultar requisitos",
                            onAction = {
                                viewModel.sendChatMessage("Explícame la microcredencial: $microText")
                                viewModel.navigateTo(Screen.Chatbot)
                            }
                        )
                    }
                }

                ProgramLevel.POSGRADO -> {
                    items(SantotoKnowledgeBase.POSGRADOS_RESUMEN) { posgradoText ->
                        SimpleOfferCard(
                            badge = "Posgrado",
                            trailing = null,
                            text = posgradoText,
                            actionLabel = "Pedir plan de estudios",
                            onAction = {
                                viewModel.sendChatMessage("Detalles e inversión del posgrado: $posgradoText")
                                viewModel.navigateTo(Screen.Chatbot)
                            }
                        )
                    }
                }

                else -> {
                    item {
                        Text(
                            text = if (filteredPrograms.size == 1) "1 programa" else "${filteredPrograms.size} programas",
                            style = MaterialTheme.typography.labelMedium,
                            color = SantotoTextSecondary
                        )
                    }
                    if (filteredPrograms.isEmpty()) {
                        item {
                            EmptySearch(
                                query = searchQuery,
                                onClear = { viewModel.setSearchQuery("") },
                                onAskBot = {
                                    viewModel.sendChatMessage("Busco un programa relacionado con: $searchQuery")
                                    viewModel.navigateTo(Screen.Chatbot)
                                }
                            )
                        }
                    }
                    items(filteredPrograms, key = { it.id }) { program ->
                        ProgramCard(program = program, onClick = { selectedProgramForModal = program })
                    }
                }
            }
        }
    }

    selectedProgramForModal?.let { prog ->
        ProgramDetailDialog(
            program = prog,
            onDismiss = { selectedProgramForModal = null },
            onAskBot = {
                selectedProgramForModal = null
                viewModel.sendChatMessage("Quiero todos los detalles para matricularme a ${prog.name}")
                viewModel.navigateTo(Screen.Chatbot)
            },
            onScheduleVisit = {
                selectedProgramForModal = null
                viewModel.navigateTo(Screen.ScheduleVisit)
            }
        )
    }
}

@Composable
private fun CatalogHeader(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    selectedLevel: ProgramLevel?,
    onLevelChange: (ProgramLevel?) -> Unit
) {
    Surface(color = SantotoNavy, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                placeholder = {
                    Text("Buscar programa", style = MaterialTheme.typography.bodyMedium, maxLines = 1, color = Color.White.copy(alpha = 0.7f))
                },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = SantotoYellow) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchChange("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Limpiar búsqueda", tint = Color.White)
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = SantotoYellow,
                    focusedBorderColor = SantotoYellow,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.35f),
                    focusedContainerColor = Color.White.copy(alpha = 0.08f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.08f)
                ),
                modifier = Modifier.fillMaxWidth().testTag("program_search_input")
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item { LevelChip("Todos", selectedLevel == null) { onLevelChange(null) } }
                item { LevelChip("Pregrados", selectedLevel == ProgramLevel.PREGRADO) { onLevelChange(ProgramLevel.PREGRADO) } }
                item { LevelChip("Microcredenciales 11°", selectedLevel == ProgramLevel.MICROCREDENCIAL) { onLevelChange(ProgramLevel.MICROCREDENCIAL) } }
                item { LevelChip("Posgrados", selectedLevel == ProgramLevel.POSGRADO) { onLevelChange(ProgramLevel.POSGRADO) } }
            }
        }
    }
}

@Composable
private fun LevelChip(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label, style = MaterialTheme.typography.labelLarge) },
        shape = RoundedCornerShape(20.dp),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color.White.copy(alpha = 0.12f),
            labelColor = Color.White,
            selectedContainerColor = SantotoYellow,
            selectedLabelColor = SantotoNavy
        ),
        border = null
    )
}

@Composable
private fun ProgramCard(program: AcademicProgram, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().testTag("program_card_${program.code}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, SantotoOutline)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = program.faculty.label().uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = SantotoTextSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = program.name,
                style = MaterialTheme.typography.titleMedium,
                color = SantotoNavy
            )

            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                InfoChip(program.modalidad)
                InfoChip(program.semestres)
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = SantotoOutline)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Inversión semestral 2026", style = MaterialTheme.typography.labelMedium, color = SantotoTextSecondary)
                    Text(program.inversion2026, style = MaterialTheme.typography.titleMedium, color = SantotoNavy)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Ver detalle", style = MaterialTheme.typography.labelLarge, color = SantotoNavy)
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = null,
                        tint = SantotoNavy,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoChip(text: String) {
    Surface(shape = RoundedCornerShape(8.dp), color = SantotoSurfaceVariant) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = SantotoTextBody,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

/** Tarjeta para microcredenciales y posgrados, cuya oferta es una línea de texto. */
@Composable
private fun SimpleOfferCard(
    badge: String,
    trailing: String?,
    text: String,
    actionLabel: String,
    onAction: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, SantotoOutline)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(shape = RoundedCornerShape(6.dp), color = SantotoYellow) {
                    Text(
                        text = badge.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = SantotoNavy,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
                if (trailing != null) {
                    Text(trailing, style = MaterialTheme.typography.titleMedium, color = SantotoNavy)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = text, style = MaterialTheme.typography.bodyMedium, color = SantotoTextBody)
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = onAction, modifier = Modifier.align(Alignment.End)) {
                Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = null, tint = SantotoNavy, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(actionLabel, style = MaterialTheme.typography.labelLarge, color = SantotoNavy)
            }
        }
    }
}

@Composable
private fun EmptySearch(query: String, onClear: () -> Unit, onAskBot: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.School, contentDescription = null, tint = SantotoNavy.copy(alpha = 0.35f), modifier = Modifier.size(48.dp))
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "No encontramos programas para \"$query\"",
            style = MaterialTheme.typography.titleSmall,
            color = SantotoNavy,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Prueba con otra palabra o pregúntale a Tommy Bot, que conoce toda la oferta.",
            style = MaterialTheme.typography.bodyMedium,
            color = SantotoTextSecondary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onAskBot,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SantotoNavy, contentColor = Color.White),
            modifier = Modifier.heightIn(min = 48.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = null, tint = SantotoYellow, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Preguntar a Tommy Bot", style = MaterialTheme.typography.labelLarge)
        }
        TextButton(onClick = onClear) { Text("Limpiar búsqueda", color = SantotoNavy) }
    }
}

@Composable
private fun DetailSection(title: String, body: String) {
    Spacer(modifier = Modifier.height(16.dp))
    Text(title.uppercase(), style = MaterialTheme.typography.labelSmall, color = SantotoNavy)
    Spacer(modifier = Modifier.height(4.dp))
    Text(body, style = MaterialTheme.typography.bodyMedium, color = SantotoTextBody)
}

@Composable
private fun ProgramDetailDialog(
    program: AcademicProgram,
    onDismiss: () -> Unit,
    onAskBot: () -> Unit,
    onScheduleVisit: () -> Unit
) {
    val context = LocalContext.current
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            LazyColumn(contentPadding = PaddingValues(20.dp)) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(program.faculty.label().uppercase(), style = MaterialTheme.typography.labelSmall, color = SantotoTextSecondary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(program.name, style = MaterialTheme.typography.titleLarge, color = SantotoNavy)
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = SantotoTextBody)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        InfoChip(program.modalidad)
                        InfoChip(program.semestres)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Surface(shape = RoundedCornerShape(14.dp), color = SantotoBg, modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Inversión semestral 2026", style = MaterialTheme.typography.labelMedium, color = SantotoTextSecondary)
                                Text(program.inversion2026, style = MaterialTheme.typography.titleMedium, color = SantotoNavy)
                            }
                            if (program.inversionNeos2027.isNotBlank()) {
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("Neos 2027", style = MaterialTheme.typography.labelMedium, color = SantotoTextSecondary)
                                    Text(program.inversionNeos2027, style = MaterialTheme.typography.titleMedium, color = SantotoNavy)
                                }
                            }
                        }
                    }

                    DetailSection("Qué te hace diferente", program.diferenciador)
                    if (program.empleabilidad.isNotBlank()) DetailSection("Campo laboral", program.empleabilidad)
                    if (program.dobleTitulacion.isNotBlank()) DetailSection("Doble titulación", program.dobleTitulacion)

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Título: ${program.titulo} · SNIES ${program.snies}",
                        style = MaterialTheme.typography.bodySmall,
                        color = SantotoTextSecondary
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = onAskBot,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SantotoNavy, contentColor = Color.White),
                        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = null, tint = SantotoYellow, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Hablar con el asesor IA", style = MaterialTheme.typography.labelLarge)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = onScheduleVisit,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, SantotoNavy),
                            modifier = Modifier.weight(1f).heightIn(min = 48.dp)
                        ) {
                            Text("Agendar visita", style = MaterialTheme.typography.labelLarge, color = SantotoNavy)
                        }
                        Button(
                            onClick = {
                                openWhatsApp(
                                    context,
                                    "Hola, quiero información sobre ${program.name} en la Santoto Tunja (costos, becas y fechas de inscripción)."
                                )
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SantotoWhatsApp, contentColor = Color.White),
                            modifier = Modifier.weight(1f).heightIn(min = 48.dp)
                        ) {
                            Text("WhatsApp", style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }
        }
    }
}
