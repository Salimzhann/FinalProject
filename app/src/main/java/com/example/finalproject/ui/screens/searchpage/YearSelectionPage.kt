package com.example.finalproject.ui.screens.searchpage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.finalproject.ui.viewmodel.SearchViewModel


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

