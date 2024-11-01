package com.example.finalproject.data.repository

import com.example.finalproject.domain.model.Film
import retrofit2.http.GET
import retrofit2.Call

interface FilmApiService {
    @GET("api/v2.2/films")
    fun getFilms(): Call<List<Film>> // Предположим, что ответ — это список объектов Film
}
