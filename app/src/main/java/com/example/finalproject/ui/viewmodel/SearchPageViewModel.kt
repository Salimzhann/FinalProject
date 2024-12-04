package com.example.finalproject.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.data.repository.api
import com.example.finalproject.domain.model.ScreenState
import com.example.finalproject.domain.model.filter.Country
import com.example.finalproject.domain.model.filter.Genre
import com.example.finalproject.domain.model.search.Film
import com.example.finalproject.domain.model.search.Movie
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchViewModel : ViewModel() {
    private val _selectedCountryId = MutableStateFlow<Int?>(null) // ID выбранной страны
    val selectedCountryId: StateFlow<Int?> = _selectedCountryId
    private val _selectedGenreId = MutableStateFlow<Int?>(null) // ID выбранного жанра
    val selectedGenreId: StateFlow<Int?> = _selectedGenreId
    private val _selectedCountryName = MutableStateFlow<String?>(null)
    val selectedCountryName: StateFlow<String?> = _selectedCountryName
    private val _selectedGenreName = MutableStateFlow<String?>(null)
    val selectedGenreName: StateFlow<String?> = _selectedGenreName
    val screenStateSearch = MutableLiveData<ScreenState<List<Film>>>(ScreenState.Initial)
    private val searchQuery = MutableStateFlow("")
    val genres = MutableLiveData<List<Genre>>()
    val countries = MutableLiveData<List<Country>>()
    private val filtersLoadingState = MutableLiveData<ScreenState<Nothing>>(ScreenState.Initial)
    var selectedYearRange by mutableStateOf<Pair<Int, Int>?>(null)
    val filteredMoviesState = MutableLiveData<ScreenState<List<Movie>>>(ScreenState.Initial)

    fun loadFilteredMovies(
        countryId: Int?,
        genreId: Int?,
        yearRange: Pair<Int, Int>?,
        ratingRange: Pair<Float, Float>,
        sorting: String,
        category: String
    ) {
        filteredMoviesState.value = ScreenState.Loading
        viewModelScope.launch {
            try {
                val response = api.getFilteredMovies(
                    countries = countryId?.let { listOf(it) },
                    genres = genreId?.let { listOf(it) },
                    yearFrom = yearRange?.first ?: 1000,
                    yearTo = yearRange?.second ?: 3000,
                    ratingFrom = ratingRange.first,
                    ratingTo = ratingRange.second,
                    order = sorting,
                    type = if (category == "All") "ALL" else category
                )

                if (response.isSuccessful) {
                    val movies = response.body()?.items ?: emptyList()
                    filteredMoviesState.value = if (movies.isNotEmpty()) {
                        ScreenState.Success(movies)
                    } else {
                        ScreenState.Error("Фильмы не найдены")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    filteredMoviesState.value =
                        ScreenState.Error("Ошибка сервера: ${response.message()}. Details: $errorBody")
                }
            } catch (e: Exception) {
                filteredMoviesState.value = ScreenState.Error("Ошибка сети: ${e.localizedMessage}")
            }
        }
    }

    fun updateYearRange(startYear: Int, endYear: Int) {
        selectedYearRange = startYear to endYear
    }

    fun updateSelectedCountry(id: Int?, name: String?) {
        _selectedCountryId.value = id
        _selectedCountryName.value = name
    }
    fun updateSelectedGenre(id: Int?, name: String?) {
        _selectedGenreId.value = id
        _selectedGenreName.value = name
    }

    init {
        viewModelScope.launch {
            searchQuery
                .debounce(500)
                .collect { query ->
                    if (query.isNotBlank()) {
                        performSearch(query)
                    } else {
                        screenStateSearch.value = ScreenState.Initial
                    }
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        searchQuery.value = query
    }

    private suspend fun performSearch(query: String) {
        screenStateSearch.value = ScreenState.Loading
        try {
            val response = api.searchMovies(query)
            if (response.isSuccessful) {
                val films = response.body()?.films ?: emptyList()
                screenStateSearch.value = if (films.isEmpty()) {
                    ScreenState.Error("Ничего не найдено")
                } else {
                    ScreenState.Success(films)
                }
            } else {
                screenStateSearch.value =
                    ScreenState.Error("Ошибка сервера: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            screenStateSearch.value = ScreenState.Error("Ошибка сети: ${e.localizedMessage}")
        }
    }

    fun loadFilters() {
        filtersLoadingState.value = ScreenState.Loading
        viewModelScope.launch {
            try {
                val response = api.getFilters()
                if (response.isSuccessful) {
                    val filters = response.body()
                    if (filters != null) {
                        genres.postValue(filters.genres)
                        countries.postValue(filters.countries)
                        filtersLoadingState.value =
                            ScreenState.Success(Unit) as Nothing
                    } else {
                        filtersLoadingState.value = ScreenState.Error("Ответ сервера пустой")
                    }
                } else {
                    filtersLoadingState.value =
                        ScreenState.Error("Ошибка сервера: ${response.errorBody()?.string() ?: response.message()}")
                }
            } catch (e: Exception) {
                filtersLoadingState.value = ScreenState.Error("Ошибка сети: ${e.localizedMessage}")
            }
        }
    }
}
