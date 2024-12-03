package com.example.finalproject.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.finalproject.domain.model.Genre

@Entity(tableName = "films")
data class FilmEntity(
    @PrimaryKey val kinopoiskId: Long,
    val nameRu: String?,
    val nameOriginal: String?,
    val posterUrl: String?,
    val ratingKinopoisk: Double?,
    val genres: List<Genre>,
    val collectionType: String
)
