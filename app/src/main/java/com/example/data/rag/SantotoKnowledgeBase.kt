package com.example.data.rag

import com.example.data.model.AcademicProgram
import com.example.data.model.FacultyCategory
import com.example.data.model.ProgramLevel

object SantotoKnowledgeBase {

    val DIRECTORY_TUNJA = listOf(
        "Sede - Seccional Tunja | Admisiones y marketing | Celular: 317 510 5383",
        "Sede - Seccional Tunja | Admisiones y marketing | Celular: 317 514 1479",
        "Correo Oficial de Admisiones Tunja: admisiones@ustatunja.edu.co",
        "Sede Principal Bogotá | Admisiones pregrado: 314 372 9654 | Posgrado: 313 204 8451",
        "Sede Bucaramanga | Admisiones Posgrado: 312 531 1525 | Mercadeo: 314 218 8178",
        "Sede Medellín | Pregrado y Posgrado: 318 883 2762",
        "Sede Villavicencio | Pregrado: 315 648 9750 | Posgrado: 318 475 6107"
    )

    val REQUISITOS_INFO = mapOf(
        "Inscripción Normal" to """
            Requisitos de Inscripción Normal (Pregrado):
            1. Fotocopia del documento de identidad legible.
            2. Formato de verificación y tratamiento de datos personales debidamente diligenciado (https://hubs.ly/Q02T1sR00).
            3. Foto tamaño 3x4 a color, fondo blanco (.jpg o .png).
            4. Resultado del Examen de Estado Saber 11.
            5. Fotocopias de diploma y acta de grado de bachiller.
            6. Certificado de afiliación a EPS.
            Nota: Durante la inscripción se solicita contrato de matrícula; no debes cargarlo allí, la Dirección de Admisiones lo adjuntará una vez culmines tu proceso.
        """.trimIndent(),
        "Convenio SENA" to """
            Requisitos Inscripción Convenio SENA (Homologación directa):
            1. Documento de identidad legible.
            2. Formato de verificación y tratamiento de datos diligenciado y firmado.
            3. Foto tamaño 3x4 color, fondo blanco (.jpg o .png).
            4. Resultado Examen Saber 11.
            5. Diploma y acta de bachiller.
            6. Certificado EPS.
            7. Diploma y acta de grado del SENA.
            8. Contenidos programáticos del SENA por competencias.
            9. Certificado de notas del SENA por competencias.
            Enviar documentos del SENA al correo: admisiones@ustatunja.edu.co con asunto: Documentos Homologación - Nombre aspirante - N° Documento - Programa académico.
        """.trimIndent(),
        "Transferencia Interna" to """
            Requisitos Transferencia Interna (Desde otra sede o seccional Santoto):
            1. Documento de identidad legible.
            2. Formato de verificación y tratamiento de datos personales.
            3. Foto 3x4 a color, fondo blanco.
            4. Resultado Saber 11.
            5. Diploma y acta de bachiller.
            6. Certificado de afiliación a EPS.
            7. Carta de aprobación de transferencia interna emitida por la facultad de origen.
            8. Histórico de notas de los semestres cursados (SAC).
            9. Paz y Salvo financiero de la sede de origen.
        """.trimIndent(),
        "Transferencia Externa" to """
            Requisitos Transferencia Externa (Desde otra Universidad/IES):
            1. Documento de identidad legible.
            2. Formato de tratamiento de datos personales.
            3. Foto 3x4 fondo blanco.
            4. Resultado Saber 11.
            5. Diploma y acta de grado de bachiller.
            6. Certificado EPS.
            7. Histórico de notas de los semestres cursados en la universidad de origen.
            8. Contenido programático del programa de origen.
            Enviar notas y contenidos a: admisiones@ustatunja.edu.co (Asunto: Documentos Homologación - Nombre - Documento - Programa).
        """.trimIndent(),
        "Doble Programa" to """
            Requisitos Doble Programa:
            1. Documento de identidad legible.
            2. Formato de verificación y tratamiento de datos.
            3. Foto 3x4 a color.
            4. Resultado Saber 11.
            5. Diploma y acta de bachiller.
            6. Certificado EPS.
            7. Carta de aprobación doble programa emitida por la facultad del programa base.
            8. Histórico de notas cursados (SAC).
        """.trimIndent()
    )

    val PREGRADOS = listOf(
        AcademicProgram(
            id = "pre_arq",
            name = "Arquitectura",
            code = "31030",
            snies = "5456",
            level = ProgramLevel.PREGRADO,
            faculty = FacultyCategory.INGENIERIAS_Y_ARQUITECTURA,
            semestres = "10 semestres",
            modalidad = "Presencial",
            inversion2026 = "$ 7.107.000",
            inversionNeos2027 = "$ 7.314.000",
            titulo = "Arquitecto",
            resolucion = "Resolución Nº 000443 del 9 de enero del 2026",
            diferenciador = "Facultad con 23 años de historia y miles de egresados. Salas de sistemas para exigencias digitales con las mejores herramientas e infraestructura de Boyacá.",
            empleabilidad = "Arquitecto diseñador, residencia de obra, interventoría, presupuestos, diseñador urbano Jr., delineante, gestor inmobiliario, restaurador de patrimonio.",
            porQueEstudiar = "Es la facultad con mayor tiempo y egresados de la región. Más buscada para estudiar arquitectura en Boyacá.",
            electivas = "Bioclimática, Métodos y representación 1, 2 y 3, Laboratorio de patrimonio, Laboratorio digital.",
            homologaciones = "SENA (dibujo arquitectónico, construcción) y otras IES.",
            movilidad = "Universidad de Asunción, UNAM, Universidad CEU San Pablo Madrid."
        ),
        AcademicProgram(
            id = "pre_bioing",
            name = "Bioingeniería",
            code = "31011",
            snies = "116050",
            level = ProgramLevel.PREGRADO,
            faculty = FacultyCategory.INGENIERIAS_Y_ARQUITECTURA,
            semestres = "8 semestres",
            modalidad = "Presencial",
            inversion2026 = "$ 5.232.000",
            inversionNeos2027 = "$ 5.385.000",
            titulo = "Bioingeniero",
            resolucion = "Resolución 006775 del 26 de abril de 2023 con vigencia de 7 años",
            diferenciador = "Integra ingeniería, física y matemáticas con medicina, biología y ciencias de la vida para resolver problemas de salud y biotecnología.",
            empleabilidad = "Diseño de equipos médicos, sector pecuario y agrícola, bioinformática, promotor de tecnología hospitalaria, gestor de MIPYME tecnológica.",
            electivas = "Biotecnología, Biorremediación, Biología Computacional, Biomédica, Biosensores, Energías Renovables.",
            dobleTitulacion = "Doble programa con Ing. Electrónica, Ing. Mecánica, Cultura Física."
        ),
        AcademicProgram(
            id = "pre_ing_amb",
            name = "Ingeniería Ambiental",
            code = "31010",
            snies = "109663",
            level = ProgramLevel.PREGRADO,
            faculty = FacultyCategory.INGENIERIAS_Y_ARQUITECTURA,
            semestres = "8 semestres",
            modalidad = "Presencial",
            inversion2026 = "$ 5.850.000",
            inversionNeos2027 = "$ 5.850.000",
            titulo = "Ingeniero Ambiental",
            resolucion = "Resolución 014249 del 5 de agosto de 2020 con vigencia de 7 años",
            diferenciador = "Diseño, optimización y adaptación de tecnologías para la prevención y mitigación de contaminación de agua, aire, suelo y cambio climático.",
            empleabilidad = "Empresas mineras, petroleras, entes gubernamentales, CAR, empresas de servicios públicos, consultoría ambiental.",
            dobleTitulacion = "Doble titulación con Universidad de Quintana Roo (México) cursando 2 semestres adicionales."
        ),
        AcademicProgram(
            id = "pre_ing_civ",
            name = "Ingeniería Civil",
            code = "31001",
            snies = "7750",
            level = ProgramLevel.PREGRADO,
            faculty = FacultyCategory.INGENIERIAS_Y_ARQUITECTURA,
            semestres = "8 semestres",
            modalidad = "Presencial",
            inversion2026 = "$ 6.989.700",
            inversionNeos2027 = "$ 6.989.700",
            titulo = "Ingeniero Civil",
            resolucion = "Resolución 012996 del 31 de julio de 2023 con Acreditación de Alta Calidad (6 años)",
            diferenciador = "Más de 25 años de trayectoria e impacto en Boyacá. Acreditación de Alta Calidad. Laboratorios de última tecnología en estructuras, vías, geotecnia, recursos hídricos.",
            empleabilidad = "Planear, diseñar y construir obras civiles, vías, puentes, acueductos, alcantarillados, gerencia de proyectos de construcción.",
            electivas = "Diseño estructural, instalaciones hidrosanitarias, túneles, BIM, drenaje vial."
        ),
        AcademicProgram(
            id = "pre_ing_datos_ia",
            name = "Ingeniería de Datos e Inteligencia Artificial",
            code = "31012",
            snies = "116280",
            level = ProgramLevel.PREGRADO,
            faculty = FacultyCategory.INGENIERIAS_Y_ARQUITECTURA,
            semestres = "8 semestres",
            modalidad = "Presencial",
            inversion2026 = "$ 6.541.000",
            inversionNeos2027 = "$ 6.731.000",
            titulo = "Ingeniero de Datos e Inteligencia Artificial",
            resolucion = "Resolución 23663 del 05 de diciembre de 2023 con vigencia de 7 años",
            diferenciador = "Ocho semestres con formación en líneas de tendencia global: IA, Big Data, Machine Learning, IoT, visión por computadora.",
            empleabilidad = "Científico de datos, ingeniero de machine learning, analista de datos empresariales, consultor en IA y automatización.",
            dobleTitulacion = "Doble programa con Ingeniería Electrónica.",
            movilidad = "Más de 350 opciones (Alemania, Suiza, India, España, Italia, EE.UU., Brasil)."
        ),
        AcademicProgram(
            id = "pre_ing_sis",
            name = "Ingeniería de Sistemas",
            code = "31005",
            snies = "53540",
            level = ProgramLevel.PREGRADO,
            faculty = FacultyCategory.INGENIERIAS_Y_ARQUITECTURA,
            semestres = "8 semestres",
            modalidad = "Híbrida (Presencial - Virtual)",
            inversion2026 = "$ 6.252.000",
            inversionNeos2027 = "$ 6.434.000",
            titulo = "Ingeniero de Sistemas",
            resolucion = "Resolución 009050 del 1 de abril del 2026",
            diferenciador = "Formación integral en desarrollo de software, Cloud Computing, DevOps, Ciberseguridad, Inteligencia Artificial Generativa y Vibe Coding.",
            empleabilidad = "Full Stack Developer, Arquitecto de Software, Ingeniero DevOps, Gerente IT, Ingeniero de IA, Desarrollador Cloud.",
            electivas = "Full Stack Development, DevOps - DevSecOps, Vibe Coding, n8n Automation, IA Generativa, React aplicado, Ciberseguridad.",
            dobleTitulacion = "Doble programa con Ingeniería Electrónica. Homologación directa multicampus y SENA."
        ),
        AcademicProgram(
            id = "pre_ing_sis_virt",
            name = "Virtual - Ingeniería de Sistemas",
            code = "31014",
            snies = "119237",
            level = ProgramLevel.PREGRADO,
            faculty = FacultyCategory.INGENIERIAS_Y_ARQUITECTURA,
            semestres = "8 semestres",
            modalidad = "Virtual",
            inversion2026 = "$ 4.027.000",
            inversionNeos2027 = "$ 4.145.000",
            titulo = "Ingeniero de Sistemas",
            resolucion = "Resolución n.º 010586 del 22 de abril del 2026",
            diferenciador = "Modalidad 100% virtual con el mismo rigor y plan curricular tomasino. Gran accesibilidad económica.",
            empleabilidad = "Desarrollador remoto global, arquitecto de soluciones de software, DevOps, consultor TI."
        ),
        AcademicProgram(
            id = "pre_ing_elec",
            name = "Ingeniería Electrónica",
            code = "31002",
            snies = "7741",
            level = ProgramLevel.PREGRADO,
            faculty = FacultyCategory.INGENIERIAS_Y_ARQUITECTURA,
            semestres = "10 semestres",
            modalidad = "Presencial",
            inversion2026 = "$ 5.974.000",
            inversionNeos2027 = "$ 5.974.000",
            titulo = "Ingeniero Electrónico",
            resolucion = "Resolución 002012 del 28 de febrero de 2024 con Acreditación de Alta Calidad",
            diferenciador = "Acreditación de Alta Calidad. Robótica industrial, telecomunicaciones de alta velocidad, IoT, energías limpias y drones.",
            empleabilidad = "Automatización, telecomunicaciones, energía solar y eólica, desarrollo de hardware, industria 4.0.",
            dobleTitulacion = "Doble programa con Ingeniería de Datos e Inteligencia Artificial."
        ),
        AcademicProgram(
            id = "pre_ing_ind",
            name = "Ingeniería Industrial",
            code = "31007",
            snies = "106920",
            level = ProgramLevel.PREGRADO,
            faculty = FacultyCategory.INGENIERIAS_Y_ARQUITECTURA,
            semestres = "8 semestres",
            modalidad = "Presencial",
            inversion2026 = "$ 5.572.000",
            inversionNeos2027 = "$ 5.735.000",
            titulo = "Ingeniero Industrial",
            resolucion = "Resolución 024871 del 20 de diciembre de 2023",
            diferenciador = "Líneas de profundización: Mejoramiento de procesos y Gestión organizacional. Laboratorio de Métodos Industriales, FlexSim, SolidWorks. Nodo HUB Boyacá Red Reune.",
            empleabilidad = "Gerente de producción, logística y cadena de suministro, calidad, seguridad industrial, consultoría de operaciones.",
            dobleTitulacion = "Doble programa con Ing. Ambiental e Ing. Civil."
        ),
        AcademicProgram(
            id = "pre_ing_mec",
            name = "Ingeniería Mecánica",
            code = "31008",
            snies = "106947",
            level = ProgramLevel.PREGRADO,
            faculty = FacultyCategory.INGENIERIAS_Y_ARQUITECTURA,
            semestres = "8 semestres",
            modalidad = "Presencial",
            inversion2026 = "$ 6.273.000",
            inversionNeos2027 = "$ 6.455.000",
            titulo = "Ingeniero Mecánico",
            resolucion = "Resolución 025205 del 24 de diciembre de 2024",
            diferenciador = "Enfoque práctico de vanguardia con patentes activas y prototipos en laboratorios especializados. Convenios con empresas e industria energética.",
            empleabilidad = "Diseño de maquinaria, sector automotriz, aeroespacial, energías renovables, industria de petróleo y gas, mantenimiento RCM.",
            dobleTitulacion = "Doble programa con Ingeniería Electrónica."
        ),
        AcademicProgram(
            id = "pre_der",
            name = "Derecho",
            code = "32020",
            snies = "9144",
            level = ProgramLevel.PREGRADO,
            faculty = FacultyCategory.CIENCIAS_JURIDICAS_Y_POLITICAS,
            semestres = "10 semestres",
            modalidad = "Presencial",
            inversion2026 = "$ 6.813.000",
            inversionNeos2027 = "$ 6.945.750",
            titulo = "Abogado",
            resolucion = "Resolución 023005 del 30 de noviembre de 2021 con Acreditación de Alta Calidad",
            diferenciador = "Acreditación de Alta Calidad. Sistema modular innovador, formación humanista tomasina y consultorio jurídico integral.",
            empleabilidad = "Juez, fiscal, litigante, asesor corporativo, funcionario de la rama judicial, diplomático, consultor en derecho público y privado."
        ),
        AcademicProgram(
            id = "pre_adm",
            name = "Administración de Empresas",
            code = "32040",
            snies = "19388",
            level = ProgramLevel.PREGRADO,
            faculty = FacultyCategory.CIENCIAS_ECONOMICAS_Y_ADMINISTRATIVAS,
            semestres = "8 semestres",
            modalidad = "Presencial",
            inversion2026 = "$ 4.584.000",
            inversionNeos2027 = "$ 4.717.000",
            titulo = "Administrador de Empresas",
            resolucion = "Resolución 006407 del 20 de abril de 2023 con Acreditación de Alta Calidad",
            diferenciador = "Acreditación de Alta Calidad Multicampus. Dos convenios de doble titulación internacional con la Universidad de Colima en México.",
            empleabilidad = "Gerente general, director de proyectos, emprendedor, consultor de negocios, gestor financiero y de marketing."
        ),
        AcademicProgram(
            id = "pre_cont",
            name = "Contaduría Pública",
            code = "31062",
            snies = "101613",
            level = ProgramLevel.PREGRADO,
            faculty = FacultyCategory.CIENCIAS_ECONOMICAS_Y_ADMINISTRATIVAS,
            semestres = "8 semestres",
            modalidad = "Presencial",
            inversion2026 = "$ 4.210.000",
            inversionNeos2027 = "$ 4.210.000",
            titulo = "Contador Público",
            resolucion = "Resolución Nº 000431 del 09 de enero del 2026",
            diferenciador = "Microcertificación gratuita para estudiantes en software SIIGO Nube y Helissa NIIF. Doble titulación con la Universidad Veracruzana (México). Prácticas con DIAN, CGN y PWC.",
            empleabilidad = "Contador General, Revisor Fiscal, Oficial de Cumplimiento, Asesor Tributario, Gerente Financiero."
        ),
        AcademicProgram(
            id = "pre_mkt",
            name = "Marketing y Transformación Digital",
            code = "33402",
            snies = "116961",
            level = ProgramLevel.PREGRADO,
            faculty = FacultyCategory.CIENCIAS_ECONOMICAS_Y_ADMINISTRATIVAS,
            semestres = "8 semestres",
            modalidad = "Combinada (Presencial-Virtual)",
            inversion2026 = "$ 5.356.000",
            inversionNeos2027 = "$ 5.512.000",
            titulo = "Profesional en Marketing y Transformación Digital",
            resolucion = "Resolución 009357 del 06 de junio de 2024",
            diferenciador = "Programa de vanguardia combinando marketing estratégico con herramientas de analítica, IA, automatización y nuevos medios digitales.",
            empleabilidad = "Director de Marketing Digital, Gerente de Transformación Digital, Growth Hacker, Analista Digital, Consultor E-commerce."
        ),
        AcademicProgram(
            id = "pre_neg_int",
            name = "Negocios Internacionales",
            code = "33052",
            snies = "101838",
            level = ProgramLevel.PREGRADO,
            faculty = FacultyCategory.CIENCIAS_ECONOMICAS_Y_ADMINISTRATIVAS,
            semestres = "8 semestres",
            modalidad = "Presencial",
            inversion2026 = "$ 5.171.000",
            inversionNeos2027 = "$ 5.321.000",
            titulo = "Profesional en Negocios Internacionales",
            resolucion = "Resolución n° 008957 del 31 de marzo de 2026",
            diferenciador = "Líneas de profundización en Logística Internacional, Trading, Marketing Internacional y Relaciones Internacionales. Talleres de Mandarín, Francés y Alemán.",
            dobleTitulacion = "Profesional en Comercio Exterior con BUAP (México)."
        ),
        AcademicProgram(
            id = "pre_neg_int_virt",
            name = "Virtual - Negocios Internacionales",
            code = "32043",
            snies = "101838",
            level = ProgramLevel.PREGRADO,
            faculty = FacultyCategory.CIENCIAS_ECONOMICAS_Y_ADMINISTRATIVAS,
            semestres = "8 semestres",
            modalidad = "Virtual",
            inversion2026 = "$ 3.966.000",
            inversionNeos2027 = "$ 4.081.000",
            titulo = "Profesional en Negocios Internacionales",
            resolucion = "Resolución n.º 08957 del 31 de marzo del 2026",
            diferenciador = "Misma excelencia tomasina en modalidad 100% virtual a menor costo de inversión.",
            dobleTitulacion = "Profesional en Comercio Exterior BUAP."
        ),
        AcademicProgram(
            id = "pre_int_neg",
            name = "Inteligencia de Negocios",
            snies = "Pendiente",
            level = ProgramLevel.PREGRADO,
            faculty = FacultyCategory.CIENCIAS_ECONOMICAS_Y_ADMINISTRATIVAS,
            semestres = "8 semestres",
            modalidad = "Híbrida (50.4% virtual, 66 de 131 créditos virtuales)",
            inversion2026 = "Pendiente por definir",
            inversionNeos2027 = "Pendiente por definir",
            titulo = "Profesional en Inteligencia de Negocios",
            resolucion = "Resolución No. 018423 del 21 de julio de 2026 del MEN (7 años)",
            diferenciador = "Convierte datos masivos en decisiones estratégicas de alta dirección mediante analítica predictiva, visualización y Big Data.",
            empleabilidad = "Business Intelligence Lead, Analista Estratégico de Negocios, Data Warehouse Manager."
        ),
        AcademicProgram(
            id = "pre_dis_int",
            name = "Diseño de Interacción",
            code = "31009",
            snies = "109495",
            level = ProgramLevel.PREGRADO,
            faculty = FacultyCategory.INGENIERIAS_Y_ARQUITECTURA,
            semestres = "8 semestres",
            modalidad = "Presencial",
            inversion2026 = "$ 5.678.000",
            inversionNeos2027 = "$ 5.844.000",
            titulo = "Profesional en Diseño de Interacción",
            resolucion = "Resolución 012792 del 10 de julio de 2020",
            diferenciador = "Diseña productos y experiencias digitales: interfaces UI/UX, realidad virtual, apps móviles, diseño de servicios y videojuegos para mejorar la calidad de vida.",
            empleabilidad = "Product Designer, Diseñador UX/UI, Diseñador de Videojuegos y VR, Consultor de Experiencia de Usuario.",
            dobleTitulacion = "Doble titulación con Diseño Gráfico Bogotá."
        ),
        AcademicProgram(
            id = "pre_cul_fis",
            name = "Cultura Física, Deporte y Recreación",
            code = "32080",
            snies = "106756",
            level = ProgramLevel.PREGRADO,
            faculty = FacultyCategory.CIENCIAS_DE_LA_SALUD_Y_DEPORTE,
            semestres = "9 semestres",
            modalidad = "Presencial",
            inversion2026 = "$ 3.966.000",
            inversionNeos2027 = "$ 3.966.000",
            titulo = "Profesional en Cultura física, deporte y recreación",
            resolucion = "Resolución 010149 del 20 junio de 2024",
            diferenciador = "El mejor campus deportivo de Boyacá. Laboratorios de Morfofisiología y Fisiología del Ejercicio con tecnología de punta.",
            empleabilidad = "Entrenador de alto rendimiento, preparador físico, gestor deportivo, rehabilitación deportiva, docente deportivo.",
            dobleTitulacion = "Universidad Autónoma de Baja California (México) - Lic. en Actividad Física y Deporte."
        ),
        AcademicProgram(
            id = "pre_nut_diet",
            name = "Nutrición y Dietética",
            snies = "119193",
            level = ProgramLevel.PREGRADO,
            faculty = FacultyCategory.CIENCIAS_DE_LA_SALUD_Y_DEPORTE,
            semestres = "9 semestres",
            modalidad = "Presencial",
            inversion2026 = "Por definir",
            inversionNeos2027 = "Por definir",
            titulo = "Nutricionista y dietista",
            resolucion = "Resolución No. 009134 del 01 de abril de 2026 del MEN (7 años)",
            diferenciador = "Formación en nutrición clínica, empresarial, deportiva y pública con enfoque biopsicosocial y laboratorios de análisis sensorial.",
            empleabilidad = "Nutricionista clínico en hospitales, asesor deportivo de alto rendimiento, formulador de planes alimentarios en industria alimentaria."
        ),
        AcademicProgram(
            id = "pre_lic_lenguas",
            name = "Licenciatura en Español y Lenguas Extranjeras (Inglés y Francés)",
            code = "32090",
            snies = "109963",
            level = ProgramLevel.PREGRADO,
            faculty = FacultyCategory.EDUCACION_Y_HUMANIDADES,
            semestres = "10 semestres",
            modalidad = "Presencial",
            inversion2026 = "$ 4.151.000",
            inversionNeos2027 = "$ 4.272.000",
            titulo = "Licenciado en Español y Lenguas Extranjeras: Inglés Francés",
            resolucion = "Resolución 009833 del 16 de junio de 2020",
            diferenciador = "Dominio y certificación en tres lenguas (Español, Inglés y Francés nivel C1). Habilita para docencia en Colombia y en el extranjero.",
            empleabilidad = "Docente trilingüe nacional e internacional, gestor de programas bilingües, traductor y consultor editorial."
        ),
        AcademicProgram(
            id = "pre_lic_infantil",
            name = "Licenciatura en Educación Infantil Bilingüe",
            code = "32091",
            snies = "118409",
            level = ProgramLevel.PREGRADO,
            faculty = FacultyCategory.EDUCACION_Y_HUMANIDADES,
            semestres = "9 semestres",
            modalidad = "Presencial",
            inversion2026 = "$ 4.163.000",
            inversionNeos2027 = "$ 4.285.000",
            titulo = "Licenciado/a en Educación Bilingüe",
            resolucion = "Resolución Nº 022697 del 25 de noviembre del 2025",
            diferenciador = "Enfoque en neurodidáctica, gamificación y bilingüismo en primera infancia con nivel de inglés B2 certificado.",
            empleabilidad = "Docente en preescolar y primaria bilingüe, director pedagógico, diseñador de recursos didácticos infantiles."
        )
    )

    val MICROCREDENCIALES_GRADO_11 = listOf(
        "Arquitectura: 'Expresión I: Representación y Comunicación de una Idea' | Inversión: $100.000 | Duración: 1 semestre | Docente: Medina Alba Jairo Mauricio",
        "Bioingeniería: 'Introducción a la Bioingeniería' | Inversión: $100.000 | Duración: 1 semestre | Docente: Aragon Morales María Fernanda",
        "Ingeniería Civil: 'Introducción a la Ingeniería' | Inversión: $100.000 | Duración: 1 semestre | Docente: Castro Peña Julián",
        "Ingeniería de Datos e IA: 'Introducción a la Ing. de Datos e IA' | Inversión: $100.000 | Duración: 1 semestre",
        "Ingeniería de Sistemas: 'Programación Estructurada para Ingenierías' | Inversión: $100.000 | Duración: 1 semestre | Docente: Castro Barinas Manuel Leonardo",
        "Ingeniería Electrónica: 'Introducción a la Ingeniería Electrónica' | Inversión: $100.000 | Duración: 1 semestre | Docente: Álvarez Castañeda William",
        "Ingeniería Industrial: 'Gestión Organizacional' | Inversión: $100.000 | Duración: 1 semestre | Docente: Sandoval Castillo Luis Andrés",
        "Derecho: 'Historia del Derecho y de las Instituciones Jurídicas' | Inversión: $100.000 | Duración: 1 semestre",
        "Derecho: 'Sociología Jurídica' | Inversión: $100.000 | Duración: 1 semestre",
        "Administración de Empresas: 'Pensamiento Administrativo' | Inversión: $100.000 | Duración: 1 semestre",
        "Contaduría Pública: 'Contabilidad Financiera Básica' | Inversión: $100.000 | Duración: 1 semestre",
        "Marketing y Transformación Digital: 'Pensamiento Creativo' | Inversión: $100.000 | Duración: 1 semestre | Docente: Castro Cely Cristhian Andrés",
        "Negocios Internacionales: 'Protocolo y Etiqueta de Alta Dirección' | Inversión: $100.000 | Duración: 1 semestre | Docente: Acuña Daza Gloria Stella",
        "Diseño de Interacción: 'Expresión 1' | Inversión: $100.000 | Duración: 1 semestre | Docente: Suárez Cruces Martha Liliana",
        "Cultura Física, Deporte y Recreación: 'Pedagogía de la Cultura Física' | Inversión: $100.000 | Duración: 1 semestre | Docente: Suárez Gil Oscar Augusto",
        "Dirección de Ciencias Básicas: 'Matemática I' | Inversión: $100.000 | Duración: 1 semestre | Docente: Samacá Ramírez José Vicente",
        "Dirección de Humanidades: 'Persona Humana, Sociedad y Conocimiento' | Inversión: $100.000 | Duración: 1 semestre"
    )

    val POSGRADOS_RESUMEN = listOf(
        "Esp. en Estructuras (Presencial $7.980.000 / Virtual $7.600.000) - 2 semestres",
        "Esp. en Geotecnia Vial y Pavimentos (Presencial $7.970.000 / Virtual $7.870.000) - 2 semestres",
        "Esp. en Gerencia de Proyectos de Construcción (Híbrida $7.950.000) - 2 semestres",
        "Esp. en Gerencia de Mantenimiento y Gestión de Activos (Presencial $5.900.000 / Virtual $5.750.000)",
        "Esp. en Ingeniería Hidroambiental (Híbrida $6.760.000)",
        "Virtual Esp. en Ciencia de Datos ($5.950.000) - 2 semestres",
        "Virtual Esp. en Gerencia de la Cadena de Valor y Productividad ($4.700.000)",
        "Esp. en Derecho Penal y Procesal Penal ($7.600.000)",
        "Esp. en Derecho Administrativo ($7.740.000)",
        "Esp. en Contratación Estatal ($7.280.000)",
        "Esp. en Gobierno y Políticas Públicas Virtual ($6.200.000)",
        "Esp. en Auditoría de Salud ($7.550.000)",
        "Esp. en Auditoría y Gestión de Riesgos ($6.200.000)",
        "Esp. en Innovación y Marketing ($6.100.000)",
        "Virtual Esp. en Gerencia Estratégica de Costos ($3.970.000)",
        "Maestría en Geotecnia Vial y Pavimentos (Presencial $8.750.000 / Virtual $8.650.000) - 4 semestres",
        "Maestría en Ingeniería ($7.290.000) - 3 semestres",
        "Maestría en Ingeniería Civil con Énfasis en Hidroambiental ($8.400.000) - 4 semestres",
        "Virtual Maestría en Sostenibilidad Ambiental ($6.800.000) - 3 semestres",
        "Maestría en Arquitectura Avanzada ($7.900.000) - 3 semestres",
        "Virtual Maestría en Liderazgo y Transformación Digital ($6.200.000)",
        "Maestría en Derecho Penal y Procesal Penal ($9.000.000)",
        "Maestría en Derecho Administrativo ($9.200.000)",
        "Maestría en Derecho Privado (Presencial $8.900.000 / Virtual $8.700.000)",
        "Maestría en Marketing Internacional y Negocios (Presencial $6.700.000 / Virtual $6.520.000)",
        "Maestría en Entrenamiento Deportivo y Actividad Física ($5.700.000) - 3 semestres",
        "Doctorado en Sostenibilidad ($14.800.000) - 6 semestres",
        "Doctorado en Derecho Público (Presencial $11.100.000 / Virtual $9.900.000) - 6 semestres",
        "Doctorado en Pedagogía y Neurociencia Aplicada a la Educación ($10.190.000) - 6 semestres"
    )
}
