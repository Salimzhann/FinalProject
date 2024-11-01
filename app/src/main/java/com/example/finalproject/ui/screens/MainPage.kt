package com.example.finalproject.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.finalproject.domain.viewmodel.MainPageViewModel
import com.example.finalproject.R
import com.example.finalproject.domain.model.MovieItem

@Composable
fun SetupUI(viewModel: MainPageViewModel, navController: NavController) {
    val premieres by viewModel.premieres.observeAsState(emptyList())
    val popularCinema by viewModel.popularCinema.observeAsState(emptyList())
    val usaActionMovies by viewModel.usaActionMovies.observeAsState(emptyList())

    LazyColumn(modifier = Modifier.padding(top = 57.dp)) {
        item {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Постер фильма",
                modifier = Modifier.size(160.dp, 60.dp)
                    .padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            MovieSection("ТОП 250 ФИЛЬМОВ", viewModel.getLimitedMovies(premieres), navController, "ТОП 250 ФИЛЬМОВ")
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            MovieSection("Популярное", viewModel.getLimitedMovies(popularCinema), navController, "Популярное")
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            MovieSection("ТОП 250 СЕРИАЛОВ", viewModel.getLimitedMovies(usaActionMovies), navController, "ТОП 250 СЕРИАЛОВ")
        }
    }
}


@Composable
fun MovieSection(title: String, movies: List<MovieItem>, navController: NavController, category: String) {
    Column(modifier = Modifier.height(330.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
            TextButton(onClick = {
                navController.navigate("allMovies/$category")  // Навигация на экран с полным списком фильмов
            }) {
                Text(
                    text = "Все",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Blue,
                )
            }
        }
        LazyRow(contentPadding = PaddingValues(start = 10.dp)) {
            items(movies.size) { index ->
                MovieItemView(movie = movies[index])
            }
        }
    }
}