package com.example.data.model

data class TourHotspot(
    val title: String,
    val description: String,
    val yawAngle: Float // Grados de 0 a 360
)

data class CampusTourLocation(
    val id: String,
    val title: String,
    val subtitle: String,
    val campusName: String,
    val description: String,
    val primaryColorHex: Long,
    val secondaryColorHex: Long,
    val tags: List<String>,
    val highlights: List<String>,
    val hotspots: List<TourHotspot>
)
