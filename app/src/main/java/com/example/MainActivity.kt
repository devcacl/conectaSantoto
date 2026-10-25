package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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

@Composable
fun SantotoApp(
    viewModel: SantotoViewModel = viewModel()
) {
    val currentRole by viewModel.currentRole.collectAsStateWithLifecycle()
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val isVrModeEnabled by viewModel.isVrModeEnabled.collectAsStateWithLifecycle()

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
                        val nextRole = if (currentRole == UserRole.ASPIRANTE) {
                            UserRole.PROFESIONAL_ADMISIONES
                        } else {
                            UserRole.ASPIRANTE
                        }
                        viewModel.setRole(nextRole)
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
