package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.data.model.CampusEvent
import com.example.data.model.EventRegistration
import com.example.ui.components.QrCode
import com.example.ui.theme.SantotoBg
import com.example.ui.theme.SantotoNavy
import com.example.ui.theme.SantotoOutline
import com.example.ui.theme.SantotoOutlineStrong
import com.example.ui.theme.SantotoSuccess
import com.example.ui.theme.SantotoTextBody
import com.example.ui.theme.SantotoTextPrimary
import com.example.ui.theme.SantotoTextSecondary
import com.example.ui.theme.SantotoYellow
import com.example.viewmodel.SantotoViewModel
import com.example.viewmodel.Screen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EventTicketScreen(
    registration: EventRegistration,
    event: CampusEvent,
    viewModel: SantotoViewModel,
    modifier: Modifier = Modifier
) {
    // Tras inscribirse no se vuelve al formulario: el ViewModel regresa a la lista de eventos.
    BackHandler { viewModel.navigateBack() }

    val context = LocalContext.current
    val formattedRegDate = remember(registration.registeredAt) {
        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(registration.registeredAt))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SantotoNavy)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = SantotoSuccess.copy(alpha = 0.2f),
            border = androidx.compose.foundation.BorderStroke(1.dp, SantotoSuccess)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("¡Inscripción confirmada!", style = MaterialTheme.typography.labelLarge, color = Color.White)
            }
        }

        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier.fillMaxWidth().testTag("digital_ticket_card")
        ) {
            Column {
                Box(modifier = Modifier.fillMaxWidth().height(130.dp)) {
                    AsyncImage(
                        model = event.imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)))
                    Column(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(shape = RoundedCornerShape(6.dp), color = SantotoYellow) {
                            Text(
                                text = "USTA TUNJA",
                                style = MaterialTheme.typography.labelSmall,
                                color = SantotoNavy,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                        Text(event.title, style = MaterialTheme.typography.titleSmall, color = Color.White, maxLines = 2)
                    }
                }

                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    TicketField("Fecha", event.dateBadge, Icons.Default.CalendarToday)
                    TicketField("Horario", event.time, Icons.Default.AccessTime)
                    TicketField("Lugar", event.location, Icons.Default.LocationOn)

                    HorizontalDivider(color = SantotoOutline)

                    Text("TITULAR", style = MaterialTheme.typography.labelSmall, color = SantotoTextSecondary)
                    Column {
                        Text(registration.fullName, style = MaterialTheme.typography.titleMedium, color = SantotoNavy)
                        Text(
                            text = listOf(registration.phone, registration.schoolOrOrigin).filter { it.isNotBlank() }.joinToString(" · "),
                            style = MaterialTheme.typography.bodySmall,
                            color = SantotoTextSecondary
                        )
                    }
                }

                Canvas(modifier = Modifier.fillMaxWidth().height(16.dp)) {
                    drawLine(
                        color = SantotoOutlineStrong,
                        start = Offset(24.dp.toPx(), size.height / 2),
                        end = Offset(size.width - 24.dp.toPx(), size.height / 2),
                        strokeWidth = 2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 10f), 0f)
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth().background(SantotoBg).padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Fondo blanco y margen alrededor: el lector necesita la "zona de silencio"
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, SantotoOutline)
                    ) {
                        QrCode(
                            content = registration.ticketCode,
                            modifier = Modifier.padding(14.dp).size(168.dp).testTag("ticket_qr")
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(shape = RoundedCornerShape(8.dp), color = SantotoNavy) {
                        Text(
                            text = registration.ticketCode,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.ExtraBold,
                            style = MaterialTheme.typography.titleSmall,
                            color = SantotoYellow,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Presenta este código al ingresar al campus.",
                        style = MaterialTheme.typography.bodySmall,
                        color = SantotoTextBody,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Emitido: $formattedRegDate",
                        style = MaterialTheme.typography.bodySmall,
                        color = SantotoTextSecondary
                    )
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
                onClick = {
                    val shareText = """
                        🎟️ *BOLETO DIGITAL OFICIAL - SANTOTO TUNJA*
                        Evento: ${event.title}
                        Fecha: ${event.dateBadge} (${event.time})
                        Lugar: ${event.location}
                        Titular: ${registration.fullName}
                        Código de acceso: ${registration.ticketCode}

                        Presenta este boleto en el acceso principal de la Universidad Santo Tomás Tunja.
                    """.trimIndent()
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, shareText)
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(sendIntent, "Compartir boleto"))
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SantotoYellow, contentColor = SantotoNavy),
                modifier = Modifier.weight(1f).heightIn(min = 48.dp)
            ) {
                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Compartir", style = MaterialTheme.typography.labelLarge)
            }

            OutlinedButton(
                onClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("Código de boleto", registration.ticketCode))
                    Toast.makeText(context, "Código ${registration.ticketCode} copiado", Toast.LENGTH_SHORT).show()
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)),
                modifier = Modifier.weight(1f).heightIn(min = 48.dp)
            ) {
                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Copiar código", style = MaterialTheme.typography.labelLarge)
            }
        }

        TextButton(onClick = { viewModel.navigateBack() }, modifier = Modifier.heightIn(min = 48.dp)) {
            Text("Ver más eventos", style = MaterialTheme.typography.labelLarge, color = Color.White)
        }
        TextButton(onClick = { viewModel.navigateTo(Screen.Home) }, modifier = Modifier.heightIn(min = 48.dp)) {
            Text("Ir al inicio", style = MaterialTheme.typography.labelLarge, color = SantotoYellow)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun TicketField(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(icon, contentDescription = null, tint = SantotoNavy, modifier = Modifier.size(18.dp).padding(top = 2.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(label.uppercase(), style = MaterialTheme.typography.labelSmall, color = SantotoTextSecondary)
            Text(value, style = MaterialTheme.typography.bodyMedium, color = SantotoTextPrimary)
        }
    }
}
