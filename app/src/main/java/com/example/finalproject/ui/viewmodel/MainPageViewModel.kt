package com.example.finalproject.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.finalproject.data.remote.FilmApiService
import com.example.finalproject.domain.model.FilmDetail
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
                    .build()
                chain.proceed(request)
            }.build()
        )
        .build()

    private val api = retrofit.create(FilmApiService::class.java)

    val screenStatePremieres = MutableLiveData<ScreenState<List<MovieItem>>>(ScreenState.Initial)
    val screenStatePopular = MutableLiveData<ScreenState<List<MovieItem>>>(ScreenState.Initial)
    val screenStateSeries = MutableLiveData<ScreenState<List<MovieItem>>>(ScreenState.Initial)
    val screenStateFilmDetail = MutableLiveData<ScreenState<FilmDetail>>(ScreenState.Initial)

    init {
        loadMovies()
    }

    fun getLimitedMovies(movies: List<MovieItem>): List<MovieItem> = movies.take(8)

    private fun loadMovies() {
        loadMovieData("TOP_250_MOVIES", screenStatePremieres)
        loadMovieData("TOP_POPULAR_ALL", screenStatePopular)
        loadMovieData("TOP_250_TV_SHOWS", screenStateSeries)
    }

    private fun loadMovieData(type: String, stateLiveData: MutableLiveData<ScreenState<List<MovieItem>>>) {
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

    fun loadFilmDetailById(filmId: Long) {
        screenStateFilmDetail.value = ScreenState.Loading
        api.getFilmById(filmId).enqueue(object : Callback<FilmDetail> {
            override fun onResponse(call: Call<FilmDetail>, response: Response<FilmDetail>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        screenStateFilmDetail.value = ScreenState.Success(it)
                    } ?: run {
                        screenStateFilmDetail.value = ScreenState.Error("No film details found.")
                    }
                } else {
                    screenStateFilmDetail.value = ScreenState.Error("Failed to load film detail: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<FilmDetail>, t: Throwable) {
                screenStateFilmDetail.value = ScreenState.Error("Request failed: ${t.message}")
            }
        })
    }
}
