package com.example.finalproject.helper

import com.example.finalproject.data.local.FilmEntity
import com.example.finalproject.domain.model.FilmDetail
import com.example.finalproject.domain.model.MovieItem

fun FilmEntity.toFilmDetail(): FilmDetail {
    return FilmDetail(
        kinopoiskId = this.kinopoiskId,
        nameRu = this.nameRu,
        nameOriginal = this.nameOriginal ?: "",
        posterUrl = this.posterUrl ?: "",
        ratingKinopoisk = this.ratingKinopoisk,
        genres = this.genres,
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

fun FilmEntity.toMovieItem(): MovieItem {
    return MovieItem(
        kinopoiskId = this.kinopoiskId,
        nameRu = this.nameRu ?: null,
        nameEn = null,
        nameOriginal = this.nameOriginal ?: this.nameRu ?: "",
        countries = emptyList(),
        genres = this.genres,
        ratingKinopoisk = this.ratingKinopoisk,
        ratingImbd = null,
        year = "",
        type = "",
        posterUrl = this.posterUrl ?: "",
        posterUrlPreview = "",
    )
}