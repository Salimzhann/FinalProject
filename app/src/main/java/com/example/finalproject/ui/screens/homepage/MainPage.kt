package com.example.finalproject.ui.screens.homepage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.finalproject.ui.viewmodel.MainPageViewModel
import com.example.finalproject.R
import com.example.finalproject.domain.model.MovieItem
import com.example.finalproject.domain.model.ScreenState

@Composable
fun SetupUI(viewModel: MainPageViewModel, navController: NavController) {
    val screenStatePremieres by viewModel.screenStatePremieres.observeAsState(ScreenState.Initial)
    val screenStatePopular by viewModel.screenStatePopular.observeAsState(ScreenState.Initial)
    val screenStateSeries by viewModel.screenStateSeries.observeAsState(ScreenState.Initial)

    val isAllDataLoaded = screenStatePremieres is ScreenState.Success &&
            screenStatePopular is ScreenState.Success &&
            screenStateSeries is ScreenState.Success

    val isLoading = screenStatePremieres is ScreenState.Loading ||
            screenStatePopular is ScreenState.Loading ||
            screenStateSeries is ScreenState.Loading

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (isAllDataLoaded) {
        LazyColumn(modifier = Modifier.padding(top = 57.dp)) {
            item {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "Постер фильма",
                    modifier = Modifier
                        .size(160.dp, 60.dp)
                        .padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                MovieSection(
                    "ТОП 250 ФИЛЬМОВ",
                    viewModel.getLimitedMovies((screenStatePremieres as ScreenState.Success).data),
                    navController,
                    "ТОП 250 ФИЛЬМОВ",
                    viewModel = viewModel
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                MovieSection(
                    "Популярное",
                    viewModel.getLimitedMovies((screenStatePopular as ScreenState.Success).data),
                    navController,
                    "Популярное",
                    viewModel = viewModel
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                MovieSection(
                    "ТОП 250 СЕРИАЛОВ",
                    viewModel.getLimitedMovies((screenStateSeries as ScreenState.Success).data),
                    navController,
                    "ТОП 250 СЕРИАЛОВ",
                    viewModel = viewModel
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                Text(
                    text = "Ошибка загрузки данных. Пожалуйста, попробуйте снова.",
                    color = Color.Red,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun MovieSection(title: String, movies: List<MovieItem>, navController: NavController, category: String, viewModel: MainPageViewModel) {
    Column(modifier = Modifier.height(270.dp)) {
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
                navController.navigate("allMovies/$category")
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
                MovieItemView(movie = movies[index]) {
                    navController.navigate("movieDetail/${movies[index].kinopoiskId}")
                }
            }
        }
    }
}
