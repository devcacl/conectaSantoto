package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SantotoNavy
import com.example.ui.theme.SantotoYellow
import com.example.viewmodel.Screen
import com.example.viewmodel.UserRole

/**
 * An original, floating island dock navigation bar:
 * - Floating pill shape with elevation and rounded corners
 * - Minimalist, single tone blue with golden yellow active indicators
 * - Smooth touch targets and zero clutter
 */
@Composable
fun SantotoBottomBar(
    currentScreen: Screen,
    currentRole: UserRole,
    onNavigate: (Screen) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(28.dp),
                    ambientColor = SantotoNavy.copy(alpha = 0.3f),
                    spotColor = SantotoNavy.copy(alpha = 0.5f)
                ),
            shape = RoundedCornerShape(28.dp),
            color = SantotoNavy
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                if (currentRole == UserRole.ASPIRANTE) {
                    FloatingDockItem(
                        icon = Icons.Default.Home,
                        label = "Inicio",
                        isSelected = currentScreen is Screen.Home,
                        onClick = { onNavigate(Screen.Home) },
                        modifier = Modifier.weight(1f)
                    )
                    FloatingDockItem(
                        icon = Icons.AutoMirrored.Filled.Chat,
                        label = "Asesor IA",
                        isSelected = currentScreen is Screen.Chatbot,
                        onClick = { onNavigate(Screen.Chatbot) },
                        modifier = Modifier.weight(1f)
                    )
                    FloatingDockItem(
                        icon = Icons.Default.CalendarMonth,
                        label = "Visitas",
                        isSelected = currentScreen is Screen.ScheduleVisit,
                        onClick = { onNavigate(Screen.ScheduleVisit) },
                        modifier = Modifier.weight(1f)
                    )
                    FloatingDockItem(
                        icon = Icons.Default.ViewInAr,
                        label = "360 & VR",
                        isSelected = currentScreen is Screen.Explora360,
                        onClick = { onNavigate(Screen.Explora360) },
                        modifier = Modifier.weight(1f)
                    )
                    FloatingDockItem(
                        icon = Icons.Default.Psychology,
                        label = "Vocacional",
                        isSelected = currentScreen is Screen.VocationalTest,
                        onClick = { onNavigate(Screen.VocationalTest) },
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    FloatingDockItem(
                        icon = Icons.Default.Home,
                        label = "Inicio",
                        isSelected = currentScreen is Screen.Home,
                        onClick = { onNavigate(Screen.Home) },
                        modifier = Modifier.weight(1f)
                    )
                    FloatingDockItem(
                        icon = Icons.Default.People,
                        label = "Leads",
                        isSelected = currentScreen is Screen.AdmissionsPanel,
                        onClick = { onNavigate(Screen.AdmissionsPanel) },
                        modifier = Modifier.weight(1f)
                    )
                    FloatingDockItem(
                        icon = Icons.Default.CalendarMonth,
                        label = "Visitas",
                        isSelected = currentScreen is Screen.ScheduleVisit,
                        onClick = { onNavigate(Screen.ScheduleVisit) },
                        modifier = Modifier.weight(1f)
                    )
                    FloatingDockItem(
                        icon = Icons.Default.Event,
                        label = "Eventos",
                        isSelected = currentScreen is Screen.Events,
                        onClick = { onNavigate(Screen.Events) },
                        modifier = Modifier.weight(1f)
                    )
                    FloatingDockItem(
                        icon = Icons.Default.Analytics,
                        label = "Métricas",
                        isSelected = currentScreen is Screen.AdmissionsPanel,
                        onClick = { onNavigate(Screen.AdmissionsPanel) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun FloatingDockItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val iconColor by animateColorAsState(
        targetValue = if (isSelected) SantotoNavy else Color.White.copy(alpha = 0.7f),
        animationSpec = tween(durationMillis = 200),
        label = "iconColor"
    )

    val pillColor by animateColorAsState(
        targetValue = if (isSelected) SantotoYellow else Color.Transparent,
        animationSpec = tween(durationMillis = 200),
        label = "pillColor"
    )

    val pillSize by animateDpAsState(
        targetValue = if (isSelected) 36.dp else 28.dp,
        animationSpec = tween(durationMillis = 200),
        label = "pillSize"
    )

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(pillSize)
                .clip(CircleShape)
                .background(pillColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = label,
            fontSize = 9.5.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) SantotoYellow else Color.White.copy(alpha = 0.7f),
            maxLines = 1,
            textAlign = TextAlign.Center
        )
    }
}
