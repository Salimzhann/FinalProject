package com.example.finalproject.domain.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.domain.model.ScreenState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StateViewModel : ViewModel() {
    private val _screenState = mutableStateOf<ScreenState<String>>(ScreenState.Initial)
    val screenState: State<ScreenState<String>> = _screenState

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _screenState.value = ScreenState.Loading
            try {
                // Здесь можно добавить реальную логику загрузки данных
                delay(2000) // Имитация задержки сети
                _screenState.value = ScreenState.Success("Данные успешно загружены")
            } catch (e: Exception) {
                _screenState.value = ScreenState.Error("Ошибка загрузки данных: ${e.message}")
            }
        }
    }
}
