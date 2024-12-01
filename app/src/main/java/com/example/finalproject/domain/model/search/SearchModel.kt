package com.example.finalproject.domain.model.search

data class Film(
    val filmId: Long,
    val nameRu: String?,
    val nameEn: String?,
    val type: String?,
    val year: String?,
    val description: String?,
    val filmLength: String?,
    val countries: List<Country>,
    val genres: List<Genre>,
    val rating: String?,
    val ratingVoteCount: Int?,
    val posterUrl: String?,
    val posterUrlPreview: String?
)

data class Country(
    val country: String
)

data class Genre(
    val genre: String
)
data class MovieResponse(
    val keyword: String,
    val pagesCount: Int,
    val searchFilmsCountResult: Int,
    val films: List<Film>
)
