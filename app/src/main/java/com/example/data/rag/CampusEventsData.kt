package com.example.data.rag

data class CampusEvent(
    val id: String,
    val title: String,
    val dateBadge: String,
    val time: String,
    val location: String,
    val category: String,
    val description: String,
    val isFeatured: Boolean = false
)

object CampusEventsData {
    val EVENTS = listOf(
        CampusEvent(
            id = "event_1",
            title = "Open Day Santoto Tunja 2026-2027",
            dateBadge = "18 OCT",
            time = "08:30 AM - 01:00 PM",
            location = "Campus Av. Universitaria - Edificio Santo Domingo",
            category = "Feria Universitaria",
            description = "Vive la experiencia tomasina por un día: clases muestra, retos en laboratorios de IA y robótica, simulación de juicios en Derecho y talleres en vivo.",
            isFeatured = true
        ),
        CampusEvent(
            id = "event_2",
            title = "Taller de Inmersión Microcredenciales Grado 11",
            dateBadge = "24 OCT",
            time = "02:00 PM - 05:00 PM",
            location = "Campus Centro Histórico y Av. Universitaria",
            category = "Colegios Grado 11",
            description = "Aprende cómo adelantar créditos de tu carrera universitaria por solo $100.000 mientras cursas 11° y recibe certificación institucional homologable.",
            isFeatured = true
        ),
        CampusEvent(
            id = "event_3",
            title = "Feria de Becas y Financiación Directa USTA",
            dateBadge = "30 OCT",
            time = "09:00 AM - 04:00 PM",
            location = "Plaza Central Santo Domingo",
            category = "Admisiones y Financiación",
            description = "Asesoría personalizada sobre descuentos por pronto pago hasta el 31 de octubre, convenios con el SENA, crédito directo sin codeudor y becas por promedio."
        ),
        CampusEvent(
            id = "event_4",
            title = "Webinar Internacional: Tendencias en IA y Big Data",
            dateBadge = "05 NOV",
            time = "05:00 PM - 07:00 PM",
            location = "Virtual (Transmisión Microsoft Teams)",
            category = "Académico",
            description = "Docentes y expertos de la Facultad de Ingeniería de Sistemas y Datos explican el mercado laboral global para los nuevos ingenieros de la Santoto."
        )
    )
}
