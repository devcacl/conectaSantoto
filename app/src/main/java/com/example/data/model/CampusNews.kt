package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class CampusNews(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val summary: String,
    val content: String,
    val category: String, // e.g. "Institucional", "Admisiones", "Becas & Financiación", "Académico", "Innovación"
    val dateText: String, // e.g. "19 OCT 2026"
    val imageUrl: String = "https://images.unsplash.com/photo-1523240795612-9a054b0db644?w=800&auto=format&fit=crop&q=80",
    val author: String = "Dirección de Comunicaciones USTA Tunja",
    val readTimeMinutes: Int = 3,
    val isFeatured: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
