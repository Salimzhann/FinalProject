package com.example.finalproject.ui.screens.searchpage

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.finalproject.ui.viewmodel.SearchViewModel


@Composable
fun FilterPage(navController: NavController, viewModel: SearchViewModel) {
    val selectedCountryName by viewModel.selectedCountryName.collectAsStateWithLifecycle()
    val selectedGenreName by viewModel.selectedGenreName.collectAsStateWithLifecycle()

    var selectedCategory by remember { mutableStateOf("ALL") }
    var selectedSorting by remember { mutableStateOf("Дата") }
    var minRating by remember { mutableFloatStateOf(1f) }
    var maxRating by remember { mutableFloatStateOf(10f) }

    val selectedYearRange = viewModel.selectedYearRange

    Column(modifier = Modifier.fillMaxSize()) {
        Header(navController)
        Text(
            "Показывать",
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        CategoryButtons(selectedCategory) { category ->
            selectedCategory = category
        }
        Spacer(modifier = Modifier.height(16.dp))

        SelectableRow("Страна", selectedCountryName ?: "Выберите страну", onClick = {
            navController.navigate("country")
        })
        HorizontalDivider(color = Color.LightGray)

        SelectableRow("Жанр", selectedGenreName ?: "Выберите жанр", onClick = {
            navController.navigate("genre")
        })
        HorizontalDivider(color = Color.LightGray)

        SelectableRow("Год", selectedYearRange?.let {
            "с ${it.first} до ${it.second}"
        } ?: "Выберите год", onClick = {
            navController.navigate("year")
        })
        HorizontalDivider(color = Color.LightGray)

        Text(
            "Рейтинг",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp)
        )

        CustomRangeSlider(
            value = minRating..maxRating,
            onValueChange = { newRange ->
                minRating = newRange.start
                maxRating = newRange.endInclusive
            },
            valueRange = 1f..10f,
            steps = 1000,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "%.0f".format(minRating),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "%.0f".format(maxRating),
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = Color.LightGray)

        Text(
            "Сортировать",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        SortingButtons(selectedSorting) { sorting ->
            selectedSorting = sorting
        }
        Spacer(modifier = Modifier.height(25.dp))
        HorizontalDivider(color = Color.LightGray)
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                Log.d("MovieFilter", "Selected Country ID: ${viewModel.selectedCountryId.value}")
                Log.d("MovieFilter", "Selected Genre ID: ${viewModel.selectedGenreId.value}")
                Log.d("MovieFilter", "Year Range: $selectedYearRange")
                Log.d("MovieFilter", "Rating Range: $minRating to $maxRating")
                Log.d("MovieFilter", "Sorting: ${when (selectedSorting) {
                    "Дата" -> "YEAR"
                    "Популярность" -> "NUM_VOTE"
                    "Рейтинг" -> "RATING"
                    else -> "RATING"
                }}")
                Log.d("MovieFilter", "Category: $selectedCategory")
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
                navController.popBackStack()
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(width = 250.dp, height = 40.dp)
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3D3BFF))
        ) {
            Text(text = "Применить фильтры", color = Color.White)
        }
    }
}

@Composable
fun CategoryButtons(selectedCategory: String, onSelect: (String) -> Unit) {
    val categories = listOf("ALL", "FILM", "TV_SHOW")
    val displayTexts = listOf("Все", "Фильмы", "Сериалы")
    SegmentedControl(
        items = categories,
        displayTexts = displayTexts,
        selectedItem = selectedCategory,
        onItemSelected = onSelect,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@Composable
fun SortingButtons(selectedSorting: String, onSelect: (String) -> Unit) {
    val sortingOptions = listOf("Дата", "Популярность", "Рейтинг")
    SegmentedControl(
        items = sortingOptions,
        displayTexts = sortingOptions,
        selectedItem = selectedSorting,
        onItemSelected = onSelect,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@Composable
fun SegmentedControl(
    items: List<String>,
    displayTexts: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.dp, Color.Gray),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .height(40.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = selectedItem == item
                val backgroundColor = if (isSelected) Color(0xFF3D3BFF) else Color.Transparent
                val contentColor = if (isSelected) Color.White else Color.Black

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onItemSelected(item) }
                        .background(backgroundColor)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = displayTexts[index],
                        color = contentColor
                    )
                }

                if (index != items.lastIndex) {
                    Divider(
                        color = Color.Gray,
                        modifier = Modifier
                            .width(1.dp)
                            .fillMaxHeight()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomRangeSlider(
    modifier: Modifier = Modifier,
    value: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int = 0
) {
    RangeSlider(
        value = value,
        onValueChange = onValueChange,
        valueRange = valueRange,
        steps = steps,
        modifier = modifier,
        colors = SliderDefaults.colors(
            thumbColor = Color.White,
            activeTrackColor = Color.Transparent,
            inactiveTrackColor = Color.Transparent,
            activeTickColor = Color(0xFF3D3BFF),
            inactiveTickColor = Color.LightGray
        ),
        startThumb = {
            CustomThumb()
        },
        endThumb = {
            CustomThumb()
        }
    )
}

@Composable
fun SelectableRow(label: String, value: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
        Text(text = value, color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun Header(navController: NavController, title: String = "Настройки поиска") {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Back")
        }
        Spacer(modifier = Modifier.width(70.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CustomThumb() {
    Canvas(
        modifier = Modifier
            .size(24.dp)
    ) {
        drawCircle(
            color = Color.Black,
            radius = size.minDimension / 2,
            style = Stroke(width = 1.dp.toPx())
        )
    }
}
