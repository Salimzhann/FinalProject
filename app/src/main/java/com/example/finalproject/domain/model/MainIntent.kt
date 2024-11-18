package com.example.finalproject.domain.model

sealed class MainIntent {
    data object LoadAllData : MainIntent()
    data object LoadPremieres : MainIntent()
    data object LoadPopular : MainIntent()
    data object LoadSeries : MainIntent()
    data class LoadFilmDetailById(val filmId: Long) : MainIntent()
    data class LoadStaffById(val filmId: Long) : MainIntent()
    data class LoadActorDetails(val id: Int) : MainIntent()
    data class LoadFilmImages(val filmId: Long, val type: String? = "STILL", val page: Int = 1) : MainIntent()
}