package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Launch
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.model.ProgramLevel
import com.example.ui.components.SantotoActionCard
import com.example.ui.theme.SantotoBg
import com.example.ui.theme.SantotoGold
import com.example.ui.theme.SantotoNavy
import com.example.ui.theme.SantotoNavyDark
import com.example.ui.theme.SantotoSuccess
import com.example.ui.theme.SantotoYellow
import com.example.viewmodel.SantotoViewModel
import com.example.viewmodel.Screen

@Composable
fun HomeScreen(
    viewModel: SantotoViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val events by viewModel.events.collectAsStateWithLifecycle()
    val newsList by viewModel.news.collectAsStateWithLifecycle()

    var showLeadCaptureDialog by remember { mutableStateOf(false) }
    var leadName by remember { mutableStateOf("") }
    var leadPhone by remember { mutableStateOf("") }
    var leadEmail by remember { mutableStateOf("") }
    var leadSchool by remember { mutableStateOf("") }
    var leadProgram by remember { mutableStateOf("Ingeniería de Sistemas") }
    var leadSuccessMsg by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SantotoBg),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Official Clean Hero Card with Exact Santoto Crest
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("official_logo_banner_card"),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = SantotoNavy),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Exact Official Santoto Emblem
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(2.dp, SantotoYellow, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_santoto_logo),
                                contentDescription = "Logo Oficial Universidad Santo Tomás",
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "UNIVERSIDAD SANTO TOMÁS",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "Seccional Tunja • Admisiones",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SantotoYellow
                            )
                            Text(
                                text = "Vigilada MinEducación • SNIES 1705",
                                fontSize = 9.5.sp,
                                color = Color.White.copy(alpha = 0.75f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Construye tu futuro profesional",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "22 pregrados de alta calidad, posgrados y microcredenciales para grado 11°.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.85f),
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Clean Action Buttons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { viewModel.navigateTo(Screen.Chatbot) },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SantotoYellow,
                                contentColor = SantotoNavy
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("hero_ask_bot_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Asesor IA", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        Button(
                            onClick = { showLeadCaptureDialog = !showLeadCaptureDialog },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White.copy(alpha = 0.15f),
                                contentColor = Color.White
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = null,
                                tint = SantotoYellow,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Pedir Info", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // 2. Official SAC Inscriptions Card - Clean white card with yellow button (No orange!)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val url = "https://sac.usantotomas.edu.co/sgacampus/services/inscripciones/home"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    }
                    .testTag("official_inscriptions_button"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SantotoNavy.copy(alpha = 0.08f),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Launch,
                                    contentDescription = null,
                                    tint = SantotoNavy,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Inscripciones en Línea",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = SantotoNavy
                            )
                            Text(
                                text = "Portal SAC Oficial de Admisiones",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = SantotoYellow
                    ) {
                        Text(
                            text = "Inscribirme",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SantotoNavy,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                        )
                    }
                }
            }
        }

        // Quick Lead Form (if expanded)
        if (showLeadCaptureDialog) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Déjanos tus datos de contacto",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = SantotoNavy
                        )
                        Text(
                            text = "Un asesor oficial de admisiones te orientará de inmediato",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF64748B)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        if (leadSuccessMsg) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = SantotoSuccess.copy(alpha = 0.12f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "¡Registrado con éxito! Te contactaremos vía WhatsApp.",
                                    color = SantotoSuccess,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        } else {
                            val formFieldColors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color(0xFF0F172A),
                                unfocusedTextColor = Color(0xFF0F172A),
                                focusedLabelColor = SantotoNavy,
                                unfocusedLabelColor = Color(0xFF475569),
                                focusedBorderColor = SantotoNavy,
                                unfocusedBorderColor = Color(0xFF94A3B8),
                                cursorColor = SantotoNavy,
                                focusedContainerColor = Color(0xFFF8FAFC),
                                unfocusedContainerColor = Color(0xFFFFFFFF)
                            )

                            OutlinedTextField(
                                value = leadName,
                                onValueChange = { leadName = it },
                                label = { Text("Nombre completo *", fontWeight = FontWeight.Medium) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth().testTag("lead_input_name"),
                                colors = formFieldColors
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = leadPhone,
                                onValueChange = { leadPhone = it },
                                label = { Text("Celular / WhatsApp *", fontWeight = FontWeight.Medium) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth().testTag("lead_input_phone"),
                                colors = formFieldColors
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = leadEmail,
                                onValueChange = { leadEmail = it },
                                label = { Text("Correo electrónico", fontWeight = FontWeight.Medium) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors = formFieldColors
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = leadSchool,
                                onValueChange = { leadSchool = it },
                                label = { Text("Colegio o institución", fontWeight = FontWeight.Medium) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors = formFieldColors
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = leadProgram,
                                onValueChange = { leadProgram = it },
                                label = { Text("Programa de tu interés", fontWeight = FontWeight.Medium) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors = formFieldColors
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Button(
                                onClick = {
                                    if (leadName.isNotBlank() && leadPhone.isNotBlank()) {
                                        viewModel.captureLead(
                                            fullName = leadName,
                                            phone = leadPhone,
                                            email = leadEmail,
                                            cityOrSchool = leadSchool,
                                            programInterest = leadProgram,
                                            notes = "Lead capturado desde Home Screen"
                                        )
                                        leadSuccessMsg = true
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = SantotoNavy,
                                    contentColor = Color.White
                                ),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("lead_submit_button")
                            ) {
                                Text(
                                    text = "Enviar Datos",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section Header
        item {
            Text(
                text = "SERVICIOS PRINCIPALES",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = SantotoNavy,
                letterSpacing = 0.5.sp
            )
        }

        // 3. Action Cards (Light, Clean, White, with Yellow Badges)
        item {
            SantotoActionCard(
                title = "Tommy Bot • Asesor Virtual",
                subtitle = "Orientación oficial sobre carreras, costos, homologaciones y becas USTA Tunja",
                icon = Icons.AutoMirrored.Filled.Chat,
                badgeText = null,
                onClick = { viewModel.navigateTo(Screen.Chatbot) }
            )
        }

        item {
            SantotoActionCard(
                title = "Explora 360°",
                subtitle = "Recorrido virtual interactivo 360.santototunja.edu.co",
                icon = Icons.Default.ViewInAr,
                badgeText = "360°",
                badgeColor = SantotoYellow,
                onClick = { viewModel.navigateTo(Screen.Explora360) }
            )
        }

        item {
            SantotoActionCard(
                title = "Agendar Visita al Campus",
                subtitle = "Elige día y hora en el calendario y obtén tu pase digital QR",
                icon = Icons.Default.CalendarMonth,
                badgeText = "CALENDARIO",
                badgeColor = SantotoYellow,
                onClick = { viewModel.navigateTo(Screen.ScheduleVisit) }
            )
        }

        item {
            SantotoActionCard(
                title = "Test de Orientación Vocacional",
                subtitle = "Descubre tus áreas de afinidad y carreras recomendadas",
                icon = Icons.Default.Psychology,
                onClick = { viewModel.navigateTo(Screen.VocationalTest) }
            )
        }

        item {
            SantotoActionCard(
                title = "Oferta Académica 2026 - 2027",
                subtitle = "Pregrados, posgrados y acreditaciones de alta calidad",
                icon = Icons.Default.School,
                onClick = { viewModel.navigateTo(Screen.ProgramsCatalog) }
            )
        }

        item {
            SantotoActionCard(
                title = "Microcredenciales Grado 11°",
                subtitle = "Adelanta materias universitarias por solo $100.000 homologables",
                icon = Icons.Default.Engineering,
                badgeText = "$100.000",
                badgeColor = SantotoYellow,
                onClick = {
                    viewModel.setLevelFilter(ProgramLevel.MICROCREDENCIAL)
                    viewModel.navigateTo(Screen.ProgramsCatalog)
                }
            )
        }

        item {
            SantotoActionCard(
                title = "Noticias & Actualidad USTA",
                subtitle = "Convocatorias de becas, convenios, vida universitaria e innovación",
                icon = Icons.Default.Newspaper,
                badgeText = "BOLETÍN",
                badgeColor = SantotoYellow,
                onClick = { viewModel.navigateTo(Screen.News) }
            )
        }

        // 4. Próximos Eventos & Open Days
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PRÓXIMOS EVENTOS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SantotoNavy,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "Ver todos",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SantotoNavy,
                    modifier = Modifier.clickable { viewModel.navigateTo(Screen.Events) }
                )
            }
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(events.take(5), key = { it.id }) { event ->
                    Card(
                        modifier = Modifier
                            .width(260.dp)
                            .clickable { viewModel.navigateTo(Screen.EventConfirmRegistration(event)) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column {
                            // Event Image
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(115.dp)
                            ) {
                                AsyncImage(
                                    model = event.imageUrl,
                                    contentDescription = event.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black.copy(alpha = 0.3f))
                                )
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = SantotoYellow,
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = event.dateBadge,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SantotoNavy,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = SantotoNavy.copy(alpha = 0.85f),
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = "${event.availableSpots} cupos",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SantotoGold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = event.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = SantotoNavyDark,
                                    maxLines = 2,
                                    minLines = 2
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = event.location,
                                    fontSize = 10.sp,
                                    color = Color(0xFF64748B),
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = SantotoNavy,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Inscribirme",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 5.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. NOTICIAS & ACTUALIDAD TOMASINA
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "NOTICIAS & ACTUALIDAD",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SantotoNavy,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "Ver todas",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SantotoNavy,
                    modifier = Modifier.clickable { viewModel.navigateTo(Screen.News) }
                )
            }
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(newsList.take(4), key = { it.id }) { newsItem ->
                    Card(
                        modifier = Modifier
                            .width(260.dp)
                            .clickable { viewModel.navigateTo(Screen.News) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(115.dp)
                            ) {
                                AsyncImage(
                                    model = newsItem.imageUrl,
                                    contentDescription = newsItem.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = SantotoNavy.copy(alpha = 0.85f),
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = newsItem.category,
                                        fontSize = 8.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SantotoGold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = newsItem.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = SantotoNavyDark,
                                    maxLines = 2
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = newsItem.dateText,
                                    fontSize = 10.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }

        // 5. Professional Institutional Footer (No emojis, official contact info & portal)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SantotoNavy,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.School,
                                    contentDescription = null,
                                    tint = SantotoYellow,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Universidad Santo Tomás",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = SantotoNavy
                            )
                            Text(
                                text = "Seccional Tunja • Código SNIES: 1708",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    androidx.compose.material3.HorizontalDivider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(12.dp))

                    // Contact phone & WhatsApp
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            tint = SantotoNavy,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Líneas de Atención: (608) 744 0404 • WhatsApp: 317 510 5383 / 317 514 1479",
                            fontSize = 11.sp,
                            color = Color(0xFF334155),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Email
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = SantotoNavy,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "admisiones@ustatunja.edu.co",
                            fontSize = 11.sp,
                            color = Color(0xFF334155),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Web portal
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = null,
                            tint = SantotoNavy,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Portal Oficial: santototunja.edu.co / admisiones",
                            fontSize = 11.sp,
                            color = SantotoNavy,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Locations
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = SantotoNavy,
                            modifier = Modifier.size(16.dp).padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Sedes: Campus Centro (Cra. 19 # 11-64) | Campus Av. Universitaria (Cll. 19 # 22-64), Tunja, Boyacá",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B),
                            lineHeight = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFF8FAFC)
                    ) {
                        Text(
                            text = "Institución de Educación Superior sujeta a inspección y vigilancia por el Ministerio de Educación Nacional.",
                            fontSize = 9.sp,
                            color = Color(0xFF94A3B8),
                            modifier = Modifier.padding(8.dp),
                            lineHeight = 13.sp
                        )
                    }
                }
            }
        }
    }
}

