package com.example.finalproject.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.finalproject.domain.model.ScreenState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finalproject.domain.viewmodel.FilmsViewModel

@Composable
fun FilmsScreen(viewModel: FilmsViewModel = viewModel()) {
    when (val state = viewModel.screenState.value) {
        is ScreenState.Initial -> Text("Welcome to Kinopoisk Films!")
        is ScreenState.Loading -> CircularProgressIndicator()
        is ScreenState.Success -> {
            LazyColumn {
                items(state.data) { film ->
                    Text(film.title) // Предполагается, что у Film есть свойство title
                }
            }
        }
        is ScreenState.Error -> Text("Error: ${state.message}")
    }
}


@Preview
@Composable
fun PreviewFilmsScreen() {
    FilmsScreen()
}
