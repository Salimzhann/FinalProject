package com.example.finalproject.domain.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.finalproject.data.repository.FilmApiService
import com.example.finalproject.domain.model.MovieItem
import com.example.finalproject.domain.model.MovieResponse
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

    val premieres: MutableLiveData<List<MovieItem>> = MutableLiveData()
    val popularCinema: MutableLiveData<List<MovieItem>> = MutableLiveData()
    val usaActionMovies: MutableLiveData<List<MovieItem>> = MutableLiveData()

    init {
        loadMovies()
    }

    fun getLimitedMovies(movies: List<MovieItem>): List<MovieItem> {
        return movies.take(8)
    }

    private fun loadMovies() {
        loadMovieData("TOP_250_MOVIES", premieres)
        loadMovieData("TOP_POPULAR_ALL", popularCinema)
        loadMovieData("TOP_250_TV_SHOWS", usaActionMovies)
    }

    private fun loadMovieData(type: String, liveData: MutableLiveData<List<MovieItem>>) {
        api.getCollections(type, 1).enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    response.body()?.items?.let {
                        liveData.value = it
                        Log.d("API_RESPONSE", "Loaded ${it.size} items for type $type")
                    }
                } else {
                    Log.e("API_ERROR", "Failed to load data: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.e("API_ERROR", "Request failed", t)
            }
        })
    }
}