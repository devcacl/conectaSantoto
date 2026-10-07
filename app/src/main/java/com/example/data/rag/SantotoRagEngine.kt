package com.example.data.rag

import com.example.data.model.AcademicProgram
import com.example.data.model.ChatMessage
import com.example.data.model.RagDocument
import com.example.data.remote.CohereClient
import com.example.data.remote.GeminiClient

data class RagResult(
    val answer: String,
    val sources: List<String>,
    val suggestedActions: List<String> = emptyList()
)

object SantotoRagEngine {

    suspend fun query(
        userQuery: String,
        conversationHistory: List<ChatMessage> = emptyList(),
        dynamicDocuments: List<RagDocument> = emptyList()
    ): RagResult {
        val queryLower = userQuery.lowercase().trim()

        // 1. Detect if asking for a human advisor / real person
        val isHumanAdvisorRequest = queryLower.contains("humano") ||
                queryLower.contains("persona real") ||
                queryLower.contains("asesor real") ||
                queryLower.contains("alguien real") ||
                queryLower.contains("hablar con alguien") ||
                queryLower.contains("comunicarme con una persona") ||
                queryLower.contains("atencion humana") ||
                queryLower.contains("contacto real") ||
                queryLower.contains("whatsapp")

        // 2. Detect off-topic / unrelated queries
        val isExplicitGreeting = queryLower in listOf("hola", "buenas", "buenos dias", "buenas tardes", "buenas noches", "saludos", "hola tommy", "hola tommy bot")
        val isAcademicQuery = SantotoKnowledgeBase.PREGRADOS.any { prog ->
            queryLower.contains(prog.name.lowercase()) ||
                    queryLower.contains(prog.diferenciador.lowercase()) ||
                    queryLower.contains(prog.titulo.lowercase())
        } || dynamicDocuments.any { doc ->
            queryLower.split(" ").filter { it.length > 3 }.any { word -> doc.content.lowercase().contains(word) }
        } || queryLower.contains("costo") || queryLower.contains("precio") || queryLower.contains("carrera") ||
                queryLower.contains("pregrado") || queryLower.contains("posgrado") || queryLower.contains("semestre") ||
                queryLower.contains("inscrip") || queryLower.contains("matricul") || queryLower.contains("beca") ||
                queryLower.contains("descuento") || queryLower.contains("sena") || queryLower.contains("microcredencial") ||
                queryLower.contains("grado 11") || queryLower.contains("requisito") || queryLower.contains("campus") ||
                queryLower.contains("sede") || queryLower.contains("tunja") || queryLower.contains("universidad") ||
                queryLower.contains("santo tomas") || queryLower.contains("santoto") || queryLower.contains("horario") ||
                queryLower.contains("pensum") || queryLower.contains("materia") || queryLower.contains("admision") ||
                queryLower.contains("icetex") || queryLower.contains("financi") || queryLower.contains("visita") ||
                queryLower.contains("360") || queryLower.contains("open day") || queryLower.contains("feria") ||
                isHumanAdvisorRequest || isExplicitGreeting

        // Retrieve Relevant Knowledge Context
        val matchingPrograms = findMatchingPrograms(queryLower)
        val matchingMicro = findMatchingMicrocredentials(queryLower)
        val matchingRequisitos = findMatchingRequisitos(queryLower)
        val isDirectoryQuery = queryLower.contains("contacto") || queryLower.contains("telefono") || queryLower.contains("linea") || queryLower.contains("correo") || queryLower.contains("whatsapp")
        val isPosgradoQuery = queryLower.contains("posgrado") || queryLower.contains("maestria") || queryLower.contains("especializa")

        val contextBuilder = StringBuilder()
        val sources = mutableListOf<String>()

        // 2.1 Dynamic RAG Documents from Admin Panel (.md Knowledge Base)
        if (dynamicDocuments.isNotEmpty()) {
            dynamicDocuments.forEach { doc ->
                contextBuilder.append("ARCHIVO OFICIAL [${doc.title}] (Categoría: ${doc.category}):\n")
                contextBuilder.append(doc.content).append("\n\n")
                sources.add(doc.title)
            }
        } else {
            // Fallback to embedded knowledge if database has not yet been initialized
            if (matchingPrograms.isNotEmpty()) {
                contextBuilder.append("OFERTA DE PREGRADOS SANTOTO TUNJA (2026-2027):\n")
                matchingPrograms.forEach { prog ->
                    contextBuilder.append("• Carrera: ").append(prog.name).append("\n")
                    contextBuilder.append("  Título: ").append(prog.titulo).append("\n")
                    contextBuilder.append("  Inversión Semestral: ").append(prog.inversion2026).append("\n")
                    contextBuilder.append("  Duración: ").append(prog.semestres).append(" semestres\n")
                    contextBuilder.append("  Modalidad: ").append(prog.modalidad).append("\n")
                    contextBuilder.append("  SNIES: ").append(prog.snies).append("\n")
                    contextBuilder.append("  Enfoque diferenciador: ").append(prog.diferenciador).append("\n")
                    contextBuilder.append("  Perfil y empleabilidad: ").append(prog.empleabilidad).append("\n\n")
                }
                sources.add("Portafolio Oficial Pregrados USTA Tunja")
            }

            if (matchingMicro.isNotEmpty()) {
                contextBuilder.append("MICROCREDENCIALES GRADO 11° (COLEGIOS):\n")
                matchingMicro.forEach { contextBuilder.append("• ").append(it).append("\n") }
                sources.add("Programa Microcredenciales Grado 11° USTA Tunja ($100.000)")
            }

            if (matchingRequisitos.isNotEmpty() || queryLower.contains("inscri") || queryLower.contains("requisit")) {
                contextBuilder.append("PASOS Y REQUISITOS DE INSCRIPCIÓN Y MATRÍCULA:\n")
                SantotoKnowledgeBase.REQUISITOS_INFO.forEach { (cat, desc) ->
                    contextBuilder.append(cat).append(":\n").append(desc).append("\n\n")
                }
                sources.add("Reglamento de Admisiones y Matrículas USTA Tunja")
            }
        }

        // Always include Official Contacts & WhatsApp links
        if (isDirectoryQuery || isHumanAdvisorRequest || dynamicDocuments.none { it.category == "Directorio" }) {
            contextBuilder.append("LÍNEAS OFICIALES DE ATENCIÓN Y WHATSAPP SANTOTO TUNJA:\n")
            contextBuilder.append("• WhatsApp Asesoría 1: +57 317 514 1479 (Enlace: https://wa.me/573175141479)\n")
            contextBuilder.append("• WhatsApp Asesoría 2: +57 317 510 5383 (Enlace: https://wa.me/573175105383)\n")
            contextBuilder.append("• Correo Admisiones: admisiones@ustatunja.edu.co\n")
            contextBuilder.append("• Portal Web Oficial: https://santototunja.edu.co/admisiones\n")
            contextBuilder.append("• Portal Inscripciones SAC: https://sac.usantotomas.edu.co/sgacampus/services/inscripciones/home\n")
            sources.add("Directorio Oficial de Admisiones Tunja")
        }

        if (isPosgradoQuery && dynamicDocuments.none { it.category == "Posgrados" }) {
            contextBuilder.append("OFERTA DE POSGRADOS TUNJA:\n")
            SantotoKnowledgeBase.POSGRADOS_RESUMEN.forEach { contextBuilder.append("• ").append(it).append("\n") }
            sources.add("Oferta de Posgrados Santoto Tunja")
        }

        val retrievedContext = contextBuilder.toString()

        val systemPrompt = """
            Eres 'Tommy Bot', el asesor inteligente oficial de Admisiones y Marketing de la Universidad Santo Tomás Seccional Tunja (Santoto Tunja).

            PERSONALIDAD Y TONO:
            - Amistoso, formal, colaborador, proactivo y comercialmente persuasivo ("vendedor institucional").
            - Tu objetivo es enamorar y motivar a los aspirantes para que se matriculen en la Santoto Tunja, destacando su acreditación de alta calidad multicampus, modernos laboratorios de Inteligencia Artificial, robótica, simulación clínica y talleres en el Campus Av. Universitaria y Campus Centro Histórico.
            - Motiva siempre al aspirante a dar el siguiente paso (agendar una visita al campus, solicitar información personalizada o inscribirse en línea en el portal SAC).

            REGLAS ESTRICTAS DE ALCANCE Y SEGURIDAD:
            1. EXCLUSIVIDAD USTA TUNJA: Solo estás autorizado a responder preguntas directamente relacionadas con la Universidad Santo Tomás Seccional Tunja (carreras de pregrado, posgrados, microcredenciales de grado 11°, costos 2026/2027, requisitos de admisión, homologaciones SENA, fechas, visitas y sedes en Tunja).
            2. PREGUNTAS NO RELACIONADAS: Si la pregunta NO es sobre la Universidad Santo Tomás Seccional Tunja o temas de admisiones y carreras universitarias, DEBES responder exactamente en este sentido:
            "Como Tommy Bot, asesor oficial de la Universidad Santo Tomás Seccional Tunja, únicamente estoy autorizado para responder preguntas relacionadas con nuestra oferta académica, procesos de admisión, costos de matrícula y vida universitaria en la Santoto Tunja. ¿Te gustaría conocer nuestras carreras o requisitos de ingreso?"
            3. SOLICITUD DE PERSONA REAL O WHATSAPP: Si el usuario pide hablar con una persona real, asesor humano o WhatsApp, dale una cordial bienvenida y suministra de inmediato los enlaces oficiales:
            - WhatsApp Admisiones 1: https://wa.me/573175141479 (Línea: 317 514 14 79)
            - WhatsApp Admisiones 2: https://wa.me/573175105383 (Línea: 317 510 53 83)
            Invítalo a dar clic para chatear directamente con nuestro equipo de admisiones en tiempo real.
            4. RECOMENDACIÓN EXACTA DE CARRERAS SEGÚN ARCHIVOS OFICIALES (.md):
            - Tu única y absoluta fuente de verdad sobre carreras, títulos, valores de matrícula y requisitos son los ARCHIVOS OFICIALES (.md) suministrados en el CONTEXTO OFICIAL.
            - Recomienda y enumera ÚNICAMENTE las carreras que figuren explícitamente en los archivos oficiales provistos. NUNCA inventes programas ni agregues carreras que no estén en los archivos.
            - Si el usuario pregunta qué carreras hay o cuántas carreras ofrece la Santoto Tunja, menciona y describe las que están en el archivo de pregrados sin inventar ninguna otra.
            5. FORMATO LIMPIO: Usa viñetas ordenadas (•) y resalta aspectos clave con **negrita**.

            CONTEXTO OFICIAL SANTOTO TUNJA:
            $retrievedContext
        """.trimIndent()

        // 3. Try Cohere API with multi-turn conversation history
        val cohereResult = CohereClient.generateChat(
            systemPrompt = systemPrompt,
            userMessage = userQuery,
            history = conversationHistory
        )

        val answerText = if (cohereResult.isSuccess) {
            cohereResult.getOrNull() ?: fallbackResponse(userQuery, matchingPrograms, isHumanAdvisorRequest, isAcademicQuery)
        } else {
            val geminiResult = GeminiClient.generateContent(
                systemPrompt = systemPrompt,
                userPrompt = userQuery
            )
            if (geminiResult.isSuccess) {
                geminiResult.getOrNull() ?: fallbackResponse(userQuery, matchingPrograms, isHumanAdvisorRequest, isAcademicQuery)
            } else {
                fallbackResponse(userQuery, matchingPrograms, isHumanAdvisorRequest, isAcademicQuery)
            }
        }

        val suggestedActions = determineSuggestedActions(queryLower, matchingPrograms, isHumanAdvisorRequest)

        return RagResult(
            answer = answerText,
            sources = if (sources.isEmpty()) listOf("Base de Datos Oficial USTA Tunja") else sources.distinct(),
            suggestedActions = suggestedActions
        )
    }

    private fun findMatchingPrograms(query: String): List<AcademicProgram> {
        val keywords = query.split(" ", ",", ".").filter { it.length > 2 }
        return SantotoKnowledgeBase.PREGRADOS.filter { prog ->
            val progLower = prog.name.lowercase()
            keywords.any { kw -> progLower.contains(kw) } ||
                    prog.diferenciador.lowercase().contains(query) ||
                    prog.empleabilidad.lowercase().contains(query)
        }
    }

    private fun findMatchingMicrocredentials(query: String): List<String> {
        if (!query.contains("micro") && !query.contains("grado 11") && !query.contains("colegio") && !query.contains("100")) {
            val keywords = query.split(" ").filter { it.length > 3 }
            val byKeyword = SantotoKnowledgeBase.MICROCREDENCIALES_GRADO_11.filter { item ->
                keywords.any { kw -> item.lowercase().contains(kw) }
            }
            return byKeyword
        }
        return SantotoKnowledgeBase.MICROCREDENCIALES_GRADO_11
    }

    private fun findMatchingRequisitos(query: String): List<String> {
        return SantotoKnowledgeBase.REQUISITOS_INFO.values.toList()
    }

    private fun determineSuggestedActions(
        query: String,
        programs: List<AcademicProgram>,
        isHumanRequest: Boolean
    ): List<String> {
        val actions = mutableListOf<String>()

        if (isHumanRequest) {
            actions.add("📱 Chatear por WhatsApp (317 514 14 79)")
            actions.add("📱 Chatear por WhatsApp (317 510 53 83)")
            actions.add("Agendar Visita al Campus")
            return actions
        }

        if (programs.isNotEmpty()) {
            val prog = programs.first()
            actions.add("¿Qué costo tiene ${prog.name}?")
            actions.add("¿Cómo es la homologación SENA?")
            actions.add("Agendar visita guiada al campus")
        } else if (query.contains("micro") || query.contains("grado 11")) {
            actions.add("Inscripción Microcredenciales $100.000")
            actions.add("¿Qué colegios tienen convenio?")
        } else {
            actions.add("Conocer las carreras de Tunja")
            actions.add("Microcredenciales Grado 11° ($100.000)")
            actions.add("Hablar con un asesor por WhatsApp")
        }
        return actions.take(3)
    }

    private fun fallbackResponse(
        query: String,
        matchingPrograms: List<AcademicProgram>,
        isHumanRequest: Boolean,
        isAcademicQuery: Boolean
    ): String {
        if (isHumanRequest) {
            return "¡Con gusto te comunico con nuestro equipo humano de admisiones! Puedes escribirnos de inmediato a nuestras líneas oficiales de WhatsApp:\n\n" +
                    "• **WhatsApp Admisiones 1:** [317 514 1479](https://wa.me/573175141479)\n" +
                    "• **WhatsApp Admisiones 2:** [317 510 5383](https://wa.me/573175105383)\n\n" +
                    "¡Nuestros profesionales de admisiones te orientarán de inmediato!"
        }

        if (!isAcademicQuery) {
            return "Como Tommy Bot, asesor oficial de Admisiones de la Universidad Santo Tomás Seccional Tunja, únicamente estoy autorizado para responder consultas sobre nuestra oferta académica, procesos de admisión, costos de matrícula y vida universitaria en la Santoto Tunja.\n\n¿En qué carrera o trámite de admisión te gustaría que te oriente hoy?"
        }

        if (matchingPrograms.isNotEmpty()) {
            val prog = matchingPrograms.first()
            return "¡Excelente elección! **${prog.name}** en la Universidad Santo Tomás Seccional Tunja es un programa líder con acreditación de alta calidad.\n\n" +
                    "• **Título:** ${prog.titulo}\n" +
                    "• **Inversión semestral 2026/2027:** ${prog.inversion2026}\n" +
                    "• **Duración:** ${prog.semestres} semestres\n" +
                    "• **Modalidad:** ${prog.modalidad}\n" +
                    "• **Diferenciador:** ${prog.diferenciador}\n\n" +
                    "¿Te gustaría que agendemos una visita al campus o conocer las opciones de becas y financiación directa?"
        }

        return "¡Hola! Soy **Tommy Bot**, tu asesor oficial de la Universidad Santo Tomás Seccional Tunja. Contamos con exactamente 22 carreras de pregrado, posgrados y microcredenciales para grado 11°. ¿Sobre qué carrera o proceso de admisión te gustaría recibir información?"
    }
}
