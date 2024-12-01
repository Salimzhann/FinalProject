package com.example.finalproject.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.data.repository.api
import com.example.finalproject.domain.model.ScreenState
import com.example.finalproject.domain.model.search.Film
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchViewModel : ViewModel() {
    val screenStateSearch = MutableLiveData<ScreenState<List<Film>>>(ScreenState.Initial)
    private val searchQuery = MutableStateFlow("")

    init {
        // Запускаем наблюдение за изменениями текста
        viewModelScope.launch {
            searchQuery
                .debounce(500) // Ожидание 500 мс после последнего изменения текста
                .collect { query ->
                    if (query.isNotBlank()) {
                        performSearch(query)
                    } else {
                        screenStateSearch.value = ScreenState.Initial
                    }
                }
        }
    }

    // Обновляем текст поиска
    fun onSearchQueryChanged(query: String) {
        searchQuery.value = query
    }

    // Выполняем запрос
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
}
