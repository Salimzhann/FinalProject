package com.example.finalproject.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.finalproject.data.remote.api
import com.example.finalproject.domain.model.ActorDetail
import com.example.finalproject.domain.model.FilmDetail
import com.example.finalproject.domain.model.ImageItem
import com.example.finalproject.domain.model.MovieItem
import com.example.finalproject.domain.model.MovieResponse
import com.example.finalproject.domain.model.ScreenState
import com.example.finalproject.domain.model.StaffMember
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainPageViewModel : ViewModel() {

    val screenStatePremieres = MutableLiveData<ScreenState<List<MovieItem>>>(ScreenState.Initial)
    val screenStatePopular = MutableLiveData<ScreenState<List<MovieItem>>>(ScreenState.Initial)
    val screenStateSeries = MutableLiveData<ScreenState<List<MovieItem>>>(ScreenState.Initial)
    val screenStateFilmDetail = MutableLiveData<ScreenState<FilmDetail>>(ScreenState.Initial)
    val staffMembers = MutableLiveData<List<StaffMember>>()
    val filmImages = MutableLiveData<List<ImageItem>>()
    val isLoadingImages = MutableLiveData(false)
    val actorDetails = MutableLiveData<ActorDetail>(null)

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
    fun loadStaff(filmId: Long) {
        api.getFilmStaff(filmId).enqueue(object : Callback<List<StaffMember>> {
            override fun onResponse(call: Call<List<StaffMember>>, response: Response<List<StaffMember>>) {
                if (response.isSuccessful) {
                    staffMembers.value = response.body() ?: emptyList()
                } else {
                    Log.e("API Error", "Failed to load staff data")
                }
            }

            override fun onFailure(call: Call<List<StaffMember>>, t: Throwable) {
                Log.e("API Failure", "Error fetching staff data: ${t.message}")
            }
        })
    }

    fun loadFilmDetailAndStaffById(movieId: Long) {
        loadFilmDetailById(movieId)
        loadStaff(movieId)
    }
    fun loadActorDetails(id: Int) {
        viewModelScope.launch {
            try {
                val response = api.getActorDetailById(id)
                if (response.isSuccessful) {
                    actorDetails.value = response.body()
                } else {
                    Log.e("ActorDetails", "Failed to fetch details: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("ActorDetails", "Error fetching actor details", e)
            }
        }
    }

    fun loadFilmImages(filmId: Long, type: String? = "STILL", page: Int = 1) {
        viewModelScope.launch {
            isLoadingImages.value = true
            try {
                val response = api.getFilmImages(filmId, type, page)
                if (response.isSuccessful && response.body() != null) {
                    filmImages.value = response.body()!!.items
                } else {
                    Log.e("API Error", "Failed to load film images: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("API Exception", "Error fetching film images", e)
            } finally {
                isLoadingImages.value = false
            }
        }
    }
}
