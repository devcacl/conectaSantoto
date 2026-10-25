package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SantotoBg
import com.example.ui.theme.SantotoCyan
import com.example.ui.theme.SantotoGold
import com.example.ui.theme.SantotoNavy
import com.example.ui.theme.SantotoNavyDark
import com.example.ui.theme.SantotoYellow

@Composable
fun InteractiveCalendarView(
    selectedDateText: String, // e.g. "2026-10-15"
    onDateSelected: (String) -> Unit,
    highlightedDates: Set<String> = emptySet(), // Dates that have events or visits
    modifier: Modifier = Modifier,
    minDateText: String? = null // Fechas anteriores (yyyy-MM-dd) quedan deshabilitadas
) {
    // Arranca en el mes de la fecha seleccionada (o el mes actual)
    val initial = remember {
        val parts = selectedDateText.split("-")
        val cal = java.util.Calendar.getInstance()
        val y = parts.getOrNull(0)?.toIntOrNull() ?: cal.get(java.util.Calendar.YEAR)
        val m = parts.getOrNull(1)?.toIntOrNull() ?: (cal.get(java.util.Calendar.MONTH) + 1)
        y to m
    }
    var currentYear by remember { mutableIntStateOf(initial.first) }
    var currentMonth by remember { mutableIntStateOf(initial.second) } // 1-12

    val monthNames = listOf(
        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    )

    val daysInMonth = when (currentMonth) {
        2 -> if (currentYear % 4 == 0) 29 else 28
        4, 6, 9, 11 -> 30
        else -> 31
    }

    // First day of week for October 2026 (Oct 1 2026 was Thursday -> index 3 if Mon=0)
    // Simplified calculation for calendar grid offset:
    val firstDayOffset = remember(currentYear, currentMonth) {
        val y = if (currentMonth <= 2) currentYear - 1 else currentYear
        val m = if (currentMonth <= 2) currentMonth + 12 else currentMonth
        val d = 1
        // Zeller-like day of week: 0=Mon, 1=Tue, ..., 6=Sun
        val c = y / 100
        val k = y % 100
        val w = (d + (13 * (m + 1)) / 5 + k + k / 4 + c / 4 + 5 * c) % 7
        // Convert to Monday=0, Sunday=6
        (w + 5) % 7
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Month & Year Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (currentMonth == 1) {
                            currentMonth = 12
                            currentYear -= 1
                        } else {
                            currentMonth -= 1
                        }
                    },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                        contentDescription = "Mes anterior",
                        tint = SantotoNavy,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Text(
                    text = "${monthNames[currentMonth - 1]} $currentYear",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = SantotoNavy
                )

                IconButton(
                    onClick = {
                        if (currentMonth == 12) {
                            currentMonth = 1
                            currentYear += 1
                        } else {
                            currentMonth += 1
                        }
                    },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "Mes siguiente",
                        tint = SantotoNavy,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Weekday labels
            val weekDays = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                weekDays.forEach { dayName ->
                    Text(
                        text = dayName,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(36.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Days Grid
            val totalSlots = firstDayOffset + daysInMonth
            val rows = (totalSlots + 6) / 7

            for (r in 0 until rows) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    for (c in 0 until 7) {
                        val index = r * 7 + c
                        val dayNumber = index - firstDayOffset + 1

                        if (dayNumber in 1..daysInMonth) {
                            val formattedDate = String.format("%04d-%02d-%02d", currentYear, currentMonth, dayNumber)
                            val isSelected = selectedDateText.startsWith(formattedDate)
                            val hasActivity = highlightedDates.any { it.startsWith(formattedDate) }
                            val isPast = minDateText != null && formattedDate < minDateText

                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isSelected -> SantotoNavy
                                            else -> Color.Transparent
                                        }
                                    )
                                    .clickable(enabled = !isPast) { onDateSelected(formattedDate) },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "$dayNumber",
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = when {
                                            isSelected -> SantotoYellow
                                            isPast -> Color(0xFFCBD5E1)
                                            else -> SantotoNavy
                                        }
                                    )
                                    if (hasActivity && !isSelected) {
                                        Box(
                                            modifier = Modifier
                                                .size(5.dp)
                                                .clip(CircleShape)
                                                .background(SantotoYellow)
                                        )
                                    } else if (hasActivity && isSelected) {
                                        Box(
                                            modifier = Modifier
                                                .size(5.dp)
                                                .clip(CircleShape)
                                                .background(SantotoYellow)
                                        )
                                    }
                                }
                            }
                        } else {
                            // Blank cell
                            Spacer(modifier = Modifier.size(38.dp))
                        }
                    }
                }
            }
        }
    }
}
