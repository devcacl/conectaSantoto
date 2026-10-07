package com.example.data.rag

import com.example.data.model.CampusTourLocation
import com.example.data.model.TourHotspot

object CampusTourData {

    val LOCATIONS: List<CampusTourLocation> = listOf(
        CampusTourLocation(
            id = "av_universitaria",
            title = "Campus Av. Universitaria",
            subtitle = "Edificio Santo Domingo de Guzmán",
            campusName = "Campus Principal Norte",
            description = "El campus moderno más completo de la región con más de 30.000 m² de zonas verdes, laboratorios de ingeniería, auditorios y biblioteca central.",
            primaryColorHex = 0xFF002B49,
            secondaryColorHex = 0xFF0072CE,
            tags = listOf("Aulas Inteligentes", "Auditorios", "Biblioteca", "Zonas Verdes"),
            highlights = listOf(
                "Edificio Santo Domingo de Guzmán con tecnología bioclimática",
                "Salas de sistemas equipadas con software de arquitectura e ingeniería",
                "Cafetería central y espacios de coworking tomasino",
                "Conectividad Wi-Fi 6 en todo el campus"
            ),
            hotspots = listOf(
                TourHotspot("Entrada Principal", "Acceso peatonal y vehicular con torniquetes biométricos", 20f),
                TourHotspot("Plaza Santo Domingo", "Punto neurálgico de encuentro cultural y eventos universitarios", 110f),
                TourHotspot("Bloque de Ingenierías", "Edificio de 5 pisos con laboratorios y salas de simulación", 230f),
                TourHotspot("Biblioteca Fray Alberto Montealegre", "Colección bibliográfica de más de 40.000 volúmenes físicos y digitales", 310f)
            )
        ),
        CampusTourLocation(
            id = "centro_historico",
            title = "Campus Centro Histórico",
            subtitle = "Convento San Alberto Magno",
            campusName = "Sede Tradicional Centro",
            description = "Joya arquitectónica en el corazón de Tunja. Combina claustros coloniales declarados patrimonio con modernos auditorios y salas de audiencias para Derecho y Ciencias Administrativas.",
            primaryColorHex = 0xFF7A1C16,
            secondaryColorHex = 0xFFD99B00,
            tags = listOf("Patrimonio Colonial", "Consultorio Jurídico", "Salas de Audiencias", "Centro Tunja"),
            highlights = listOf(
                "Claustro colonial con arcos de cantería y jardín interior",
                "Sala de audiencias orales con tecnología de grabación judicial",
                "Consultorio Jurídico con atención a la comunidad",
                "Sede de posgrados y alta gerencia"
            ),
            hotspots = listOf(
                TourHotspot("Claustro Central", "Patios históricos y arquería de estilo barroco tomasino", 45f),
                TourHotspot("Sala de Audiencias", "Espacio simulado para prácticas de litigio oral y mediación", 140f),
                TourHotspot("Consultorio Jurídico", "Atención profesional en asesoría legal gratuita", 250f),
                TourHotspot("Capilla de San Alberto Magno", "Espacio de espiritualidad, silencio y conciertos corales", 330f)
            )
        ),
        CampusTourLocation(
            id = "laboratorios_ia",
            title = "Centro de Datos, IA & Robótica",
            subtitle = "Edificio Giordano Bruno",
            campusName = "Campus Av. Universitaria",
            description = "Ecosistema de investigación aplicada con clusters de cómputo para Inteligencia Artificial, servidores de Big Data, robótica industrial y electrónica.",
            primaryColorHex = 0xFF0D47A1,
            secondaryColorHex = 0xFF00E5FF,
            tags = listOf("Inteligencia Artificial", "Big Data", "Robótica", "Drones"),
            highlights = listOf(
                "Sala Móvil equipada para hackathons y Vibe Coding",
                "Laboratorio de Inteligencia Artificial y Visión Artificial",
                "Laboratorio de Digitales, Microcontroladores y Redes Móviles",
                "Banco de pruebas de robótica industrial y drones de inspección"
            ),
            hotspots = listOf(
                TourHotspot("Laboratorio de IA", "Estaciones de trabajo de alto rendimiento para entrenamiento de modelos", 60f),
                TourHotspot("Taller de Prototipado y Robótica", "Brazos robóticos industriales e impresoras 3D", 175f),
                TourHotspot("Sala de Redes y Telecomunicaciones", "Racks de fibra óptica y switches gestionables", 290f)
            )
        ),
        CampusTourLocation(
            id = "complejo_deportivo",
            title = "Complejo Deportivo & Gimnasio",
            subtitle = "Campus Deportivo Universitario",
            campusName = "Campus Av. Universitaria",
            description = "El mejor complejo deportivo de las universidades boyacenses, con canchas reglamentarias de fútbol, baloncesto, voleibol, pista y laboratorios de fisiología del ejercicio.",
            primaryColorHex = 0xFF1B5E20,
            secondaryColorHex = 0xFFFFC72C,
            tags = listOf("Canchas Múltiples", "Gimnasio", "Fisiología", "Pista Atlética"),
            highlights = listOf(
                "Cancha de fútbol 11 y fútbol sala con medidas reglamentarias",
                "Laboratorio de Morfofisiología y Análisis Sensorial",
                "Gimnasio universitario con pesas libres y máquinas cardiovasculares",
                "Zonas de calentamiento y recuperación muscular"
            ),
            hotspots = listOf(
                TourHotspot("Cancha Principal", "Escenario de torneos universitarios ASCUN y recreación", 30f),
                TourHotspot("Gimnasio Fitness", "Equipamiento de fuerza y acondicionamiento aeróbico", 150f),
                TourHotspot("Laboratorio de Esfuerzo", "Cicloergómetros y analizadores de gases para atletas", 270f)
            )
        )
    )
}
