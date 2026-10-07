package com.example.data.model

data class TestOption(
    val text: String,
    val facultyWeight: FacultyCategory
)

data class VocationalQuestion(
    val id: Int,
    val title: String,
    val categoryDescription: String,
    val options: List<TestOption>
)

data class VocationalResult(
    val topFaculty: FacultyCategory,
    val facultyScores: Map<FacultyCategory, Int>,
    val recommendedPrograms: List<String>,
    val personalityProfile: String
)
