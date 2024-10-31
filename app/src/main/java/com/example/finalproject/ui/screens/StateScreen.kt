package com.example.finalproject.ui.screens

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.finalproject.domain.model.ScreenState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finalproject.domain.viewmodel.StateViewModel

@Composable
fun StateScreen(viewModel: StateViewModel = viewModel()) {
    when (val state = viewModel.screenState.value) {
        is ScreenState.Initial -> Text("Добро пожаловать!")
        is ScreenState.Loading -> CircularProgressIndicator()
        is ScreenState.Success -> Text(state.data)
        is ScreenState.Error -> Text(state.message)
    }
}

@Preview
@Composable
fun PreviewMainPage() {
    MainPage()
}
