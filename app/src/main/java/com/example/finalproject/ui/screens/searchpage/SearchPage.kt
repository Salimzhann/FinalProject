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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.finalproject.domain.model.ScreenState
import com.example.finalproject.domain.model.search.Film
import com.example.finalproject.ui.viewmodel.SearchViewModel

@Composable
fun SearchPage(navController: NavController, viewModel: SearchViewModel) {
    var searchText by remember { mutableStateOf("") }
    val screenState by viewModel.screenStateSearch.observeAsState(ScreenState.Initial)

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
                Text("Введите ключевые слова для поиска")
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
            Text(film.description ?: "", style = MaterialTheme.typography.bodySmall)
        }
    }
}


@Composable
fun FilterPage(navController: NavController) {
    // Состояния для кнопок фильтров и выбора страны/жанра
    var selectedCategory by remember { mutableStateOf("All") }
    val selectedCountry by remember { mutableStateOf("Россия") }
    val selectedGenre by remember { mutableStateOf("Action") }
    var selectedSorting by remember { mutableStateOf("Дата") }

    // Состояния для минимального и максимального рейтинга
    var minRating by remember { mutableFloatStateOf(1f) }
    var maxRating by remember { mutableFloatStateOf(10f) }

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
            modifier = Modifier.fillMaxWidth()
                .clickable{navController.navigate("country")}
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Страна", modifier = Modifier.weight(1f))
            Text(text = selectedCountry, color = Color.Gray)
        }
        HorizontalDivider(color = Color.Gray)

        // Жанр
        Row(
            modifier = Modifier.fillMaxWidth()
                .clickable{navController.navigate("genre")}
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Жанр", modifier = Modifier.weight(1f))
            Text(text = selectedGenre, color = Color.Gray)
        }
        HorizontalDivider(color = Color.Gray)

        // Год
        Row(
            modifier = Modifier.fillMaxWidth()
                .clickable{navController.navigate("year")}
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Год", modifier = Modifier.weight(1f))
            Text(text = "Выберите год", color = Color.Gray)
        }
        HorizontalDivider(color = Color.Gray)

        // Рейтинг (с одним слайдером с двумя ползунками)
        Text(text = "Рейтинг", style = MaterialTheme.typography.bodyMedium)

        // Слайдер с двумя ползунками
        RangeSlider(
            value = minRating..maxRating, // Диапазон значений с ползунками
            onValueChange = { newRange ->
                minRating = newRange.start
                maxRating = newRange.endInclusive
            },
            valueRange = 1f..10f, // Диапазон значений
            steps = 8,
            modifier = Modifier.fillMaxWidth()
        )

        // Отображение значений
        Text(text = "Минимум: ${"%.1f".format(minRating)}", style = MaterialTheme.typography.bodySmall)
        Text(text = "Максимум: ${"%.1f".format(maxRating)}", style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = Color.Gray)

        // Сортировка
        Text(text = "Сортировать", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))

        // Кнопки для сортировки
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

        // Кнопка для применения фильтров
        Button(
            onClick = { navController.popBackStack() },
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
@Composable
fun CountrySelectionPage(navController: NavController) {
    val countries = listOf("Россия", "США", "Великобритания", "Канада", "Германия")
    var selectedCountry by remember { mutableStateOf("Россия") }
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Заголовок с кнопкой "Назад"
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

        // TextField для поиска стран
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
            items(countries.filter { it.contains(searchQuery, ignoreCase = true) }) { country ->
                CountryListItem(country, selectedCountry) {
                    selectedCountry = country
                    navController.popBackStack() // Возвращаемся на предыдущую страницу с выбранной страной
                }
            }
        }
    }
}

@Composable
fun CountryListItem(country: String, selectedCountry: String, onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = country,
                modifier = Modifier.weight(1f),
                style = if (selectedCountry == country) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium
            )
        }
        HorizontalDivider(color = Color.Gray)
    }
}
@Composable
fun GenreSelectionPage(navController: NavController) {
    val genres = listOf("Драма", "Комедия", "Экшен", "Триллер", "Фантастика")
    var selectedGenre by remember { mutableStateOf("Любой") }
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Заголовок с кнопкой "Назад"
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

        // TextField для поиска жанров
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
            items(genres.filter { it.contains(searchQuery, ignoreCase = true) }) { genre ->
                GenreListItem(genre, selectedGenre) {
                    selectedGenre = genre
                    navController.popBackStack() // Возвращаемся на предыдущую страницу с выбранным жанром
                }
            }
        }
    }
}

@Composable
fun GenreListItem(genre: String, selectedGenre: String, onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = genre,
                modifier = Modifier.weight(1f),
                style = if (selectedGenre == genre) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium
            )
        }
        HorizontalDivider(color = Color.Gray)
    }
}
@Composable
fun YearSelectionPage(navController: NavController) {
    val years = (1900..2024).toList() // Пример списка годов
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
        YearSelector(startYear, yearsInCards, startYearCardIndex, {
            startYearCardIndex = it
        }) { selectedYear ->
            startYear = selectedYear
        }

        Text("Искать в период до", color = Color.Gray, modifier = Modifier.padding(8.dp))
        YearSelector(endYear, yearsInCards, endYearCardIndex, {
            endYearCardIndex = it
        }) { selectedYear ->
            endYear = selectedYear
        }

        Button(
            onClick = {
                println("Выбран период с $startYear по $endYear")
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
            IconButton(onClick = { if (cardIndex > 0) onCardChange(cardIndex - 1) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
            }
            IconButton(onClick = { if (cardIndex < yearsInCards.size - 1) onCardChange(cardIndex + 1) }) {
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
                        .background(if (year == selectedYear) Color.Cyan else Color.LightGray, shape = RoundedCornerShape(8.dp))
                        .clickable { onYearSelected(year) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(year.toString(), style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
