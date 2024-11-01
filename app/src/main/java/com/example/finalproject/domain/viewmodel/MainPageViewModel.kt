package com.example.finalproject.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.finalproject.data.repository.FilmApiService
import com.example.finalproject.domain.model.MovieItem
import com.example.finalproject.domain.model.MovieResponse
import com.example.finalproject.domain.model.ScreenState
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class MainPageViewModel : ViewModel() {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://kinopoiskapiunofficial.tech/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder().addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("X-API-KEY", "e82baed2-914f-4384-999a-a71af825d6bd")
                    .addHeader("Content-type", "application/json")
                    .build()
                chain.proceed(request)
            }.build()
        )
        .build()

    private val api = retrofit.create(FilmApiService::class.java)

    val screenStatePremieres = MutableLiveData<ScreenState>(ScreenState.Initial)
    val screenStatePopular = MutableLiveData<ScreenState>(ScreenState.Initial)
    val screenStateSeries = MutableLiveData<ScreenState>(ScreenState.Initial)

    init {
        loadMovies()
    }

    fun getLimitedMovies(movies: List<MovieItem>): List<MovieItem> = movies.take(8)

    private fun loadMovies() {
        loadMovieData("TOP_250_MOVIES", screenStatePremieres)
        loadMovieData("TOP_POPULAR_ALL", screenStatePopular)
        loadMovieData("TOP_250_TV_SHOWS", screenStateSeries)
    }

    private fun loadMovieData(type: String, stateLiveData: MutableLiveData<ScreenState>) {
        stateLiveData.value = ScreenState.Loading
        api.getCollections(type, 1).enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    val movies = response.body()?.items ?: emptyList()
                    stateLiveData.value = ScreenState.Success(movies)
                } else {
                    stateLiveData.value = ScreenState.Error("Failed to load data: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                stateLiveData.value = ScreenState.Error("Request failed: ${t.message}")
            }
        })
    }
}