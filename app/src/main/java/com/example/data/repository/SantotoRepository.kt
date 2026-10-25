package com.example.data.repository

import com.example.data.local.SantotoDao
import com.example.data.model.CampusEvent
import com.example.data.model.CampusNews
import com.example.data.model.CampusVisit
import com.example.data.model.EventRegistration
import com.example.data.model.Lead
import com.example.data.model.LeadStatus
import com.example.data.model.RagDocument
import com.example.data.model.VisitStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class SantotoRepository(private val dao: SantotoDao) {

    val allLeads: Flow<List<Lead>> = dao.getAllLeads()
    val allVisits: Flow<List<CampusVisit>> = dao.getAllVisits()
    val allEvents: Flow<List<CampusEvent>> = dao.getAllEvents()
    val allNews: Flow<List<CampusNews>> = dao.getAllNews()
    val allRegistrations: Flow<List<EventRegistration>> = dao.getAllRegistrations()
    val allRagDocuments: Flow<List<RagDocument>> = dao.getAllRagDocuments()

    val leadCount: Flow<Int> = dao.getLeadCount()
    val visitCount: Flow<Int> = dao.getVisitCount()
    val eventCount: Flow<Int> = dao.getEventCount()
    val newsCount: Flow<Int> = dao.getNewsCount()
    val ragDocumentCount: Flow<Int> = dao.getRagDocumentCount()

    suspend fun insertLead(lead: Lead): Long = dao.insertLead(lead)
    suspend fun updateLead(lead: Lead) = dao.updateLead(lead)
    suspend fun updateLeadStatus(id: Long, status: LeadStatus) = dao.updateLeadStatus(id, status)
    suspend fun deleteLead(lead: Lead) = dao.deleteLead(lead)

    suspend fun insertVisit(visit: CampusVisit): Long = dao.insertVisit(visit)
    suspend fun updateVisit(visit: CampusVisit) = dao.updateVisit(visit)
    suspend fun updateVisitStatus(id: Long, status: VisitStatus) = dao.updateVisitStatus(id, status)
    suspend fun deleteVisit(visit: CampusVisit) = dao.deleteVisit(visit)

    suspend fun insertEvent(event: CampusEvent): Long = dao.insertEvent(event)
    suspend fun updateEvent(event: CampusEvent) = dao.updateEvent(event)
    suspend fun deleteEvent(event: CampusEvent) = dao.deleteEvent(event)

    // News CRUD
    suspend fun insertNews(news: CampusNews): Long = dao.insertNews(news)
    suspend fun updateNews(news: CampusNews) = dao.updateNews(news)
    suspend fun deleteNews(news: CampusNews) = dao.deleteNews(news)

    // Event Registrations & Attendance
    fun getRegistrationsForEvent(eventId: Long): Flow<List<EventRegistration>> = dao.getRegistrationsForEvent(eventId)
    suspend fun insertRegistration(registration: EventRegistration): Long = dao.insertRegistration(registration)
    suspend fun updateRegistration(registration: EventRegistration) = dao.updateRegistration(registration)
    suspend fun deleteRegistration(registration: EventRegistration) = dao.deleteRegistration(registration)
    suspend fun updateAttendance(registrationId: Long, attended: Boolean) = dao.updateAttendance(registrationId, attended)

    suspend fun registerForEvent(
        event: CampusEvent,
        fullName: String,
        phone: String,
        email: String,
        school: String
    ): EventRegistration {
        val randomSuffix = (1000..9999).random()
        val ticketCode = "USTA-EVT-$randomSuffix"

        val reg = EventRegistration(
            eventId = event.id,
            eventTitle = event.title,
            fullName = fullName.trim(),
            phone = phone.trim(),
            email = email.trim(),
            schoolOrOrigin = school.trim(),
            attended = false,
            ticketCode = ticketCode
        )
        val id = dao.insertRegistration(reg)

        // Decrement available spots in real time
        val newSpots = (event.availableSpots - 1).coerceAtLeast(0)
        dao.updateEvent(event.copy(availableSpots = newSpots))

        // Also ensure lead exists in pipeline
        val lead = Lead(
            fullName = fullName.trim(),
            phone = phone.trim(),
            email = email.trim(),
            cityOrSchool = school.ifBlank { "Aspirante a Evento" },
            programInterest = event.category,
            status = LeadStatus.CONTACTADO,
            notes = "Inscrito al evento: ${event.title} (${event.dateBadge} - $ticketCode)"
        )
        dao.insertLead(lead)

        return reg.copy(id = id)
    }

    suspend fun getAllRagDocumentsList(): List<RagDocument> = dao.getAllRagDocumentsList()
    suspend fun insertRagDocument(doc: RagDocument): Long = dao.insertRagDocument(doc)
    suspend fun updateRagDocument(doc: RagDocument) = dao.updateRagDocument(doc)
    suspend fun deleteRagDocument(doc: RagDocument) = dao.deleteRagDocument(doc)

    suspend fun seedInitialDataIfEmpty() {
        val leadCnt = dao.getLeadCount().first()
        if (leadCnt == 0) {
            val initialLeads = listOf(
                Lead(
                    fullName = "Valentina Rojas Díaz",
                    phone = "3124567890",
                    email = "valentina.rojas@gmail.com",
                    cityOrSchool = "Colegio Boyacá de Tunja",
                    programInterest = "Ingeniería de Sistemas",
                    degreeLevel = "Pregrado",
                    status = LeadStatus.ADMITIDO,
                    notes = "Interesada en Inteligencia Artificial y Vibe Coding. Solicitó homologación SENA.",
                    createdAt = System.currentTimeMillis() - 86400000L * 4
                ),
                Lead(
                    fullName = "Sebastián Gómez Silva",
                    phone = "3178901234",
                    email = "sebastian.gomez@hotmail.com",
                    cityOrSchool = "INEM Carlos Arturo Torres",
                    programInterest = "Derecho",
                    degreeLevel = "Pregrado",
                    status = LeadStatus.MATRICULADO,
                    notes = "Matrícula financiera legalizada para Neos 2027. Recibo pagado.",
                    createdAt = System.currentTimeMillis() - 86400000L * 7
                ),
                Lead(
                    fullName = "Mariana Castro Prieto",
                    phone = "3156789012",
                    email = "mariana.castro@gmail.com",
                    cityOrSchool = "Escuela Normal Superior Santiago de Tunja",
                    programInterest = "Arquitectura",
                    degreeLevel = "Pregrado",
                    status = LeadStatus.DOCUMENTOS_PRESENTADOS,
                    notes = "Presentó acta de grado, resultado Saber 11 y formato de datos.",
                    createdAt = System.currentTimeMillis() - 86400000L * 2
                )
            )
            dao.insertLeads(initialLeads)

            val initialVisits = listOf(
                CampusVisit(
                    studentName = "Carlos Andrés Beltrán",
                    phone = "3143216549",
                    email = "carlos.beltran@gmail.com",
                    campus = "Campus Av. Universitaria",
                    dateText = "2026-10-15",
                    timeSlot = "10:00 AM - 12:00 PM",
                    programInterest = "Ingeniería de Datos e Inteligencia Artificial",
                    visitorCount = 3,
                    school = "Colegio de la Presentación",
                    status = VisitStatus.CONFIRMADA
                ),
                CampusVisit(
                    studentName = "Paula Andrea Medina",
                    phone = "3112345678",
                    email = "paula.medina@gmail.com",
                    campus = "Campus Centro Histórico",
                    dateText = "2026-10-16",
                    timeSlot = "02:00 PM - 04:00 PM",
                    programInterest = "Derecho",
                    visitorCount = 2,
                    school = "Colegio Salesiano Tunja",
                    status = VisitStatus.AGENDADA
                )
            )
            dao.insertVisits(initialVisits)
        }

        val evtCnt = dao.getEventCount().first()
        if (evtCnt == 0) {
            val initialEvents = listOf(
                CampusEvent(
                    title = "Open Day Santoto Tunja 2026 - 2027",
                    dateBadge = "18 OCT",
                    dateText = "2026-10-18",
                    time = "08:30 AM - 01:00 PM",
                    location = "Campus Av. Universitaria - Edificio Santo Domingo",
                    category = "Feria Universitaria",
                    description = "Vive la experiencia tomasina por un día: clases muestra, retos en laboratorios de IA y robótica, simulación de juicios en Derecho y talleres en vivo con directores.",
                    imageUrl = "https://images.unsplash.com/photo-1523240795612-9a054b0db644?w=800&auto=format&fit=crop&q=80",
                    capacity = 80,
                    availableSpots = 62,
                    isFeatured = true
                ),
                CampusEvent(
                    title = "Taller de Inmersión Microcredenciales Grado 11°",
                    dateBadge = "24 OCT",
                    dateText = "2026-10-24",
                    time = "02:00 PM - 05:00 PM",
                    location = "Campus Centro Histórico y Av. Universitaria",
                    category = "Colegios Grado 11",
                    description = "Aprende cómo adelantar créditos de tu carrera universitaria por solo $100.000 mientras cursas 11° y recibe certificación institucional homologable.",
                    imageUrl = "https://images.unsplash.com/photo-1531482615713-2afd69097998?w=800&auto=format&fit=crop&q=80",
                    capacity = 50,
                    availableSpots = 28,
                    isFeatured = true
                ),
                CampusEvent(
                    title = "Feria de Becas y Financiación Directa USTA",
                    dateBadge = "30 OCT",
                    dateText = "2026-10-30",
                    time = "09:00 AM - 04:00 PM",
                    location = "Plaza Central Santo Domingo",
                    category = "Admisiones y Financiación",
                    description = "Asesoría personalizada sobre descuentos por pronto pago hasta el 31 de octubre, convenios con el SENA, crédito directo sin codeudor y becas por promedio.",
                    imageUrl = "https://images.unsplash.com/photo-1541339907198-e08756dedf3f?w=800&auto=format&fit=crop&q=80",
                    capacity = 120,
                    availableSpots = 95,
                    isFeatured = true
                ),
                CampusEvent(
                    title = "Simposio de Inteligencia Artificial & Big Data Tunja",
                    dateBadge = "06 NOV",
                    dateText = "2026-11-06",
                    time = "03:00 PM - 06:00 PM",
                    location = "Auditorio Mayor Campus Av. Universitaria",
                    category = "Innovación & IA",
                    description = "Conferencias internacionales sobre aplicaciones de Inteligencia Artificial, agentes autónomos y análisis masivo de datos en Boyacá y el país.",
                    imageUrl = "https://images.unsplash.com/photo-1485827404703-89b55fcc595e?w=800&auto=format&fit=crop&q=80",
                    capacity = 90,
                    availableSpots = 44,
                    isFeatured = true
                )
            )
            dao.insertEvents(initialEvents)

            // Seed initial registrations for demoing check-in and attendance management
            val eventList = dao.getAllEvents().first()
            val openDayId = eventList.firstOrNull()?.id ?: 1L
            val initialRegistrations = listOf(
                EventRegistration(
                    eventId = openDayId,
                    eventTitle = "Open Day Santoto Tunja 2026 - 2027",
                    fullName = "Valentina Rojas Díaz",
                    phone = "3124567890",
                    email = "valentina.rojas@gmail.com",
                    schoolOrOrigin = "Colegio Boyacá de Tunja",
                    attended = false,
                    ticketCode = "USTA-EVT-7182"
                ),
                EventRegistration(
                    eventId = openDayId,
                    eventTitle = "Open Day Santoto Tunja 2026 - 2027",
                    fullName = "Juan David Salamanca",
                    phone = "3109876543",
                    email = "juan.salamanca@outlook.com",
                    schoolOrOrigin = "Colegio Salesiano Tunja",
                    attended = true,
                    ticketCode = "USTA-EVT-4910"
                ),
                EventRegistration(
                    eventId = openDayId,
                    eventTitle = "Open Day Santoto Tunja 2026 - 2027",
                    fullName = "Mariana Andrea Castro",
                    phone = "3156789012",
                    email = "mariana.castro@hotmail.com",
                    schoolOrOrigin = "Colegio de la Presentación",
                    attended = false,
                    ticketCode = "USTA-EVT-8321"
                )
            )
            initialRegistrations.forEach { dao.insertRegistration(it) }
        }

        // Seed initial Campus News (Noticias USTA)
        val newsCnt = dao.getNewsCount().first()
        if (newsCnt == 0) {
            val initialNews = listOf(
                CampusNews(
                    title = "Santoto Tunja abre admisiones 2026-2027 con descuentos especiales por pronto pago",
                    summary = "Aprovecha descuentos de hasta el 15% por pago oportuno hasta el 31 de octubre en los 22 programas de pregrado de la Seccional Tunja.",
                    content = """
La Universidad Santo Tomás Seccional Tunja abre oficialmente el calendario de inscripciones y matrículas para el periodo académico 2026-2027. Los aspirantes que completen su matrícula antes del 31 de octubre podrán acceder a tarifas preferenciales y descuentos del 10% y 15% según el programa.

Además, la institución ofrece financiación directa sin intermediarios bancarios ni codeudor, así como convenios de homologación con el SENA para tecnólogos que deseen profesionalizarse en menos tiempo. Para más información, comunícate con la Dirección de Admisiones al WhatsApp 317 510 5383.
                    """.trimIndent(),
                    category = "Admisiones",
                    dateText = "19 OCT 2026",
                    imageUrl = "https://images.unsplash.com/photo-1523050854058-8df90110c9f1?w=800&auto=format&fit=crop&q=80",
                    author = "Dirección de Admisiones & Marketing",
                    readTimeMinutes = 3,
                    isFeatured = true
                ),
                CampusNews(
                    title = "Microcredenciales para grado 11°: adelanta materias universitarias por solo $100.000",
                    summary = "Una oportunidad única para que estudiantes de último año escolar inicien su formación superior y obtengan créditos homologables oficiales.",
                    content = """
El innovador modelo de Microcredenciales de la Universidad Santo Tomás Tunja permite a los jóvenes de grado 11° cursar asignaturas introductorias de programas como Ingeniería de Sistemas, Derecho, Administración y Diseño de Interacción por un valor simbólico de $100.000 COP.

Al finalizar satisfactoriamente la microcredencial, el estudiante recibe una insignia digital y certificado con código de verificación que será homologado como crédito académico al matricularse en el pregrado correspondiente en la Santoto.
                    """.trimIndent(),
                    category = "Colegios",
                    dateText = "18 OCT 2026",
                    imageUrl = "https://images.unsplash.com/photo-1509062522246-3755977927d7?w=800&auto=format&fit=crop&q=80",
                    author = "Vicerrectoría Académica",
                    readTimeMinutes = 2,
                    isFeatured = true
                ),
                CampusNews(
                    title = "Semillero de Inteligencia Artificial de la Santoto gana primer puesto en reto tecnológico",
                    summary = "Estudiantes tomasinos presentaron una solución de visión computacional y modelos predictivos aplicada a la agricultura boyacense.",
                    content = """
El equipo de investigación de la Facultad de Ingeniería de Sistemas e Ingeniería de Datos de la Santoto Tunja se coronó campeón en el Hackathon Regional de Innovación 2026 con su proyecto 'AgroSense Tunja'.

La herramienta utiliza modelos de redes neuronales convolucionales y sensores IoT para predecir plagas en cultivos de papa y cebolla, demostrando la alta calidad formativa y pertinencia social de los ingenieros tomasinos.
                    """.trimIndent(),
                    category = "Innovación & IA",
                    dateText = "15 OCT 2026",
                    imageUrl = "https://images.unsplash.com/photo-1517245386807-bb43f82c33c4?w=800&auto=format&fit=crop&q=80",
                    author = "Facultad de Ingeniería",
                    readTimeMinutes = 4,
                    isFeatured = false
                ),
                CampusNews(
                    title = "Nuevo programa de Nutrición y Dietética recibe registro calificado del Ministerio",
                    summary = "La Seccional amplía su oferta en ciencias de la salud con un pregrado de 9 semestres enfocado en nutrición clínica y deportiva.",
                    content = """
Mediante la Resolución oficial del Ministerio de Educación Nacional, la Santoto Tunja suma a su catálogo académico el pregrado en Nutrición y Dietética. El programa cuenta con modernos laboratorios de evaluación nutricional, biofísica y bromatología en el Campus Av. Universitaria.

Las inscripciones ya se encuentran habilitadas a través del portal SAC y la oficina de admisiones.
                    """.trimIndent(),
                    category = "Académico",
                    dateText = "12 OCT 2026",
                    imageUrl = "https://images.unsplash.com/photo-1498837167922-ddd27525d352?w=800&auto=format&fit=crop&q=80",
                    author = "Comunicaciones USTA",
                    readTimeMinutes = 3,
                    isFeatured = false
                )
            )
            dao.insertNewsList(initialNews)
        }

        // Seed RAG Markdown Documents from user's verified files
        val ragCnt = dao.getRagDocumentCount().first()
        if (ragCnt == 0) {
            val initialDocs = listOf(
                RagDocument(
                    title = "Pregrado.md",
                    category = "Pregrados",
                    content = """
# Oferta Oficial de Pregrados Santoto Tunja
Fuente Oficial: Información programas(Pregrado).csv

1. Arquitectura: Título Arquitecto. SNIES 5456. Inversión 2026 hasta 31 oct: $ 7.107.000 (Neos 2027: $ 7.314.000). 10 semestres. Presencial. 23 años de historia, salas de sistemas avanzadas. Homologación SENA dibujo y construcción.
2. Bioingeniería: Título Bioingeniero. SNIES 116050. Inversión: $ 5.232.000 (Neos 2027: $ 5.385.000). 8 semestres. Presencial. Integra ingeniería con medicina, biología y salud. Doble programa con Ing. Electrónica, Mecánica y Cultura Física.
3. Ingeniería Ambiental: Título Ingeniero Ambiental. SNIES 109663. Inversión: $ 5.850.000. 8 semestres. Presencial. Mitigación de contaminación en agua, aire, suelo y cambio climático. Doble titulación con Univ. Quintana Roo (México).
4. Ingeniería Civil: Título Ingeniero Civil. SNIES 7750. Acreditación de Alta Calidad (6 años). Inversión: $ 6.989.700. 8 semestres. Presencial. Más de 25 años de trayectoria. Laboratorios en estructuras, vías, geotecnia, recursos hídricos.
5. Ingeniería de Datos e Inteligencia Artificial: Título Ingeniero de Datos e Inteligencia Artificial. SNIES 116280. Inversión: $ 6.541.000 (Neos 2027: $ 6.731.000). 8 semestres. Presencial. IA, Big Data, Machine Learning, IoT. Doble programa con Ing. Electrónica.
6. Ingeniería de Sistemas: Título Ingeniero de Sistemas. SNIES 53540. Inversión: $ 6.252.000 (Neos 2027: $ 6.434.000). 8 semestres. Modalidad Híbrida. Full Stack, Cloud, DevOps, IA Generativa, Vibe Coding, n8n, ciberseguridad. Homologación SENA.
7. Virtual - Ingeniería de Sistemas: Título Ingeniero de Sistemas. SNIES 119237. Inversión: $ 4.027.000 (Neos 2027: $ 4.145.000). 8 semestres. 100% Virtual.
8. Ingeniería Electrónica: Título Ingeniero Electrónico. SNIES 7741. Acreditación de Alta Calidad. Inversión: $ 5.974.000. 10 semestres. Presencial. Robótica, IoT, redes inalámbricas, energías limpias y drones.
9. Ingeniería Industrial: Título Ingeniero Industrial. SNIES 106920. Inversión: $ 5.572.000 (Neos 2027: $ 5.735.000). 8 semestres. Presencial. Mejoramiento de procesos, laboratorio de métodos industriales, FlexSim, SolidWorks.
10. Ingeniería Mecánica: Título Ingeniero Mecánico. SNIES 106947. Inversión: $ 6.273.000 (Neos 2027: $ 6.455.000). 8 semestres. Presencial. Patentes, energías renovables, automotriz, aeroespacial, industria oil & gas.
11. Derecho: Título Abogado. SNIES 9144. Acreditación de Alta Calidad. Inversión: $ 6.813.000 (Neos 2027: $ 6.945.750). 10 semestres. Presencial. Sistema modular, formación humanista y consultorio jurídico.
12. Administración de Empresas: Título Administrador de Empresas. SNIES 19388. Acreditación de Alta Calidad Multicampus. Inversión: $ 4.584.000 (Neos 2027: $ 4.717.000). 8 semestres. Presencial. Doble titulación con Univ. de Colima (México).
13. Contaduría Pública: Título Contador Público. SNIES 101613. Inversión: $ 4.210.000. 8 semestres. Presencial. Microcertificación gratis en SIIGO Nube y Helissa NIIF. Doble titulación con Univ. Veracruzana (México). Prácticas con DIAN, CGN y PWC.
14. Marketing y Transformación Digital: Título Profesional en Marketing y Transformación Digital. SNIES 116961. Inversión: $ 5.356.000 (Neos 2027: $ 5.512.000). 8 semestres. Combinada (Presencial-Virtual). Analítica, Growth Hacking, automatización.
15. Negocios Internacionales: Título Profesional en Negocios Internacionales. SNIES 101838. Inversión: $ 5.171.000 (Neos 2027: $ 5.321.000). 8 semestres. Presencial. Trading, logística internacional, talleres de Mandarín, Francés y Alemán. Doble titulación con BUAP.
16. Virtual - Negocios Internacionales: Título Profesional en Negocios Internacionales. SNIES 101838. Inversión: $ 3.966.000 (Neos 2027: $ 4.081.000). 8 semestres. Virtual.
17. Inteligencia de Negocios: Título Profesional en Inteligencia de Negocios. Resolución 018423 del MEN (7 años). Inversión: Pendiente por definir. 8 semestres. Modalidad Híbrida (50.4% virtual). Analítica de datos, big data y visualización estratégica.
18. Licenciatura en Español y Lenguas Extranjeras Inglés y Francés: Título Licenciado en Español y Lenguas Extranjeras. SNIES 109963. Inversión: $ 4.151.000 (Neos 2027: $ 4.272.000). 10 semestres. Presencial. Certificación trilingüe nivel C1.
19. Diseño de Interacción: Título Profesional en Diseño de Interacción. SNIES 109495. Inversión: $ 5.678.000 (Neos 2027: $ 5.844.000). 8 semestres. Presencial. UI/UX, interfaces digitales, videojuegos, realidad virtual. Doble titulación con Diseño Gráfico Bogotá.
20. Licenciatura en Educación Infantil Bilingüe: Título Licenciado/a en Educación Bilingüe. SNIES 118409. Inversión: $ 4.163.000 (Neos 2027: $ 4.285.000). 9 semestres. Presencial. Primera infancia bilingüe con certificación inglés B2.
21. Cultura Física, Deporte y Recreación: Título Profesional en Cultura Física, Deporte y Recreación. SNIES 106756. Inversión: $ 3.966.000. 9 semestres. Presencial. El mejor campus deportivo de la región, fisiología del ejercicio. Doble titulación UABC México.
22. Nutrición y Dietética: Título Nutricionista y Dietista. SNIES 119193. Resolución 009134 de abril 2026. Inversión: Por definir. 9 semestres. Presencial. Nutrición clínica, pública, deportiva y empresarial.
                    """.trimIndent()
                ),
                RagDocument(
                    title = "Directorio Admisiones.md",
                    category = "Directorio",
                    content = """
# Directorio Oficial de Admisiones y Canales de Atención
- Sede Tunja | Admisiones y Marketing | Celular / WhatsApp: 317 510 5383 (https://wa.me/573175105383)
- Sede Tunja | Admisiones y Marketing | Celular / WhatsApp: 317 514 1479 (https://wa.me/573175141479)
- Correo Oficial Admisiones Tunja: admisiones@ustatunja.edu.co
- Portal Web Admisiones Tunja: https://santototunja.edu.co/admisiones
- Portal de Inscripciones en Línea SAC: https://sac.usantotomas.edu.co/sgacampus/services/inscripciones/home
- Recorrido Virtual Oficial 360°: https://360.santototunja.edu.co/
                    """.trimIndent()
                ),
                RagDocument(
                    title = "Microcredenciales Grado 11.md",
                    category = "Microcredenciales",
                    content = """
# Microcredenciales para Estudiantes de Grado 11°
Inversión: $ 100.000 por microcredencial. 1 semestre de duración. Homologable directamente a la carrera universitaria en la Santoto Tunja si la nota promedio es >= 3.5.

1. Arquitectura: Expresión I: Representación y Comunicación de una Idea ($100.000)
2. Bioingeniería: Introducción a la Bioingeniería ($100.000)
3. Ingeniería Civil: Introducción a la Ingeniería ($100.000)
4. Ingeniería de Datos e IA: Introducción a la Ingeniería de Datos e IA ($100.000)
5. Ingeniería de Sistemas: Programación Estructurada para Ingenierías ($100.000)
6. Ingeniería Electrónica: Introducción a la Ingeniería Electrónica ($100.000)
7. Ingeniería Industrial: Gestión Organizacional ($100.000)
8. Derecho: Historia del Derecho y de las Instituciones Jurídicas ($100.000)
9. Derecho: Sociología Jurídica ($100.000)
10. Administración de Empresas: Pensamiento Administrativo ($100.000)
11. Contaduría Pública: Contabilidad Financiera Básica ($100.000)
12. Marketing y Transformación Digital: Pensamiento Creativo ($100.000)
13. Negocios Internacionales: Protocolo y Etiqueta de Alta Dirección ($100.000)
14. Diseño de Interacción: Expresión 1 ($100.000)
15. Licenciatura en Lenguas: Modelos Pedagógicos
16. Cultura Física: Pedagogía de la Cultura Física ($100.000)
17. Ciencias Básicas: Matemática I ($100.000)
18. Humanidades: Persona Humana, Sociedad y Conocimiento ($100.000)
                    """.trimIndent()
                ),
                RagDocument(
                    title = "Requisitos Pregrado.md",
                    category = "Requisitos",
                    content = """
# Requisitos de Admisión Pregrado Santoto Tunja
1. Inscripción Normal:
- Fotocopia de documento de identidad legible.
- Formato de verificación y tratamiento de datos personales diligenciado (https://hubs.ly/Q02T1sR00).
- Foto tamaño 3x4 a color fondo blanco (.jpg o .png).
- Resultado examen de Estado Saber 11.
- Fotocopias de diploma y acta de grado de bachiller.
- Certificado de afiliación a EPS vigente.

2. Convenio SENA (Homologación directa):
- Documentos de inscripción normal.
- Diploma y acta de grado del SENA.
- Contenidos programáticos del SENA por competencias y certificado de notas.
- Enviar documentos a: admisiones@ustatunja.edu.co con asunto: Documentos Homologación - Nombre - Documento - Carrera.

3. Transferencia Interna y Externa:
- Certificado de notas y contenidos programáticos de la universidad de origen. Paz y salvo de la sede de origen.
                    """.trimIndent()
                ),
                RagDocument(
                    title = "Posgrado.md",
                    category = "Posgrados",
                    content = """
# Oferta de Posgrados Santoto Tunja (Especializaciones, Maestrías y Doctorados)
Especializaciones:
- Estructuras ($7.980.000) y Virtual ($7.600.000)
- Geotecnia Vial y Pavimentos ($7.970.000) y Virtual ($7.870.000)
- Gerencia de Proyectos de Construcción ($7.950.000)
- Gestión de Nuevas Tecnologías de Telecomunicaciones ($5.100.000 virtual)
- Gerencia de Mantenimiento y Gestión de Activos ($5.900.000 presencial / $5.750.000 virtual)
- Ingeniería Hidroambiental ($6.760.000)
- Gerencia de la Cadena de Valor y Productividad ($4.700.000 virtual)
- Ciencia de Datos ($5.950.000 virtual)
- Construcción Sostenible (Virtual)
- Derecho Penal y Procesal Penal ($7.600.000)
- Derecho Administrativo ($7.740.000)
- Contratación Estatal ($7.280.000)
- Gobierno y Políticas Públicas ($6.200.000 virtual)
- Auditoría de Salud ($7.550.000)
- Auditoría y Gestión de Riesgos ($6.200.000)
- Gobierno y Gestión Territorial ($7.800.000)
- Innovación y Marketing ($6.100.000)
- Gerencia Estratégica de Costos ($3.970.000 virtual)
- Liderazgo e Innovación Educativa ($5.250.000 virtual)

Maestrías:
- Geotecnia Vial y Pavimentos ($8.750.000 presencial / $8.650.000 virtual)
- Maestría en Ingeniería ($7.290.000)
- Ingeniería Civil con Énfasis en Hidroambiental ($8.400.000)
- Sostenibilidad Ambiental ($6.800.000 virtual)
- Arquitectura Avanzada ($7.900.000)
- Liderazgo y Gestión Estratégica de la Transformación Digital ($6.200.000 virtual)
- Derecho Penal y Procesal Penal ($9.000.000)
- Derecho Administrativo ($9.200.000)
- Derecho Privado ($8.900.000 presencial / $8.700.000 virtual)
- Administración ($7.500.000)
- Marketing Internacional y Negocios ($6.700.000 presencial / $6.520.000 virtual)
- Inclusión, Educación y Diversidad ($5.400.000 virtual)
- Didáctica de las Lenguas Extranjeras ($5.675.000 virtual)
- Entrenamiento Deportivo y Actividad Física ($5.700.000)

Doctorados:
- Doctorado en Sostenibilidad ($14.800.000)
- Doctorado en Derecho Público ($11.100.000 presencial / $9.900.000 virtual)
- Doctorado en Pedagogía y Neurociencia Aplicada a la Educación ($10.190.000)
                    """.trimIndent()
                ),
                RagDocument(
                    title = "Resumen Inversion.md",
                    category = "Inversión",
                    content = """
# Tabla Oficial de Inversión Semestral 2026 - USTA Tunja
Pregrados:
- Arquitectura: $ 7.107.000
- Bioingeniería: $ 5.232.000
- Ingeniería Ambiental: $ 5.850.000
- Ingeniería Civil: $ 6.989.700
- Ingeniería de Datos e IA: $ 6.541.000
- Ingeniería de Sistemas: $ 6.252.000 (Virtual: $ 4.027.000)
- Ingeniería Electrónica: $ 5.974.000
- Ingeniería Industrial: $ 5.572.000
- Ingeniería Mecánica: $ 6.273.000
- Derecho: $ 6.813.000
- Administración de Empresas: $ 4.584.000
- Contaduría Pública: $ 4.210.000
- Marketing y Transformación Digital: $ 5.356.000
- Negocios Internacionales: $ 5.171.000 (Virtual: $ 3.966.000)
- Licenciatura en Lenguas: $ 4.151.000
- Diseño de Interacción: $ 5.678.000
- Licenciatura en Educación Infantil Bilingüe: $ 4.163.000
- Cultura Física, Deporte y Recreación: $ 3.966.000
- Microcredenciales Grado 11°: $ 100.000
                    """.trimIndent()
                )
            )
            dao.insertRagDocuments(initialDocs)
        }
    }

    suspend fun resetDefaultRagDocuments() {
        dao.deleteAllRagDocuments()
        seedInitialDataIfEmpty()
    }
}
