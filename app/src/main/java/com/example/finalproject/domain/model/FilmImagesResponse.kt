package com.example.finalproject.domain.model

data class FilmImagesResponse(
    val total: Int,
    val totalPages: Int,
    val items: List<ImageItem>
)

data class ImageItem(
    val imageUrl: String,
    val previewUrl: String
)