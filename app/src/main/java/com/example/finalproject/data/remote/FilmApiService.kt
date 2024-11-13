package com.example.finalproject.data.remote

import com.example.finalproject.domain.model.ActorDetail
import com.example.finalproject.domain.model.FilmDetail
import com.example.finalproject.domain.model.FilmImagesResponse
import com.example.finalproject.domain.model.MovieResponse
import com.example.finalproject.domain.model.StaffMember
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FilmApiService {
    @GET("api/v2.2/films/collections")
    fun getCollections(@Query("type") type: String, @Query("page") page: Int): Call<MovieResponse>
    @GET("api/v2.2/films/{id}")
    fun getFilmById(@Path("id") id: Long): Call<FilmDetail>
    @GET("api/v1/staff")
    fun getFilmStaff(@Query("filmId") filmId: Long): Call<List<StaffMember>>
    @GET("api/v1/staff/{id}")
    suspend fun getActorDetailById(@Path("id") id: Int): Response<ActorDetail>
    @GET("/api/v2.2/films/{id}/images")
    suspend fun getFilmImages(
        @Path("id") filmId: Int,
        @Query("type") type: String? = null,
        @Query("page") page: Int? = 1
    ): Response<FilmImagesResponse>
}