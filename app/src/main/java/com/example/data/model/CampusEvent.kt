package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class CampusEvent(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val dateBadge: String, // e.g. "18 OCT"
    val dateText: String = "2026-10-18",
    val time: String,
    val location: String,
    val category: String,
    val description: String,
    val isFeatured: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
