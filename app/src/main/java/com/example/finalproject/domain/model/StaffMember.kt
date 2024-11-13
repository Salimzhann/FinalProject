package com.example.finalproject.domain.model

data class StaffMember(
    val staffId: Long,
    val nameRu: String,
    val nameEn: String,
    val description: String,
    val posterUrl: String,
    val professionText: String,
    val professionKey: String
)