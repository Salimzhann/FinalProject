package com.example.finalproject.ui.screens.searchpage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.finalproject.domain.model.ScreenState
import com.example.finalproject.domain.model.filter.Country
import com.example.finalproject.domain.model.filter.Genre
import com.example.finalproject.domain.model.search.Film
import com.example.finalproject.domain.model.search.Movie
import com.example.finalproject.ui.viewmodel.SearchViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce

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
        // Верхняя часть страницы с TextField
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                    viewModel.onSearchQueryChanged(it) // Обновляем состояние текста в ViewModel
                },
                label = { Text("Search movies") },
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = { navController.navigate("filter") }
            ) {
                Icon(Icons.Default.FilterList, contentDescription = "Filter")
            }
        }

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

@Composable
fun MovieItem(movie: Movie) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(movie.posterUrlPreview),
            contentDescription = null,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(movie.nameRu ?: movie.nameEn ?: "Без названия", style = MaterialTheme.typography.bodyLarge)
            Text("Год: ${movie.year ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
            Text("Рейтинг: ${movie.ratingKinopoisk ?: "N/A"}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun FilmItem(film: Film) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(film.posterUrlPreview),
            contentDescription = null,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(film.nameRu ?: film.nameEn ?: "Без названия", style = MaterialTheme.typography.bodyLarge)
            Text("Год: ${film.year}", style = MaterialTheme.typography.bodyMedium)
            Text("Рейтинг: ${film.rating ?: "N/A"}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun FilterPage(navController: NavController, viewModel: SearchViewModel) {
    // Получаем данные из ViewModel
    val selectedCountryName by viewModel.selectedCountryName.collectAsStateWithLifecycle() // Имя страны
    val selectedGenreName by viewModel.selectedGenreName.collectAsStateWithLifecycle() // Имя жанра

    var selectedCategory by remember { mutableStateOf("All") }
    var selectedSorting by remember { mutableStateOf("Дата") }
    var minRating by remember { mutableFloatStateOf(1f) }
    var maxRating by remember { mutableFloatStateOf(10f) }

    // Состояние для выбранного диапазона годов
    val selectedYearRange = viewModel.selectedYearRange

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Заголовок страницы с кнопкой назад и текстом
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Настройки поиска", style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Серый текст Показывать
        Text(text = "Показывать", color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))

        // Кнопки: Все, Фильмы, Сериалы
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            CategoryButton("Все", selectedCategory == "All") { selectedCategory = "All" }
            CategoryButton("Фильмы", selectedCategory == "Movies") { selectedCategory = "Movies" }
            CategoryButton("Сериалы", selectedCategory == "TV Shows") { selectedCategory = "TV Shows" }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = Color.Gray)

        // Страна
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate("country") }
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Страна", modifier = Modifier.weight(1f))
            Text(
                text = selectedCountryName ?: "Выберите страну", // Отображаем имя страны или текст-заглушку
                color = Color.Gray
            )
        }
        HorizontalDivider(color = Color.Gray)

        // Жанр
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate("genre") }
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Жанр", modifier = Modifier.weight(1f))
            Text(
                text = selectedGenreName ?: "Выберите жанр", // Отображаем имя жанра или текст-заглушку
                color = Color.Gray
            )
        }
        HorizontalDivider(color = Color.Gray)

        // Год
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate("year") }
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Год", modifier = Modifier.weight(1f))
            Text(
                text = selectedYearRange?.let {
                    "Выбранный год с ${it.first} по ${it.second}"
                } ?: "Выберите год",
                color = Color.Gray
            )
        }

        HorizontalDivider(color = Color.Gray)

        // Рейтинг (оставлено без изменений)
        Text(text = "Рейтинг", style = MaterialTheme.typography.bodyMedium)

        RangeSlider(
            value = minRating..maxRating,
            onValueChange = { newRange ->
                minRating = newRange.start
                maxRating = newRange.endInclusive
            },
            valueRange = 1f..10f,
            steps = 8,
            modifier = Modifier.fillMaxWidth()
        )

        Text(text = "Минимум: ${"%.1f".format(minRating)}", style = MaterialTheme.typography.bodySmall)
        Text(text = "Максимум: ${"%.1f".format(maxRating)}", style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = Color.Gray)

        // Сортировка (оставлено без изменений)
        Text(text = "Сортировать", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            SortingButton("Дата", selectedSorting == "Дата") { selectedSorting = "Дата" }
            SortingButton("Популярность", selectedSorting == "Популярность") { selectedSorting = "Популярность" }
            SortingButton("Рейтинг", selectedSorting == "Рейтинг") { selectedSorting = "Рейтинг" }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = Color.Gray)

        Button(
            onClick = {
                viewModel.loadFilteredMovies(
                    countryId = viewModel.selectedCountryId.value,
                    genreId = viewModel.selectedGenreId.value,
                    yearRange = selectedYearRange,
                    ratingRange = minRating to maxRating,
                    sorting = when (selectedSorting) {
                        "Дата" -> "YEAR"
                        "Популярность" -> "NUM_VOTE"
                        "Рейтинг" -> "RATING"
                        else -> "RATING"
                    },
                    category = selectedCategory
                )
                navController.popBackStack() // Возвращаемся на SearchPage
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Применить фильтры")
        }
    }
}
// Компонент для кнопки выбора категории
@Composable
fun CategoryButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val buttonColor = if (isSelected) Color.Blue else Color.Gray
    TextButton(onClick = onClick) {
        Text(
            text = text,
            color = buttonColor,
            modifier = Modifier.padding(8.dp)
        )
    }
}

// Компонент для кнопки сортировки
@Composable
fun SortingButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val buttonColor = if (isSelected) Color.Blue else Color.Gray
    TextButton(onClick = onClick) {
        Text(
            text = text,
            color = buttonColor,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun RangeSlider(
    modifier: Modifier = Modifier,  // Модификатор
    value: ClosedFloatingPointRange<Float>,  // Пара значений для минимального и максимального ползунка
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,  // Обработчик изменений значений
    valueRange: ClosedFloatingPointRange<Float>,  // Диапазон значений
    steps: Int = 0  // Количество шагов между ползунками
) {
    androidx.compose.material3.RangeSlider(
        value = value,  // Передаем диапазон значений
        onValueChange = onValueChange,  // Обработчик изменений
        valueRange = valueRange,  // Диапазон значений
        steps = steps,  // Количество шагов
        modifier = modifier  // Модификатор
    )
}

@OptIn(FlowPreview::class)
@Composable
fun CountrySelectionPage(navController: NavController, viewModel: SearchViewModel) {
    val countries by viewModel.countries.observeAsState(emptyList()) // Список стран
    val selectedCountryId = viewModel.selectedCountryId.collectAsStateWithLifecycle() // ID выбранной страны
    var searchQuery by remember { mutableStateOf("") }
    var filteredCountries by remember { mutableStateOf(countries) }

    // Реализация дебаунса
    LaunchedEffect(searchQuery, countries) {
        snapshotFlow { searchQuery }
            .debounce(300) // Задержка в миллисекундах
            .collect { query ->
                filteredCountries = countries.filter {
                    it.country.contains(query, ignoreCase = true)
                }
            }
    }

    LaunchedEffect(Unit) {
        if (countries.isEmpty()) {
            viewModel.loadFilters() // Загружаем фильтры при первой загрузке
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Верхняя панель с кнопкой "Назад"
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Страна", style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Поле для поиска
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Введите страну") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Список стран
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(filteredCountries) { country ->
                CountryListItem(country, selectedCountryId.value) {
                    viewModel.updateSelectedCountry(country.id, country.country) // Сохраняем ID и имя страны
                    navController.popBackStack()
                }
            }
        }
    }
}

// Элемент списка стран
@Composable
fun CountryListItem(country: Country, selectedCountryId: Int?, onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = country.country,
                modifier = Modifier.weight(1f),
                style = if (selectedCountryId == country.id) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium
            )
        }
        HorizontalDivider(color = Color.Gray)
    }
}
@OptIn(FlowPreview::class)
@Composable
fun GenreSelectionPage(navController: NavController, viewModel: SearchViewModel) {
    val genres by viewModel.genres.observeAsState(emptyList())
    val selectedGenreId = viewModel.selectedGenreId.collectAsStateWithLifecycle() // ID выбранного жанра
    var searchQuery by remember { mutableStateOf("") }
    var filteredGenres by remember { mutableStateOf(genres) }

    // Реализация дебаунса
    LaunchedEffect(searchQuery, genres) {
        snapshotFlow { searchQuery }
            .debounce(300) // Задержка в миллисекундах
            .collect { query ->
                filteredGenres = genres.filter {
                    it.genre.contains(query, ignoreCase = true)
                }
            }
    }

    LaunchedEffect(Unit) {
        if (genres.isEmpty()) {
            viewModel.loadFilters() // Загружаем фильтры при первой загрузке
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Верхняя панель с кнопкой "Назад"
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Жанр", style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Поле для поиска
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Введите жанр") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Список жанров
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(filteredGenres) { genre ->
                GenreListItem(genre, selectedGenreId.value) {
                    viewModel.updateSelectedGenre(genre.id, genre.genre) // Сохраняем ID и имя жанра
                    navController.popBackStack()
                }
            }
        }
    }
}

// Элемент списка жанров
@Composable
fun GenreListItem(genre: Genre, selectedGenreId: Int?, onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = genre.genre,
                modifier = Modifier.weight(1f),
                style = if (selectedGenreId == genre.id) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium
            )
        }
        HorizontalDivider(color = Color.Gray)
    }
}

@Composable
fun YearSelectionPage(navController: NavController, viewModel: SearchViewModel) {
    val years = (1900..2024).toList()
    var startYear by remember { mutableStateOf<Int?>(null) }
    var endYear by remember { mutableStateOf<Int?>(null) }
    var startYearCardIndex by remember { mutableIntStateOf(0) }
    var endYearCardIndex by remember { mutableIntStateOf(0) }
    val yearsInCards = years.chunked(12)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopAppBar(
            title = { Text("Выбор периода") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                }
            },
            backgroundColor = Color.Transparent,
            elevation = 0.dp
        )

        Text("Искать в период с", color = Color.Gray, modifier = Modifier.padding(8.dp))
        YearSelector(
            selectedYear = startYear,
            yearsInCards = yearsInCards,
            cardIndex = startYearCardIndex,
            onCardChange = { startYearCardIndex = it }, // Обновляем состояние индекса
            onYearSelected = { selectedYear -> startYear = selectedYear }
        )

        Text("Искать в период до", color = Color.Gray, modifier = Modifier.padding(8.dp))
        YearSelector(
            selectedYear = endYear,
            yearsInCards = yearsInCards,
            cardIndex = endYearCardIndex,
            onCardChange = { endYearCardIndex = it }, // Обновляем состояние индекса
            onYearSelected = { selectedYear -> endYear = selectedYear }
        )

        Button(
            onClick = {
                if (startYear != null && endYear != null) {
                    viewModel.updateYearRange(startYear!!, endYear!!)
                }
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Выбрать")
        }
    }
}


@Composable
fun YearSelector(
    selectedYear: Int?,
    yearsInCards: List<List<Int>>,
    cardIndex: Int,
    onCardChange: (Int) -> Unit,
    onYearSelected: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = { if (cardIndex > 0) onCardChange(cardIndex - 1) },
                enabled = cardIndex > 0 // Деактивируем кнопку, если это первая карточка
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
            }
            IconButton(
                onClick = { if (cardIndex < yearsInCards.size - 1) onCardChange(cardIndex + 1) },
                enabled = cardIndex < yearsInCards.size - 1 // Деактивируем кнопку, если это последняя карточка
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Вперед")
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.padding(8.dp)
        ) {
            items(yearsInCards[cardIndex]) { year ->
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(48.dp)
                        .background(
                            if (year == selectedYear) Color.Cyan else Color.LightGray,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { onYearSelected(year) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(year.toString(), style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

