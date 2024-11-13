package com.example.finalproject.data.remote

import com.example.finalproject.domain.model.FilmDetail
import com.example.finalproject.domain.model.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FilmApiService {
    @GET("api/v2.2/films/collections")
    fun getCollections(@Query("type") type: String, @Query("page") page: Int): Call<MovieResponse>
    @GET("api/v2.2/films/{id}")
    fun getFilmById(@Path("id") id: Long): Call<FilmDetail>
}