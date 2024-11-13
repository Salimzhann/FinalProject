package com.example.finalproject.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.finalproject.domain.model.FilmDetail
import com.example.finalproject.domain.model.ScreenState
import com.example.finalproject.ui.viewmodel.MainPageViewModel

@Composable
fun MovieDetailScreen(movieId: Long, viewModel: MainPageViewModel) {
    // Request movie details
    LaunchedEffect(movieId) {
        viewModel.loadFilmDetailById(movieId)
    }

    // Observe movie detail state
    val filmDetailState by viewModel.screenStateFilmDetail.observeAsState(ScreenState.Initial)

    when (filmDetailState) {
        is ScreenState.Success -> {
            val filmDetail = (filmDetailState as ScreenState.Success<FilmDetail>).data
            // Display detailed information about the film
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Title: ${filmDetail.nameRu}")
                Text("Year: ${filmDetail.year}")
                // Add more details as needed
            }
        }
        is ScreenState.Loading -> CircularProgressIndicator()
        is ScreenState.Error -> Text("Failed to load details")
        else -> Unit
    }
}
