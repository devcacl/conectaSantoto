package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.FacultyCategory
import com.example.data.rag.VocationalTestData
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
fun VocationalTestScreen(
    viewModel: SantotoViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    val currentQuestionIndex by viewModel.testCurrentQuestion.collectAsStateWithLifecycle()
    val testResult by viewModel.testResult.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SantotoBg)
    ) {
        if (testResult == null) {
            // QUIZ IN PROGRESS
            val question = VocationalTestData.QUESTIONS.getOrElse(currentQuestionIndex) { VocationalTestData.QUESTIONS[0] }
            val progress = (currentQuestionIndex + 1).toFloat() / VocationalTestData.QUESTIONS.size.toFloat()

            // Header progress
            Surface(
                color = SantotoNavy,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "PREGUNTA ${currentQuestionIndex + 1} DE ${VocationalTestData.QUESTIONS.size}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SantotoGold
                        )
                        Text(
                            text = question.categoryDescription,
                            fontSize = 11.sp,
                            color = SantotoCyan
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = SantotoGold,
                        trackColor = SantotoNavyDark
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Text(
                        text = question.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = SantotoNavy,
                        lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Selecciona la opción con la que más te identifiques:",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                items(question.options) { option ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.answerVocationalQuestion(option.facultyWeight) }
                            .testTag("option_${option.facultyWeight.name}"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = SantotoNavy.copy(alpha = 0.08f),
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = facultyIcon(option.facultyWeight),
                                        contentDescription = null,
                                        tint = SantotoNavy,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Text(
                                text = option.text,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = SantotoNavyDark,
                                lineHeight = 20.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        } else {
            // TEST RESULT DISPLAY
            val result = testResult!!
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = SantotoNavy)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = SantotoGold,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "¡TEST VOCACIONAL COMPLETADO!",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = SantotoGold
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Tu mayor afinidad vocacional es:",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFD3E4F4)
                            )

                            Text(
                                text = when (result.topFaculty) {
                                    FacultyCategory.INGENIERIAS_Y_ARQUITECTURA -> "Ingenierías, Datos & Arquitectura"
                                    FacultyCategory.CIENCIAS_ECONOMICAS_Y_ADMINISTRATIVAS -> "Ciencias Económicas, Negocios & Marketing"
                                    FacultyCategory.CIENCIAS_JURIDICAS_Y_POLITICAS -> "Derecho & Ciencias Políticas"
                                    FacultyCategory.CIENCIAS_DE_LA_SALUD_Y_DEPORTE -> "Salud, Deporte & Nutrición"
                                    FacultyCategory.EDUCACION_Y_HUMANIDADES -> "Educación Bilingüe & Humanidades"
                                },
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = result.personalityProfile,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFFE5EEF7),
                                lineHeight = 20.sp
                            )
                        }
                    }
                }

                item {
                    AffinityChart(result.facultyScores)
                }

                item {
                    Text(
                        text = "PROGRAMAS RECOMENDADOS EN SANTOTO TUNJA",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SantotoNavy,
                        letterSpacing = 1.sp
                    )
                }

                items(result.recommendedPrograms) { programName ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.sendChatMessage("Cuéntame todo sobre la carrera $programName")
                                viewModel.navigateTo(Screen.Chatbot)
                            },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = SantotoSuccess,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = programName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = SantotoNavyDark
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = SantotoGold
                            ) {
                                Text(
                                    text = "Consultar",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SantotoNavyDark,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                item {
                    ResultLeadCard(
                        topProgram = result.recommendedPrograms.firstOrNull() ?: "Programas Santoto Tunja",
                        facultyLabel = facultyLabel(result.topFaculty),
                        onSubmit = { name, phone, program, notes ->
                            viewModel.captureLead(
                                fullName = name,
                                phone = phone,
                                email = "",
                                cityOrSchool = "",
                                programInterest = program,
                                notes = notes
                            )
                        }
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { viewModel.navigateTo(Screen.ScheduleVisit) },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SantotoNavy),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("result_schedule_visit")
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = SantotoGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Agendar Visita", fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { viewModel.resetVocationalTest() },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SantotoCardNavy),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                tint = SantotoCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Repetir Test", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
