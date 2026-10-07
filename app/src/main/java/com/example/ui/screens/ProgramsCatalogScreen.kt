package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.AcademicProgram
import com.example.data.model.ProgramLevel
import com.example.data.rag.SantotoKnowledgeBase
import com.example.ui.theme.SantotoBg
import com.example.ui.theme.SantotoCardNavy
import com.example.ui.theme.SantotoCyan
import com.example.ui.theme.SantotoGold
import com.example.ui.theme.SantotoNavy
import com.example.ui.theme.SantotoNavyDark
import com.example.ui.theme.SantotoRoyal
import com.example.ui.theme.SantotoSuccess
import com.example.viewmodel.SantotoViewModel
import com.example.viewmodel.Screen

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
        // Search Bar & Filter Header
        Surface(
            color = SantotoNavy,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = { Text("Buscar programa (ej. Sistemas, Derecho, Datos...)", fontSize = 12.sp, color = Color.LightGray) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = SantotoGold
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(Icons.Default.Close, contentDescription = "Limpiar", tint = Color.White)
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = SantotoGold,
                        unfocusedBorderColor = Color(0xFF2C4A6F),
                        focusedContainerColor = SantotoCardNavy,
                        unfocusedContainerColor = SantotoCardNavy
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("program_search_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Filter Chips
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            label = "Todos",
                            isSelected = selectedLevelFilter == null,
                            onClick = { viewModel.setLevelFilter(null) }
                        )
                    }
                    item {
                        FilterChip(
                            label = "Pregrados (22)",
                            isSelected = selectedLevelFilter == ProgramLevel.PREGRADO,
                            onClick = { viewModel.setLevelFilter(ProgramLevel.PREGRADO) }
                        )
                    }
                    item {
                        FilterChip(
                            label = "Microcredenciales 11° ($100k)",
                            isSelected = selectedLevelFilter == ProgramLevel.MICROCREDENCIAL,
                            onClick = { viewModel.setLevelFilter(ProgramLevel.MICROCREDENCIAL) }
                        )
                    }
                    item {
                        FilterChip(
                            label = "Posgrados",
                            isSelected = selectedLevelFilter == ProgramLevel.POSGRADO,
                            onClick = { viewModel.setLevelFilter(ProgramLevel.POSGRADO) }
                        )
                    }
                }
            }
        }

        // Program Items List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (selectedLevelFilter == ProgramLevel.MICROCREDENCIAL) {
                // Microcredenciales Grado 11 view
                items(SantotoKnowledgeBase.MICROCREDENCIALES_GRADO_11) { microText ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = SantotoGold
                                ) {
                                    Text(
                                        text = "MICROCREDENCIAL GRADO 11°",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = SantotoNavyDark,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Text(
                                    text = "$100.000",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 14.sp,
                                    color = SantotoSuccess
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = microText,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = SantotoNavyDark
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = SantotoNavy,
                                    modifier = Modifier.clickable {
                                        viewModel.sendChatMessage("Explícame la microcredencial: $microText")
                                        viewModel.navigateTo(Screen.Chatbot)
                                    }
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = null, tint = SantotoGold, modifier = Modifier.size(12.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Consultar Requisitos", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (selectedLevelFilter == ProgramLevel.POSGRADO) {
                // Posgrados view
                items(SantotoKnowledgeBase.POSGRADOS_RESUMEN) { posgradoText ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.School, contentDescription = null, tint = SantotoNavy, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = posgradoText,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = SantotoNavyDark
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = SantotoNavy,
                                    modifier = Modifier.clickable {
                                        viewModel.sendChatMessage("Detalles e inversión del posgrado: $posgradoText")
                                        viewModel.navigateTo(Screen.Chatbot)
                                    }
                                ) {
                                    Text(
                                        text = "Pedir Plan de Estudios",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SantotoGold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // Pregrados view
                items(filteredPrograms, key = { it.id }) { program ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedProgramForModal = program }
                            .testTag("program_card_${program.code}"),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
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
                                        text = "SNIES: ${program.snies}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = SantotoGold.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "${program.semestres} • ${program.modalidad}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF8A6200),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = program.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = SantotoNavyDark
                            )

                            Text(
                                text = "Título: " + program.titulo,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Inversión 2026:",
                                        fontSize = 10.sp,
                                        color = Color.DarkGray
                                    )
                                    Text(
                                        text = program.inversion2026,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = SantotoSuccess
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Ver detalle",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SantotoNavy
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
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
            }
        }
    }

    // Modal Program Details
    selectedProgramForModal?.let { prog ->
        androidx.compose.ui.window.Dialog(onDismissRequest = { selectedProgramForModal = null }) {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                LazyColumn(modifier = Modifier.padding(18.dp)) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = prog.name,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = SantotoNavy
                            )
                            IconButton(onClick = { selectedProgramForModal = null }) {
                                Icon(Icons.Default.Close, contentDescription = "Cerrar")
                            }
                        }

                        Text(
                            text = "Título: ${prog.titulo} | SNIES: ${prog.snies}",
                            fontSize = 11.sp,
                            color = SantotoGold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Inversión Highlight Box
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SantotoBg,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Inversión 2026:", fontSize = 10.sp, color = Color.Gray)
                                    Text(prog.inversion2026, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SantotoSuccess)
                                }
                                if (prog.inversionNeos2027.isNotBlank()) {
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text("Neos 2027:", fontSize = 10.sp, color = Color.Gray)
                                        Text(prog.inversionNeos2027, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SantotoNavy)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("DIFERENCIADOR TOMASINO", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SantotoNavy)
                        Text(prog.diferenciador, fontSize = 12.sp, color = Color.DarkGray, lineHeight = 18.sp)

                        if (prog.empleabilidad.isNotBlank()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("CAMPO LABORAL / EMPLEABILIDAD", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SantotoNavy)
                            Text(prog.empleabilidad, fontSize = 12.sp, color = Color.DarkGray, lineHeight = 18.sp)
                        }

                        if (prog.dobleTitulacion.isNotBlank()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("DOBLE TITULACIÓN", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SantotoNavy)
                            Text(prog.dobleTitulacion, fontSize = 12.sp, color = Color.DarkGray, lineHeight = 18.sp)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    val progName = prog.name
                                    selectedProgramForModal = null
                                    viewModel.sendChatMessage("Quiero todos los detalles para matricularme a $progName")
                                    viewModel.navigateTo(Screen.Chatbot)
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SantotoNavy),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = null, tint = SantotoGold, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Asesor IA", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }

                            Button(
                                onClick = {
                                    selectedProgramForModal = null
                                    viewModel.navigateTo(Screen.ScheduleVisit)
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SantotoGold),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Agendar Visita", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = SantotoNavyDark)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (isSelected) SantotoGold else SantotoCardNavy,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) SantotoNavyDark else Color.White,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}
