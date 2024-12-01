package com.example.finalproject.ui.screens.searchpage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.finalproject.ui.viewmodel.SearchViewModel


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
