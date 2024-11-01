package com.example.finalproject.domain.model

sealed interface ScreenState {
    data object Initial : ScreenState
    data object Loading : ScreenState
    data class Success(val data: List<MovieItem>) : ScreenState
    data class Error(val message: String) : ScreenState
}
