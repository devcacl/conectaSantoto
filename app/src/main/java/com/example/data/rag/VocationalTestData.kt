package com.example.data.rag

import com.example.data.model.FacultyCategory
import com.example.data.model.TestOption
import com.example.data.model.VocationalQuestion
import com.example.data.model.VocationalResult

object VocationalTestData {

    val QUESTIONS: List<VocationalQuestion> = listOf(
        VocationalQuestion(
            id = 1,
            title = "¿Qué tipo de actividades disfrutas más en tu tiempo libre?",
            categoryDescription = "Intereses personales y hobbies",
            options = listOf(
                TestOption("Diseñar planos, experimentar con tecnología, programar o armar dispositivos", FacultyCategory.INGENIERIAS_Y_ARQUITECTURA),
                TestOption("Analizar noticias de negocios, idear emprendimientos o planear presupuestos", FacultyCategory.CIENCIAS_ECONOMICAS_Y_ADMINISTRATIVAS),
                TestOption("Debatir sobre justicia, derechos ciudadanos, leyes y resolución de conflictos", FacultyCategory.CIENCIAS_JURIDICAS_Y_POLITICAS),
                TestOption("Entrenar, practicar deportes, cuidar la nutrición y entender el cuerpo humano", FacultyCategory.CIENCIAS_DE_LA_SALUD_Y_DEPORTE),
                TestOption("Aprender idiomas, leer literatura, enseñar a otros y filosofar sobre la sociedad", FacultyCategory.EDUCACION_Y_HUMANIDADES)
            )
        ),
        VocationalQuestion(
            id = 2,
            title = "Frente a un problema en tu comunidad, ¿cómo preferirías aportar?",
            categoryDescription = "Impacto social y resolución de problemas",
            options = listOf(
                TestOption("Construyendo infraestructura sustentable o creando una aplicación digital", FacultyCategory.INGENIERIAS_Y_ARQUITECTURA),
                TestOption("Creando una empresa que genere empleo y optimice el comercio local", FacultyCategory.CIENCIAS_ECONOMICAS_Y_ADMINISTRATIVAS),
                TestOption("Defendiendo legalmente a los afectados y proponiendo políticas públicas justas", FacultyCategory.CIENCIAS_JURIDICAS_Y_POLITICAS),
                TestOption("Promoviendo hábitos saludables, actividad física y programas de alimentación", FacultyCategory.CIENCIAS_DE_LA_SALUD_Y_DEPORTE),
                TestOption("Capacitando a niños y jóvenes en valores, educación bilingüe y pensamiento crítico", FacultyCategory.EDUCACION_Y_HUMANIDADES)
            )
        ),
        VocationalQuestion(
            id = 3,
            title = "¿Cuál de estas herramientas te despierta mayor curiosidad?",
            categoryDescription = "Habilidades técnicas e instrumentos",
            options = listOf(
                TestOption("Laboratorios de robótica, software de simulación 3D e inteligencia artificial", FacultyCategory.INGENIERIAS_Y_ARQUITECTURA),
                TestOption("Dashboards de analítica financiera, plataformas de marketing digital y trading", FacultyCategory.CIENCIAS_ECONOMICAS_Y_ADMINISTRATIVAS),
                TestOption("Códigos de leyes, jurisprudencia, salas de audiencias y mediación penal", FacultyCategory.CIENCIAS_JURIDICAS_Y_POLITICAS),
                TestOption("Equipos de fisiología del ejercicio, morfología y evaluación nutricional", FacultyCategory.CIENCIAS_DE_LA_SALUD_Y_DEPORTE),
                TestOption("Laboratorios de fonética, libros en múltiples idiomas y recursos pedagógicos", FacultyCategory.EDUCACION_Y_HUMANIDADES)
            )
        ),
        VocationalQuestion(
            id = 4,
            title = "¿En qué ambiente de trabajo te imaginas en 5 años?",
            categoryDescription = "Proyección profesional",
            options = listOf(
                TestOption("En una empresa de software, constructora de obras o centro de I+D tecnológico", FacultyCategory.INGENIERIAS_Y_ARQUITECTURA),
                TestOption("En corporaciones multinacionales, firmas de auditoría o liderando mi propia startup", FacultyCategory.CIENCIAS_ECONOMICAS_Y_ADMINISTRATIVAS),
                TestOption("En juzgados, cortes, despachos jurídicos u organismos internacionales de DD.HH.", FacultyCategory.CIENCIAS_JURIDICAS_Y_POLITICAS),
                TestOption("En complejos deportivos de alto rendimiento, clínicas o entidades de salud pública", FacultyCategory.CIENCIAS_DE_LA_SALUD_Y_DEPORTE),
                TestOption("En colegios internacionales, universidades o centros de investigación lingüística", FacultyCategory.EDUCACION_Y_HUMANIDADES)
            )
        ),
        VocationalQuestion(
            id = 5,
            title = "¿Qué tipo de reto intelectual te motiva más?",
            categoryDescription = "Estilo de razonamiento",
            options = listOf(
                TestOption("Optimizar un algoritmo o diseñar una estructura sismorresistente", FacultyCategory.INGENIERIAS_Y_ARQUITECTURA),
                TestOption("Diseñar una campaña de marketing global o analizar la rentabilidad de un proyecto", FacultyCategory.CIENCIAS_ECONOMICAS_Y_ADMINISTRATIVAS),
                TestOption("Argumentar un caso complejo para demostrar inocencia o cumplimiento normativo", FacultyCategory.CIENCIAS_JURIDICAS_Y_POLITICAS),
                TestOption("Planificar el ciclo de entrenamiento de un atleta o diseñar un plan dietoterapéutico", FacultyCategory.CIENCIAS_DE_LA_SALUD_Y_DEPORTE),
                TestOption("Enseñar a un niño a leer en dos idiomas o entender la historia del pensamiento", FacultyCategory.EDUCACION_Y_HUMANIDADES)
            )
        )
    )

    fun calculateResult(answers: List<FacultyCategory>): VocationalResult {
        val scores = mutableMapOf<FacultyCategory, Int>()
        FacultyCategory.values().forEach { scores[it] = 0 }
        answers.forEach { faculty ->
            scores[faculty] = (scores[faculty] ?: 0) + 1
        }

        val topFaculty = scores.maxByOrNull { it.value }?.key ?: FacultyCategory.INGENIERIAS_Y_ARQUITECTURA

        val recommendations = when (topFaculty) {
            FacultyCategory.INGENIERIAS_Y_ARQUITECTURA -> listOf(
                "Ingeniería de Sistemas (Híbrida o Virtual)",
                "Ingeniería de Datos e Inteligencia Artificial",
                "Arquitectura",
                "Ingeniería Civil",
                "Bioingeniería",
                "Diseño de Interacción",
                "Ingeniería Industrial"
            )
            FacultyCategory.CIENCIAS_ECONOMICAS_Y_ADMINISTRATIVAS -> listOf(
                "Administración de Empresas",
                "Contaduría Pública",
                "Marketing y Transformación Digital",
                "Negocios Internacionales (Presencial o Virtual)",
                "Inteligencia de Negocios"
            )
            FacultyCategory.CIENCIAS_JURIDICAS_Y_POLITICAS -> listOf(
                "Derecho (Acreditación de Alta Calidad)",
                "Especializaciones en Derecho Penal y Administrativo",
                "Contratación Estatal"
            )
            FacultyCategory.CIENCIAS_DE_LA_SALUD_Y_DEPORTE -> listOf(
                "Cultura Física, Deporte y Recreación",
                "Nutrición y Dietética"
            )
            FacultyCategory.EDUCACION_Y_HUMANIDADES -> listOf(
                "Licenciatura en Español y Lenguas Extranjeras (Inglés - Francés)",
                "Licenciatura en Educación Infantil Bilingüe"
            )
        }

        val profileDescription = when (topFaculty) {
            FacultyCategory.INGENIERIAS_Y_ARQUITECTURA ->
                "Tu perfil destaca por el pensamiento lógico, la innovación técnica y la resolución de problemas espaciales y analíticos. Tienes alta vocación para transformar el mundo con ciencia, software y diseño de infraestructuras."
            FacultyCategory.CIENCIAS_ECONOMICAS_Y_ADMINISTRATIVAS ->
                "Posees visión estratégica, liderazgo comercial y capacidad para articular proyectos de negocio rentables y sostenibles a escala nacional e internacional."
            FacultyCategory.CIENCIAS_JURIDICAS_Y_POLITICAS ->
                "Demuestras un alto sentido de justicia, ética humanista, facilidad verbal y pasión por defender los derechos de las personas y el orden social."
            FacultyCategory.CIENCIAS_DE_LA_SALUD_Y_DEPORTE ->
                "Tienes vocación por el bienestar corporal, la salud integral, el entrenamiento de alto rendimiento y el cuidado nutricional de las comunidades."
            FacultyCategory.EDUCACION_Y_HUMANIDADES ->
                "Tu vocación radica en la pedagogía transformadora, la comunicación multicultural bilingüe y la formación de nuevas generaciones con valores y criterio."
        }

        return VocationalResult(
            topFaculty = topFaculty,
            facultyScores = scores,
            recommendedPrograms = recommendations,
            personalityProfile = profileDescription
        )
    }
}
