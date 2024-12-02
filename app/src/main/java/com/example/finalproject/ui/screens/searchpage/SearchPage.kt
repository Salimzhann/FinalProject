package com.example.finalproject.ui.screens.searchpage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.finalproject.domain.model.ScreenState
import com.example.finalproject.domain.model.search.Film
import com.example.finalproject.domain.model.search.Movie
import com.example.finalproject.ui.viewmodel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPage(navController: NavController, viewModel: SearchViewModel) {
    var searchText by remember { mutableStateOf("") }
    val screenState by viewModel.screenStateSearch.observeAsState(ScreenState.Initial)
    val filteredMoviesState by viewModel.filteredMoviesState.observeAsState(ScreenState.Initial)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Верхняя часть страницы с SearchBar
        SearchBar(
            expanded = true, // Можно сделать раскрывающимся, если потребуется
            onExpandedChange = { /* Handle expanded state */ },
            inputField = {
                // Поисковое поле
                OutlinedTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                        viewModel.onSearchQueryChanged(it) // Обновляем состояние текста в ViewModel
                    },
                    placeholder = { Text("Фильмы, актёры, режиссёры") },
                    leadingIcon = {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    },
                    trailingIcon = {
                        // Разделитель
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(1.dp)
                                .padding(vertical = 8.dp),
                            color = Color.Gray
                        )
                        // Иконка фильтра
                        IconButton(onClick = { navController.navigate("filter") }) {
                            Icon(Icons.Filled.FilterList, contentDescription = "Filter")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp), // Высота для более аккуратного вида
                    shape = MaterialTheme.shapes.medium
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large, // Округлые края
            tonalElevation = 4.dp,
            shadowElevation = 2.dp,
            content = {}
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Отображение состояния экрана
        when (screenState) {
            is ScreenState.Initial -> {
            }
            is ScreenState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is ScreenState.Success -> {
                val films = (screenState as ScreenState.Success<List<Film>>).data
                if (films.isEmpty()) {
                    Text("Ничего не найдено", modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    LazyColumn {
                        items(films) { film ->
                            FilmItem(film)
                        }
                    }
                }
            }
            is ScreenState.Error -> {
                Text(
                    text = (screenState as ScreenState.Error).message,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
        // Отображение фильмов
        when (filteredMoviesState) {
            is ScreenState.Initial -> {}
            is ScreenState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            is ScreenState.Success -> {
                val movies = (filteredMoviesState as ScreenState.Success<List<Movie>>).data
                LazyColumn {
                    items(movies) { movie ->
                        MovieItem(movie)
                    }
                }
            }
            is ScreenState.Error -> {
                Text(
                    text = (filteredMoviesState as ScreenState.Error).message,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}


@Preview
@Composable
fun PreviewSearchPage() {
    val navController = rememberNavController()
    val viewModel = SearchViewModel() // Предположим, что у вас есть соответствующий ViewModel

    SearchPage(navController = navController, viewModel = viewModel)
}

@Composable
fun MovieItem(movie: Movie) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Блок для изображения с рейтингом
        Box(
            modifier = Modifier
                .size(80.dp)
        ) {
            // Изображение фильма
            Image(
                painter = rememberAsyncImagePainter(movie.posterUrlPreview),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )

            // Рейтинг в верхнем левом углу
            Box(
                modifier = Modifier
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(4.dp)
                    .align(Alignment.TopStart)
            ) {
                Text(
                    text = movie.ratingKinopoisk?.toString() ?: "N/A",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Колонка с названием, годом и жанром
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = movie.nameRu ?: movie.nameEn ?: "Без названия",
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${movie.year ?: "N/A"}, ${movie.genres.joinToString { it.genre }}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}


@Composable
fun FilmItem(film: Film) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Блок для изображения с рейтингом
        Box(
            modifier = Modifier
                .size(80.dp)
        ) {
            // Изображение фильма
            Image(
                painter = rememberAsyncImagePainter(film.posterUrlPreview),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )

            // Рейтинг в верхнем левом углу
            Box(
                modifier = Modifier
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(4.dp)
                    .align(Alignment.TopStart)
            ) {
                Text(
                    text = film.rating ?: "N/A",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Колонка с названием, годом и жанром
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = film.nameRu ?: film.nameEn ?: "Без названия",
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${film.year ?: "N/A"}, ${film.genres.joinToString { it.genre }}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}
