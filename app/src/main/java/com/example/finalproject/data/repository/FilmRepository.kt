package com.example.finalproject.data.repository

import com.example.finalproject.domain.model.ActorDetail
import com.example.finalproject.domain.model.FilmDetail
import com.example.finalproject.domain.model.FilmImagesResponse
import com.example.finalproject.domain.model.MovieResponse
import com.example.finalproject.domain.model.StaffMember
import com.example.finalproject.domain.model.filter.FiltersResponse
import com.example.finalproject.domain.model.search.MoviesResponse
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
                .addHeader("X-API-KEY", "116b74c8-d1ec-4c61-94d1-bc4dcf100f70")
                .build()
            chain.proceed(request)
        }.build()
    )
    .build()
//b05ecff3-58c8-469e-ac81-bfa3c0ee6f1f    ||    e82baed2-914f-4384-999a-a71af825d6bd || beb20069-75af-4b17-8c30-5ea3932df8b5
//116b74c8-d1ec-4c61-94d1-bc4dcf100f70
val api: FilmRepository = retrofit.create(FilmRepository::class.java)

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
    @GET("/api/v2.1/films/search-by-keyword")
    suspend fun searchMovies(
        @Query("keyword") keyword: String,
        @Query("page") page: Int = 1
    ): Response<com.example.finalproject.domain.model.search.MovieResponse>
    @GET("/api/v2.2/films/filters")
    suspend fun getFilters(): Response<FiltersResponse>
    @GET("/api/v2.2/films")
    suspend fun getFilteredMovies(
        @Query("countries") countries: List<Int>?,
        @Query("genres") genres: List<Int>?,
        @Query("yearFrom") yearFrom: Int = 1000,
        @Query("yearTo") yearTo: Int = 3000,
        @Query("ratingFrom") ratingFrom: Float = 0f,
        @Query("ratingTo") ratingTo: Float = 10f,
        @Query("order") order: String = "RATING",
        @Query("type") type: String = "ALL",
        @Query("page") page: Int = 1
    ): Response<MoviesResponse>

}