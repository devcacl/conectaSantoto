package com.example.data.model

enum class ProgramLevel {
    PREGRADO,
    POSGRADO,
    MICROCREDENCIAL
}

enum class FacultyCategory {
    INGENIERIAS_Y_ARQUITECTURA,
    CIENCIAS_ECONOMICAS_Y_ADMINISTRATIVAS,
    CIENCIAS_JURIDICAS_Y_POLITICAS,
    CIENCIAS_DE_LA_SALUD_Y_DEPORTE,
    EDUCACION_Y_HUMANIDADES
}

data class AcademicProgram(
    val id: String,
    val name: String,
    val code: String = "",
    val snies: String = "",
    val level: ProgramLevel,
    val faculty: FacultyCategory,
    val semestres: String,
    val modalidad: String, // Presencial, Virtual, Híbrida
    val inversion2026: String,
    val inversionNeos2027: String = "",
    val titulo: String,
    val resolucion: String = "",
    val diferenciador: String,
    val empleabilidad: String = "",
    val porQueEstudiar: String = "",
    val electivas: String = "",
    val homologaciones: String = "",
    val dobleTitulacion: String = "",
    val movilidad: String = "",
    val horario: String = "",
    val enlaceInscripcion: String = ""
)
