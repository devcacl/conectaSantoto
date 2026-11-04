package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.data.model.CampusEvent
import com.example.ui.components.EventDetailRow
import com.example.ui.components.SpotsIndicator
import com.example.ui.theme.SantotoBg
import com.example.ui.theme.SantotoNavy
import com.example.ui.theme.SantotoOutline
import com.example.ui.theme.SantotoOutlineStrong
import com.example.ui.theme.SantotoTextBody
import com.example.ui.theme.SantotoTextPrimary
import com.example.ui.theme.SantotoTextSecondary
import com.example.ui.theme.SantotoYellow
import com.example.util.Validation
import com.example.viewmodel.SantotoViewModel
import com.example.viewmodel.Screen

@Composable
fun EventRegistrationScreen(
    event: CampusEvent,
    viewModel: SantotoViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    val context = LocalContext.current
    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var school by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }
    var showErrors by remember { mutableStateOf(false) }

    val nameOk = Validation.isValidName(fullName)
    val phoneOk = Validation.isValidPhone(phone)
    val emailOk = Validation.isValidEmail(email)

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = SantotoTextPrimary,
        unfocusedTextColor = SantotoTextPrimary,
        focusedLabelColor = SantotoNavy,
        unfocusedLabelColor = SantotoTextBody,
        focusedBorderColor = SantotoNavy,
        unfocusedBorderColor = SantotoOutlineStrong,
        cursorColor = SantotoNavy,
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SantotoBg)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Resumen del evento
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            border = BorderStroke(1.dp, SantotoOutline),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Box(modifier = Modifier.fillMaxWidth().height(140.dp)) {
                    AsyncImage(
                        model = event.imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SantotoYellow,
                        modifier = Modifier.align(Alignment.TopStart).padding(12.dp)
                    ) {
                        Text(
                            text = event.category.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = SantotoNavy,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(event.title, style = MaterialTheme.typography.titleMedium, color = SantotoNavy)
                    Spacer(modifier = Modifier.height(10.dp))
                    EventDetailRow(Icons.Default.CalendarToday, event.dateBadge)
                    Spacer(modifier = Modifier.height(4.dp))
                    EventDetailRow(Icons.Default.AccessTime, event.time)
                    Spacer(modifier = Modifier.height(4.dp))
                    EventDetailRow(Icons.Default.LocationOn, event.location)
                    Spacer(modifier = Modifier.height(14.dp))
                    SpotsIndicator(available = event.availableSpots, capacity = event.capacity)
                }
            }
        }

        // Formulario
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            border = BorderStroke(1.dp, SantotoOutline),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Tus datos", style = MaterialTheme.typography.titleMedium, color = SantotoNavy)

                FormField(
                    value = fullName,
                    onValueChange = { fullName = it.take(120) },
                    label = "Nombre completo *",
                    icon = Icons.Default.Person,
                    error = if (showErrors && !nameOk) Validation.NAME_ERROR else null,
                    keyboard = KeyboardOptions(capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Next),
                    colors = fieldColors,
                    tag = "reg_full_name"
                )
                FormField(
                    value = phone,
                    onValueChange = { phone = it.take(20) },
                    label = "Celular / WhatsApp *",
                    icon = Icons.Default.Phone,
                    error = if (showErrors && !phoneOk) Validation.PHONE_ERROR else null,
                    keyboard = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
                    colors = fieldColors,
                    tag = "reg_phone"
                )
                FormField(
                    value = email,
                    onValueChange = { email = it.take(160) },
                    label = "Correo electrónico *",
                    icon = Icons.Default.Email,
                    error = if (showErrors && !emailOk) Validation.EMAIL_ERROR else null,
                    keyboard = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    colors = fieldColors,
                    tag = "reg_email"
                )
                FormField(
                    value = school,
                    onValueChange = { school = it.take(160) },
                    label = "Colegio o ciudad de procedencia",
                    icon = Icons.Default.School,
                    error = null,
                    keyboard = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Done),
                    colors = fieldColors,
                    tag = "reg_school"
                )

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = {
                        if (!nameOk || !phoneOk || !emailOk) {
                            showErrors = true
                            return@Button
                        }
                        if (event.availableSpots <= 0) {
                            Toast.makeText(context, "Lo sentimos, no hay más cupos para este evento.", Toast.LENGTH_LONG).show()
                            return@Button
                        }
                        isSubmitting = true
                        viewModel.registerForEvent(
                            event = event,
                            fullName = fullName,
                            phone = phone,
                            email = email,
                            school = school.ifBlank { "Aspirante Tunja" }
                        ) { registration ->
                            isSubmitting = false
                            viewModel.navigateTo(Screen.EventTicket(registration, event))
                        }
                    },
                    enabled = !isSubmitting && event.availableSpots > 0,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SantotoNavy, contentColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 52.dp)
                        .testTag("confirm_registration_button")
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(color = SantotoYellow, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Generando tu boleto…", style = MaterialTheme.typography.labelLarge)
                    } else {
                        Icon(Icons.Default.ConfirmationNumber, contentDescription = null, tint = SantotoYellow, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Confirmar inscripción", style = MaterialTheme.typography.labelLarge)
                    }
                }

                Text(
                    text = "Al confirmar recibirás un boleto digital con código QR para entrar al campus.",
                    style = MaterialTheme.typography.bodySmall,
                    color = SantotoTextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun FormField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    error: String?,
    keyboard: KeyboardOptions,
    colors: androidx.compose.material3.TextFieldColors,
    tag: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = SantotoNavy, modifier = Modifier.size(20.dp)) },
        singleLine = true,
        isError = error != null,
        supportingText = { if (error != null) Text(error) },
        keyboardOptions = keyboard,
        shape = RoundedCornerShape(12.dp),
        colors = colors,
        modifier = Modifier.fillMaxWidth().testTag(tag)
    )
}
