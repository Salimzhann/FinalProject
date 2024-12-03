package com.example.finalproject.helper

import com.example.finalproject.data.local.FilmEntity
import com.example.finalproject.domain.model.FilmDetail

fun FilmEntity.toFilmDetail(): FilmDetail {
    // You need to reconstruct FilmDetail from FilmEntity
    return FilmDetail(
        kinopoiskId = this.kinopoiskId,
        nameRu = this.nameRu,
        nameOriginal = this.nameOriginal ?: "",
        posterUrl = this.posterUrl ?: "",
        ratingKinopoisk = this.ratingKinopoisk,
        genres = this.genres,
        // Provide default or empty values for the rest
        kinopoiskHDId = "",
        imdbId = "",
        nameEn = "",
        posterUrlPreview = "",
        coverUrl = null,
        logoUrl = null,
        reviewsCount = 0,
        ratingGoodReview = 0.0,
        ratingGoodReviewVoteCount = 0,
        ratingKinopoiskVoteCount = 0,
        ratingImdb = null,
        ratingImdbVoteCount = 0,
        ratingFilmCritics = null,
        ratingFilmCriticsVoteCount = 0,
        ratingAwait = null,
        ratingAwaitCount = null,
        ratingRfCritics = null,
        ratingRfCriticsVoteCount = null,
        webUrl = "",
        year = 0,
        filmLength = null,
        slogan = null,
        description = "",
        shortDescription = null,
        editorAnnotation = null,
        isTicketsAvailable = false,
        productionStatus = "",
        type = "",
        ratingMpaa = null,
        ratingAgeLimits = null,
        hasImax = false,
        has3D = false,
        lastSync = "",
        countries = emptyList(),
        startYear = null,
        endYear = null,
        serial = false,
        shortFilm = false,
        completed = false
    )
}