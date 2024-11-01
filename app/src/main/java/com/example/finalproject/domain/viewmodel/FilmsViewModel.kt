package com.example.finalproject.domain.viewmodel
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.data.repository.FilmApiService
import com.example.finalproject.domain.model.Film
import com.example.finalproject.domain.model.ScreenState
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FilmsViewModel : ViewModel() {
    private val _screenState = mutableStateOf<ScreenState<List<Film>>>(ScreenState.Initial)
    val screenState: State<ScreenState<List<Film>>> = _screenState

    private val apiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://kinopoiskapiunofficial.tech/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FilmApiService::class.java)
    }

    init {
        loadFilms()
    }

    private fun loadFilms() {
        _screenState.value = ScreenState.Loading
        viewModelScope.launch {
            try {
                val response = apiService.getFilms().execute()
                if (response.isSuccessful && response.body() != null) {
                    _screenState.value = ScreenState.Success(response.body()!!)
                } else {
                    _screenState.value = ScreenState.Error("Error loading films: ${response.message()}")
                }
            } catch (e: Exception) {
                _screenState.value = ScreenState.Error("Exception: ${e.message}")
            }
        }
    }
}
