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
    val imageUrl: String = "https://images.unsplash.com/photo-1541339907198-e08756dedf3f?w=800&auto=format&fit=crop&q=80",
    val capacity: Int = 60,
    val availableSpots: Int = 45,
    val isFeatured: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
