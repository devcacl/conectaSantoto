package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.data.model.CampusEvent
import com.example.ui.components.EventDetailRow
import com.example.ui.components.SpotsIndicator
import com.example.ui.theme.SantotoBg
import com.example.ui.theme.SantotoNavy
import com.example.ui.theme.SantotoOutline
import com.example.ui.theme.SantotoOutlineStrong
import com.example.ui.theme.SantotoTextBody
import com.example.ui.theme.SantotoTextSecondary
import com.example.ui.theme.SantotoYellow
import com.example.viewmodel.SantotoViewModel
import com.example.viewmodel.Screen

@Composable
fun EventsScreen(
    viewModel: SantotoViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    val eventsList by viewModel.events.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Eventos, 1: Noticias

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SantotoBg)
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = SantotoNavy,
            contentColor = Color.White,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = SantotoYellow,
                    height = 3.dp
                )
            }
        ) {
            EventsTab("Eventos", Icons.Default.Event, selectedTab == 0) { selectedTab = 0 }
            EventsTab("Noticias", Icons.Default.Newspaper, selectedTab == 1) { selectedTab = 1 }
        }

        if (selectedTab == 1) {
            NewsScreen(viewModel = viewModel, modifier = Modifier.weight(1f))
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = when (eventsList.size) {
                            0 -> "Open Days, talleres y ferias"
                            1 -> "1 evento próximo"
                            else -> "${eventsList.size} eventos próximos"
                        },
                        style = MaterialTheme.typography.labelMedium,
                        color = SantotoTextSecondary
                    )
                }

                if (eventsList.isEmpty()) {
                    item { NoEvents() }
                }

                items(eventsList, key = { it.id }) { event ->
                    EventCard(
                        event = event,
                        onRegister = { viewModel.navigateTo(Screen.EventConfirmRegistration(event)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun EventsTab(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, selected: Boolean, onClick: () -> Unit) {
    val tint = if (selected) SantotoYellow else SantotoOutlineStrong
    Tab(
        selected = selected,
        onClick = onClick,
        modifier = Modifier.heightIn(min = 48.dp),
        text = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(label, style = MaterialTheme.typography.labelLarge, color = tint)
            }
        }
    )
}

@Composable
private fun NoEvents() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.Event, contentDescription = null, tint = SantotoNavy.copy(alpha = 0.35f), modifier = Modifier.size(48.dp))
        Spacer(modifier = Modifier.height(12.dp))
        Text("No hay eventos por ahora", style = MaterialTheme.typography.titleSmall, color = SantotoNavy)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            "Vuelve pronto: publicamos Open Days y talleres cada mes.",
            style = MaterialTheme.typography.bodyMedium,
            color = SantotoTextSecondary
        )
    }
}

@Composable
private fun EventCard(event: CampusEvent, onRegister: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().testTag("event_card_${event.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, SantotoOutline)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(160.dp)) {
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

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SantotoNavy,
                    modifier = Modifier.align(Alignment.BottomStart).padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Event, contentDescription = null, tint = SantotoYellow, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(event.dateBadge, style = MaterialTheme.typography.labelMedium, color = Color.White)
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(event.title, style = MaterialTheme.typography.titleMedium, color = SantotoNavy)
                Spacer(modifier = Modifier.height(10.dp))
                EventDetailRow(Icons.Default.AccessTime, event.time)
                Spacer(modifier = Modifier.height(4.dp))
                EventDetailRow(Icons.Default.LocationOn, event.location)

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = event.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = SantotoTextBody,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(14.dp))
                SpotsIndicator(available = event.availableSpots, capacity = event.capacity)

                Spacer(modifier = Modifier.height(14.dp))
                Button(
                    onClick = onRegister,
                    enabled = event.availableSpots > 0,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SantotoNavy, contentColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp)
                        .testTag("inscribirme_button_${event.id}")
                ) {
                    Icon(Icons.Default.ConfirmationNumber, contentDescription = null, tint = SantotoYellow, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (event.availableSpots > 0) "Inscribirme" else "Cupos agotados",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}
