package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FacultyCategory
import com.example.ui.theme.SantotoGold
import com.example.ui.theme.SantotoNavy
import com.example.ui.theme.SantotoNavyDark
import com.example.ui.theme.SantotoSuccess

internal fun facultyIcon(faculty: FacultyCategory): ImageVector = when (faculty) {
    FacultyCategory.INGENIERIAS_Y_ARQUITECTURA -> Icons.Default.Engineering
    FacultyCategory.CIENCIAS_ECONOMICAS_Y_ADMINISTRATIVAS -> Icons.Default.BusinessCenter
    FacultyCategory.CIENCIAS_JURIDICAS_Y_POLITICAS -> Icons.Default.Gavel
    FacultyCategory.CIENCIAS_DE_LA_SALUD_Y_DEPORTE -> Icons.Default.FitnessCenter
    FacultyCategory.EDUCACION_Y_HUMANIDADES -> Icons.Default.MenuBook
}

internal fun facultyLabel(faculty: FacultyCategory): String = when (faculty) {
    FacultyCategory.INGENIERIAS_Y_ARQUITECTURA -> "Ingenierías, Datos & Arquitectura"
    FacultyCategory.CIENCIAS_ECONOMICAS_Y_ADMINISTRATIVAS -> "Ciencias Económicas, Negocios & Marketing"
    FacultyCategory.CIENCIAS_JURIDICAS_Y_POLITICAS -> "Derecho & Ciencias Políticas"
    FacultyCategory.CIENCIAS_DE_LA_SALUD_Y_DEPORTE -> "Salud, Deporte & Nutrición"
    FacultyCategory.EDUCACION_Y_HUMANIDADES -> "Educación Bilingüe & Humanidades"
}

internal const val ADMISSIONS_WHATSAPP = "573175105383"

internal fun openWhatsApp(context: android.content.Context, message: String, phone: String = ADMISSIONS_WHATSAPP) {
    val url = "https://wa.me/$phone?text=" + Uri.encode(message)
    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    } catch (_: Exception) {
        // Sin app compatible instalada
    }
}

@Composable
internal fun AffinityChart(scores: Map<FacultyCategory, Int>) {
    val total = scores.values.sum().coerceAtLeast(1)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "TU MAPA DE AFINIDAD",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = SantotoNavy,
                letterSpacing = 1.sp
            )
            scores.entries.sortedByDescending { it.value }.forEach { (faculty, score) ->
                val pct = score * 100 / total
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Icon(facultyIcon(faculty), null, tint = SantotoNavy, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(facultyLabel(faculty), fontSize = 12.sp, color = SantotoNavyDark, fontWeight = FontWeight.Medium)
                        }
                        Text("$pct%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SantotoNavy)
                    }
                    Spacer(Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { pct / 100f },
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                        color = SantotoGold,
                        trackColor = Color(0xFFE2E8F0)
                    )
                }
            }
        }
    }
}

@Composable
internal fun ResultLeadCard(
    topProgram: String,
    facultyLabel: String,
    onSubmit: (name: String, phone: String, program: String, notes: String) -> Unit
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var sent by remember { mutableStateOf(false) }
    val phoneOk = phone.filter { it.isDigit() }.length >= 10
    val canSend = name.isNotBlank() && phoneOk

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, SantotoGold)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Recibe asesoría gratuita sobre $topProgram",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = SantotoNavy
            )
            Text(
                text = "Un asesor de Admisiones te contactará con costos, becas y fechas de inscripción.",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF64748B)
            )
            Spacer(Modifier.height(10.dp))
            if (sent) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SantotoSuccess.copy(alpha = 0.12f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "¡Listo! Recibimos tus datos y te contactaremos pronto.",
                        color = SantotoSuccess,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            } else {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre completo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Celular / WhatsApp") },
                    singleLine = true,
                    isError = phone.isNotEmpty() && !phoneOk,
                    supportingText = { if (phone.isNotEmpty() && !phoneOk) Text("Ingresa un celular de 10 dígitos") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(10.dp))
                Button(
                    onClick = {
                        onSubmit(name.trim(), phone.trim(), topProgram, "Lead desde Test Vocacional. Afinidad: $facultyLabel")
                        sent = true
                    },
                    enabled = canSend,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SantotoNavy),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) { Text("Quiero que me contacten", fontWeight = FontWeight.Bold) }
            }
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    openWhatsApp(
                        context,
                        "Hola, hice el test vocacional en la app de Admisiones Santoto Tunja. Mi afinidad es $facultyLabel y me interesa $topProgram."
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366), contentColor = Color.White),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) { Text("Hablar por WhatsApp", fontWeight = FontWeight.Bold) }
        }
    }
}
