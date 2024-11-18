package com.example.finalproject.data.repository

import com.example.finalproject.domain.model.ActorDetail
import com.example.finalproject.domain.model.FilmDetail
import com.example.finalproject.domain.model.FilmImagesResponse
import com.example.finalproject.domain.model.MovieResponse
import com.example.finalproject.domain.model.StaffMember
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private val retrofit = Retrofit.Builder()
    .baseUrl("https://kinopoiskapiunofficial.tech/")
    .addConverterFactory(GsonConverterFactory.create())
    .client(
        OkHttpClient.Builder().addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("X-API-KEY", "b05ecff3-58c8-469e-ac81-bfa3c0ee6f1f")
                .build()
            chain.proceed(request)
        }.build()
    )
    .build()
//b05ecff3-58c8-469e-ac81-bfa3c0ee6f1f    ||    e82baed2-914f-4384-999a-a71af825d6bd || beb20069-75af-4b17-8c30-5ea3932df8b5
//116b74c8-d1ec-4c61-94d1-bc4dcf100f70
val api = retrofit.create(FilmRepository::class.java)

interface FilmRepository {
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
        @Path("id") filmId: Long,
        @Query("type") type: String? = null,
        @Query("page") page: Int? = 1
    ): Response<FilmImagesResponse>
}