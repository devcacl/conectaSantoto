package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rag_documents")
data class RagDocument(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,         // e.g. "Pregrados Oficiales.md", "Posgrados.md"
    val category: String,      // Pregrados, Posgrados, Microcredenciales, Directorio, Requisitos, Inversión
    val content: String,       // Full Markdown text
    val lastUpdated: Long = System.currentTimeMillis()
)
