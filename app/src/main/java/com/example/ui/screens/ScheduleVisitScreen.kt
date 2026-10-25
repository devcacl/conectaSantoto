package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.QrCode
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.InteractiveCalendarView
import com.example.ui.theme.SantotoBg
import com.example.ui.theme.SantotoCyan
import com.example.ui.theme.SantotoGold
import com.example.ui.theme.SantotoNavy
import com.example.util.DateUtils
import com.example.ui.theme.SantotoNavyDark
import com.example.ui.theme.SantotoSuccess
import com.example.viewmodel.SantotoViewModel

@Composable
fun ScheduleVisitScreen(
    viewModel: SantotoViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    var studentName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var school by remember { mutableStateOf("") }
    var selectedCampus by remember { mutableStateOf("Campus Av. Universitaria") }
    val todayText = remember { DateUtils.dayText(0) }
    val availableDays = remember { DateUtils.upcomingBusinessDays(8) }
    var selectedDateText by remember { mutableStateOf(DateUtils.nextBusinessDay()) }
    var selectedTimeSlot by remember { mutableStateOf("09:00 AM - 11:00 AM") }
    var programInterest by remember { mutableStateOf("Ingeniería de Sistemas") }
    var visitorCount by remember { mutableIntStateOf(2) }

    var isBooked by remember { mutableStateOf(false) }

    val timeSlots = listOf(
        "09:00 AM - 11:00 AM",
        "11:00 AM - 01:00 PM",
        "02:00 PM - 04:00 PM"
    )

    val campusOptions = listOf(
        "Campus Av. Universitaria" to "Norte: Edificio Santo Domingo, Laboratorios & Complejo Deportivo",
        "Campus Centro Histórico" to "Centro: Convento San Alberto Magno, Derecho & Negocios"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SantotoBg),
        contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 10.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        if (!isBooked) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SantotoNavy)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = SantotoGold,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "CALENDARIO DE VISITAS AL CAMPUS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SantotoGold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Selecciona el día y hora para tu visita",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Recorre nuestros campus guiado por directores y vive la experiencia universitaria.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFD0E1F0)
                        )
                    }
                }
            }

            // 1. Interactive Month Calendar Component (Requested by user)
            item {
                Text(
                    text = "1. SELECCIONA EL DÍA EN EL CALENDARIO",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SantotoNavy
                )
                Spacer(modifier = Modifier.height(6.dp))

                InteractiveCalendarView(
                    selectedDateText = selectedDateText,
                    onDateSelected = { date -> selectedDateText = date },
                    highlightedDates = availableDays,
                    minDateText = todayText
                )

                Spacer(modifier = Modifier.height(6.dp))

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SantotoNavy.copy(alpha = 0.08f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SantotoNavy, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Fecha seleccionada: $selectedDateText",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SantotoNavy
                        )
                    }
                }
            }

            // 2. Time Slot Selection
            item {
                Text(
                    text = "2. SELECCIONA LA HORA DISPONIBLE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SantotoNavy
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    timeSlots.forEach { slot ->
                        val isSelected = selectedTimeSlot == slot
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) SantotoNavy else Color.White,
                            modifier = Modifier
                                .weight(1f)
                                .border(
                                    1.dp,
                                    if (isSelected) SantotoGold else Color.LightGray,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { selectedTimeSlot = slot }
                        ) {
                            Text(
                                text = slot,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) SantotoGold else SantotoNavy,
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }

            // 3. Campus Selection
            item {
                Text(
                    text = "3. SEDE O CAMPUS A VISITAR",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SantotoNavy
                )
                Spacer(modifier = Modifier.height(6.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    campusOptions.forEach { (campus, desc) ->
                        val isSelected = selectedCampus == campus
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) SantotoNavy else Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, if (isSelected) SantotoGold else Color.LightGray, RoundedCornerShape(14.dp))
                                .clickable { selectedCampus = campus }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = if (isSelected) SantotoGold else SantotoNavy,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = campus,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = if (isSelected) Color.White else SantotoNavyDark
                                    )
                                    Text(
                                        text = desc,
                                        fontSize = 10.sp,
                                        color = if (isSelected) Color(0xFFCADBE8) else Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 4. Student Form
            item {
                Text(
                    text = "4. DATOS DE CONTACTO",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SantotoNavy
                )
                Spacer(modifier = Modifier.height(6.dp))
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    val visitFieldColors = OutlinedTextFieldDefaults.colors(
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

                    Column(modifier = Modifier.padding(16.dp)) {
                        OutlinedTextField(
                            value = studentName,
                            onValueChange = { studentName = it },
                            label = { Text("Nombre y apellidos *", fontWeight = FontWeight.Medium) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("visit_name_input"),
                            colors = visitFieldColors
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Celular / WhatsApp *", fontWeight = FontWeight.Medium) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("visit_phone_input"),
                            colors = visitFieldColors
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Correo electrónico", fontWeight = FontWeight.Medium) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = visitFieldColors
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = school,
                            onValueChange = { school = it },
                            label = { Text("Colegio o institución de origen", fontWeight = FontWeight.Medium) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = visitFieldColors
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = programInterest,
                            onValueChange = { programInterest = it },
                            label = { Text("Programa de mayor interés *", fontWeight = FontWeight.Medium) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = visitFieldColors
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Acompañantes:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            Row {
                                listOf(1, 2, 3, 4, 5).forEach { count ->
                                    Surface(
                                        shape = CircleShape,
                                        color = if (visitorCount == count) SantotoNavy else Color(0xFFE8EEF5),
                                        modifier = Modifier
                                            .padding(horizontal = 2.dp)
                                            .size(30.dp)
                                            .clickable { visitorCount = count }
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "$count",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 10.sp,
                                                color = if (visitorCount == count) SantotoGold else SantotoNavyDark
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Submit Button
            item {
                Button(
                    onClick = {
                        if (studentName.isNotBlank() && phone.isNotBlank()) {
                            viewModel.scheduleCampusVisit(
                                studentName = studentName,
                                phone = phone,
                                email = email,
                                campus = selectedCampus,
                                dateText = selectedDateText,
                                timeSlot = selectedTimeSlot,
                                programInterest = programInterest,
                                visitorCount = visitorCount,
                                school = school
                            )
                            isBooked = true
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
                        .height(50.dp)
                        .testTag("submit_schedule_visit_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = SantotoGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Confirmar Visita & Generar Pase",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            // Digital Ticket Confirmation Pass
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SantotoNavy)
                                .padding(16.dp)
                        ) {
                            Column {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = SantotoGold
                                ) {
                                    Text(
                                        text = "PASE OFICIAL DE VISITA",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = SantotoNavyDark,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = studentName.ifBlank { "Aspirante Tomasino" },
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Carrera: $programInterest",
                                    fontSize = 12.sp,
                                    color = SantotoCyan
                                )
                            }
                        }

                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("SEDE / CAMPUS", fontSize = 9.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                    Text(selectedCampus, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SantotoNavy)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("ACOMPAÑANTES", fontSize = 9.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                    Text("$visitorCount Personas", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SantotoNavy)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("FECHA", fontSize = 9.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                    Text(selectedDateText, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SantotoNavy)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("HORARIO", fontSize = 9.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                                    Text(selectedTimeSlot, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SantotoNavy)
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(SantotoBg, RoundedCornerShape(12.dp))
                                    .padding(14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.QrCode,
                                        contentDescription = "QR Code",
                                        tint = SantotoNavy,
                                        modifier = Modifier.size(80.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Presenta este pase al ingresar",
                                        fontSize = 11.sp,
                                        color = Color.DarkGray
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = { isBooked = false },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SantotoNavy),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Agendar otra visita", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
