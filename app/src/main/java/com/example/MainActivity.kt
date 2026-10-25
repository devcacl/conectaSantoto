package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.SantotoBottomBar
import com.example.ui.components.SantotoHeader
import com.example.ui.screens.AdmissionsDashboardScreen
import com.example.ui.screens.ChatbotScreen
import com.example.ui.screens.EventRegistrationScreen
import com.example.ui.screens.EventTicketScreen
import com.example.ui.screens.EventsScreen
import com.example.ui.screens.Explora360Screen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.NewsScreen
import com.example.ui.screens.ProgramsCatalogScreen
import com.example.ui.screens.ScheduleVisitScreen
import com.example.ui.screens.VocationalTestScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.SantotoViewModel
import com.example.viewmodel.Screen
import com.example.viewmodel.UserRole

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                SantotoApp()
            }
        }
    }
}

private const val PIN_PLACEHOLDER = "MY_ADMIN_PIN"

/** Acceso provisional al panel administrativo; reemplazar por Firebase Auth antes de produccion. */
@Composable
private fun AdminPinDialog(onDismiss: () -> Unit, onAuthorized: () -> Unit) {
    var pin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }
    val configured = BuildConfig.ADMIN_PIN.isNotBlank() && BuildConfig.ADMIN_PIN != PIN_PLACEHOLDER

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Acceso Administrativo", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text(
                    if (configured) "Ingresa el PIN del equipo de Admisiones."
                    else "El acceso administrativo no está configurado. Define ADMIN_PIN en el archivo .env.",
                    style = MaterialTheme.typography.bodyMedium
                )
                if (configured) {
                    OutlinedTextField(
                        value = pin,
                        onValueChange = { pin = it; error = false },
                        label = { Text("PIN") },
                        singleLine = true,
                        isError = error,
                        supportingText = { if (error) Text("PIN incorrecto") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
                    )
                }
            }
        },
        confirmButton = {
            if (configured) {
                TextButton(onClick = { if (pin == BuildConfig.ADMIN_PIN) onAuthorized() else error = true }) {
                    Text("Ingresar")
                }
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

@Composable
fun SantotoApp(
    viewModel: SantotoViewModel = viewModel()
) {
    val currentRole by viewModel.currentRole.collectAsStateWithLifecycle()
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val isVrModeEnabled by viewModel.isVrModeEnabled.collectAsStateWithLifecycle()

    var showAdminPinDialog by remember { mutableStateOf(false) }
    if (showAdminPinDialog) {
        AdminPinDialog(
            onDismiss = { showAdminPinDialog = false },
            onAuthorized = {
                showAdminPinDialog = false
                viewModel.setRole(UserRole.PROFESIONAL_ADMISIONES)
            }
        )
    }

    val showHeaderBack = currentScreen !is Screen.Home && currentScreen !is Screen.AdmissionsPanel

    val headerTitle = when (currentScreen) {
        Screen.Home -> "Admisiones y Marketing"
        Screen.Chatbot -> "Tommy Bot • Asesor USTA"
        Screen.Explora360 -> if (isVrModeEnabled) "Explora 360 (Gafas VR)" else "Explora 360 Virtual"
        Screen.VocationalTest -> "Test de Orientación"
        Screen.ScheduleVisit -> "Agendar Visita al Campus"
        Screen.ProgramsCatalog -> "Programas Académicos"
        Screen.Events -> "Próximos Eventos & Open Day"
        Screen.News -> "Noticias & Actualidad USTA"
        is Screen.EventConfirmRegistration -> "Inscripción al Evento"
        is Screen.EventTicket -> "Boleto Oficial de Acceso"
        is Screen.NewsDetail -> "Noticia USTA"
        Screen.AdmissionsPanel -> "Panel de Admisiones"
        is Screen.ProgramDetail -> "Ficha de Programa"
    }

    val headerSubtitle = when (currentRole) {
        UserRole.ASPIRANTE -> "Santoto Tunja • 2026-2027"
        UserRole.PROFESIONAL_ADMISIONES -> "Portal Administrativo Tunja"
    }

    val shouldShowBottomBar = (!isVrModeEnabled || currentScreen !is Screen.Explora360) &&
            currentScreen !is Screen.Chatbot &&
            currentScreen !is Screen.EventConfirmRegistration &&
            currentScreen !is Screen.EventTicket

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (!isVrModeEnabled || currentScreen !is Screen.Explora360) {
                SantotoHeader(
                    currentRole = currentRole,
                    onToggleRole = {
                        if (currentRole == UserRole.ASPIRANTE) {
                            showAdminPinDialog = true
                        } else {
                            viewModel.setRole(UserRole.ASPIRANTE)
                        }
                    },
                    showBackButton = showHeaderBack,
                    onBackClick = { viewModel.navigateBack() },
                    titleText = headerTitle,
                    subtitleText = headerSubtitle
                )
            }
        },
        bottomBar = {
            if (shouldShowBottomBar) {
                SantotoBottomBar(
                    currentScreen = currentScreen,
                    currentRole = currentRole,
                    onNavigate = { screen -> viewModel.navigateTo(screen) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val screen = currentScreen
            when (screen) {
                Screen.Home -> HomeScreen(viewModel = viewModel)
                Screen.Chatbot -> ChatbotScreen(viewModel = viewModel)
                Screen.Explora360 -> Explora360Screen(viewModel = viewModel)
                Screen.VocationalTest -> VocationalTestScreen(viewModel = viewModel)
                Screen.ScheduleVisit -> ScheduleVisitScreen(viewModel = viewModel)
                Screen.ProgramsCatalog -> ProgramsCatalogScreen(viewModel = viewModel)
                Screen.Events -> EventsScreen(viewModel = viewModel)
                Screen.News -> NewsScreen(viewModel = viewModel)
                is Screen.EventConfirmRegistration -> EventRegistrationScreen(event = screen.event, viewModel = viewModel)
                is Screen.EventTicket -> EventTicketScreen(registration = screen.registration, event = screen.event, viewModel = viewModel)
                is Screen.NewsDetail -> NewsScreen(viewModel = viewModel)
                Screen.AdmissionsPanel -> AdmissionsDashboardScreen(viewModel = viewModel)
                is Screen.ProgramDetail -> ProgramsCatalogScreen(viewModel = viewModel)
            }
        }
    }
}
