package com.example.finalproject.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.finalproject.domain.viewmodel.MainPageViewModel

@Composable
fun AllMoviesView(movies: List<MainPageViewModel.MovieItem>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(65.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        items(movies) { movie ->
            MovieItemView(movie = movie)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MoviesGrid() {
    AllMoviesView(MainPageViewModel().premieres)
}