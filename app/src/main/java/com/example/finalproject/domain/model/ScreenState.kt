package com.example.finalproject.domain.model

sealed class ScreenState<out T> {
    data object Initial : ScreenState<Nothing>()
    data object Loading : ScreenState<Nothing>()
    data class Success<T>(val data: T) : ScreenState<T>()
    data class Error(val message: String) : ScreenState<Nothing>()
}
