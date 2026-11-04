package com.example.ui.screens

import android.content.Intent
import com.example.ui.theme.SantotoOutline
import com.example.ui.theme.SantotoTextMuted
import com.example.ui.theme.SantotoTextPrimary
import com.example.ui.theme.SantotoTextSecondary
import android.net.Uri
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.ChatMessage
import com.example.ui.theme.SantotoBg
import com.example.ui.theme.SantotoNavy
import com.example.ui.theme.SantotoSuccess
import com.example.ui.theme.SantotoYellow
import com.example.viewmodel.SantotoViewModel

@Composable
fun ChatbotScreen(
    viewModel: SantotoViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    val context = LocalContext.current
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val isLoading by viewModel.isChatLoading.collectAsStateWithLifecycle()

    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(chatMessages.size, isLoading) {
        if (chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    val quickQuestions = listOf(
        "¿Qué carreras ofrece la Santoto Tunja?",
        "¿Cuánto cuesta Ingeniería de Sistemas?",
        "Microcredenciales Grado 11° ($100.000)",
        "Hablar con un asesor por WhatsApp",
        "Requisitos de inscripción",
        "Homologación y convenio SENA",
        "¿Cuánto cuesta Derecho?"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SantotoBg)
            .imePadding()
    ) {
        // Top Header Banner - Tommy Bot Persona
        Surface(
            color = SantotoNavy,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.SmartToy,
                    contentDescription = null,
                    tint = SantotoYellow,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Tommy Bot • Asesor Virtual de Admisiones",
                        fontSize = 12.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Exclusivo USTA Tunja • Respuestas con lógica y memoria",
                        fontSize = 11.sp,
                        color = SantotoYellow
                    )
                }
            }
        }

        // Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(chatMessages, key = { it.id }) { message ->
                MessageBubble(
                    message = message,
                    onActionClick = { actionText ->
                        if (actionText.contains("WhatsApp", ignoreCase = true) || actionText.contains("317 514") || actionText.contains("317 510")) {
                            val phone = if (actionText.contains("510")) "573175105383" else "573175141479"
                            val url = "https://wa.me/$phone?text=Hola! Me contacto desde la app de Admisiones Santoto Tunja para solicitar orientación personalizada."
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            try { context.startActivity(intent) } catch (e: Exception) { /* ignore */ }
                        } else {
                            viewModel.sendChatMessage(actionText)
                        }
                    },
                    onOpenWhatsApp = { phone ->
                        val url = "https://wa.me/57$phone?text=Hola! Quisiera hablar con un asesor de Admisiones de la Santoto Tunja."
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        try { context.startActivity(intent) } catch (e: Exception) { /* ignore */ }
                    }
                )
            }

            if (isLoading) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 12.dp, top = 6.dp)
                    ) {
                        CircularProgressIndicator(
                            color = SantotoNavy,
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Tommy Bot consultando la información oficial...",
                            fontSize = 12.sp,
                            color = SantotoNavy,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Quick Suggestion Chips (el saludo inicial ya trae sus propias sugerencias)
        if (chatMessages.size > 1) LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(quickQuestions) { question ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    modifier = Modifier
                        .border(1.dp, SantotoNavy.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
                        .clickable {
                            if (question.contains("WhatsApp", ignoreCase = true)) {
                                val url = "https://wa.me/573175141479?text=Hola! Quisiera comunicarme con un asesor de admisiones de la Santoto Tunja."
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                try { context.startActivity(intent) } catch (e: Exception) { /* ignore */ }
                            } else {
                                viewModel.sendChatMessage(question)
                            }
                        }
                ) {
                    Text(
                        text = question,
                        fontSize = 11.sp,
                        color = SantotoNavy,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Input Field - Perfectly legible dark text on white background
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            color = Color.White,
            shape = RoundedCornerShape(26.dp),
            shadowElevation = 3.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, SantotoOutline)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(
                            text = "Escribe tu pregunta...",
                            fontSize = 14.sp,
                            maxLines = 1,
                            color = SantotoTextMuted
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_input_text"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = SantotoTextPrimary,
                        unfocusedTextColor = SantotoTextPrimary,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        cursorColor = SantotoNavy,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    maxLines = 3
                )

                IconButton(
                    onClick = {
                        if (inputText.isNotBlank() && !isLoading) {
                            val textToSend = inputText
                            inputText = ""
                            viewModel.sendChatMessage(textToSend)
                        }
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (inputText.isNotBlank()) SantotoNavy else SantotoOutline)
                        .testTag("chat_send_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Enviar mensaje",
                        tint = if (inputText.isNotBlank()) SantotoYellow else SantotoTextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(
    message: ChatMessage,
    onActionClick: (String) -> Unit,
    onOpenWhatsApp: (String) -> Unit
) {
    val isUser = message.isFromUser
    val hasWhatsAppReference = !isUser && (
            message.text.contains("317 514 1479") ||
            message.text.contains("317 510 5383") ||
            message.text.contains("wa.me") ||
            message.text.contains("WhatsApp", ignoreCase = true)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Surface(
                shape = CircleShape,
                color = SantotoNavy,
                modifier = Modifier
                    .size(34.dp)
                    .align(Alignment.Top)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.SmartToy,
                        contentDescription = "Tommy Bot",
                        tint = SantotoYellow,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            modifier = Modifier.widthIn(max = 310.dp),
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
        ) {
            Surface(
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (isUser) 16.dp else 4.dp,
                    bottomEnd = if (isUser) 4.dp else 16.dp
                ),
                color = if (isUser) SantotoNavy else Color.White,
                shadowElevation = 1.dp,
                border = if (!isUser) androidx.compose.foundation.BorderStroke(1.dp, SantotoOutline) else null
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    if (!isUser) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 6.dp)
                        ) {
                            Text(
                                text = "Tommy Bot",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = SantotoNavy
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = SantotoYellow.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "Asesor USTA",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SantotoNavy,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    // Render Markdown cleanly without raw ** **
                    MarkdownMessageText(
                        rawText = message.text,
                        isUser = isUser
                    )

                    // WhatsApp Direct Contact Buttons if real person / WhatsApp was requested
                    if (hasWhatsAppReference) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Button(
                                onClick = { onOpenWhatsApp("3175141479") },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SantotoSuccess),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Chatear con Asesor: 317 514 14 79", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }

                            Button(
                                onClick = { onOpenWhatsApp("3175105383") },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SantotoSuccess),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Chatear con Asesor: 317 510 53 83", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }

                    // Cited Sources indicator
                    if (!isUser && message.sources.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = SantotoNavy,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Fuente: " + message.sources.joinToString(", "),
                                fontSize = 11.sp,
                                color = SantotoTextSecondary,
                                lineHeight = 13.sp
                            )
                        }
                    }

                    // Suggested Prompts Chips
                    if (!isUser && message.suggestedPrompts.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            message.suggestedPrompts.forEach { prompt ->
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = SantotoNavy,
                                    modifier = Modifier.clickable { onActionClick(prompt) }
                                ) {
                                    Text(
                                        text = prompt,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SantotoYellow,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Parses and formats markdown text cleanly:
 * - Replaces `**bold text**` with actual Bold SpanStyle (no raw asterisks shown)
 * - Formats list bullet points `• `, `- `, `* ` with clean margins
 * - Formats headers `### `
 */
@Composable
private fun MarkdownMessageText(
    rawText: String,
    isUser: Boolean
) {
    val textColor = if (isUser) Color.White else SantotoTextPrimary
    val annotatedString = remember(rawText, textColor) {
        parseMarkdownToAnnotatedString(rawText, textColor)
    }

    Text(
        text = annotatedString,
        fontSize = 13.sp,
        lineHeight = 20.sp,
        color = textColor
    )
}

private fun parseMarkdownToAnnotatedString(text: String, defaultColor: Color): AnnotatedString {
    return buildAnnotatedString {
        val lines = text.lines()
        lines.forEachIndexed { index, line ->
            var trimmed = line.trim()

            // Header handling
            val isHeader = trimmed.startsWith("###") || trimmed.startsWith("##") || trimmed.startsWith("#")
            if (isHeader) {
                trimmed = trimmed.replace(Regex("^#+\\s*"), "")
            }

            // Bullet handling
            if (trimmed.startsWith("* ") || trimmed.startsWith("- ") || trimmed.startsWith("• ")) {
                trimmed = "• " + trimmed.substring(2)
            }

            // Parse bold **text** within the line
            var cursor = 0
            while (cursor < trimmed.length) {
                val startBold = trimmed.indexOf("**", cursor)
                if (startBold == -1) {
                    append(trimmed.substring(cursor))
                    break
                }
                // Append text before **
                append(trimmed.substring(cursor, startBold))
                val endBold = trimmed.indexOf("**", startBold + 2)
                if (endBold == -1) {
                    append(trimmed.substring(startBold))
                    break
                }
                // Append the bold content without asterisks
                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = defaultColor)) {
                    append(trimmed.substring(startBold + 2, endBold))
                }
                cursor = endBold + 2
            }

            if (index < lines.size - 1) {
                append("\n")
            }
        }
    }
}
