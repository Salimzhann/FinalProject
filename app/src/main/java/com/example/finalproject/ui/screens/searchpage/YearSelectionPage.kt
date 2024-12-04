package com.example.finalproject.ui.screens.searchpage

import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.finalproject.ui.viewmodel.SearchViewModel

@Composable
fun YearSelectionPage(navController: NavController, viewModel: SearchViewModel) {
    val years = (1900..2024).toList()
    val yearsInCards = years.chunked(12)
    var startYear by remember { mutableStateOf<Int?>(null) }
    var endYear by remember { mutableStateOf<Int?>(null) }

    var startYearCardIndex by remember { mutableIntStateOf(0) }
    var endYearCardIndex by remember { mutableIntStateOf(yearsInCards.size - 1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopAppBar(
            title = { Text(
                text = "Период",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 85.dp)
            ) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Назад")
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
            onCardChange = { newIndex ->
                if (newIndex in yearsInCards.indices) startYearCardIndex = newIndex
            },
            onYearSelected = { selectedYear -> startYear = selectedYear }
        )

        Text("Искать в период до", color = Color.Gray, modifier = Modifier.padding(8.dp))
        YearSelector(
            selectedYear = endYear,
            yearsInCards = yearsInCards,
            cardIndex = endYearCardIndex,
            onCardChange = { newIndex ->
                if (newIndex in yearsInCards.indices) endYearCardIndex = newIndex
            },
            onYearSelected = { selectedYear -> endYear = selectedYear }
        )

        Button(
            onClick = {
                if (startYear != null && endYear != null) {
                    viewModel.updateYearRange(startYear!!, endYear!!)
                }
                navController.popBackStack()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
            modifier = Modifier
                .padding(top = 16.dp)
                .align(Alignment.CenterHorizontally)
                .size(width = 150.dp, height = 40.dp)
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
                enabled = cardIndex > 0
            ) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Назад")
            }
            IconButton(
                onClick = { if (cardIndex < yearsInCards.size - 1) onCardChange(cardIndex + 1) },
                enabled = cardIndex < yearsInCards.size - 1
            ) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Вперед")
            }
        }
        val currentYears = yearsInCards[cardIndex]
        val minYear = currentYears.minOrNull()
        val maxYear = currentYears.maxOrNull()
        val rangeText = if (minYear != null && maxYear != null) "$minYear - $maxYear" else ""

        Box(
            modifier = Modifier
                .padding(8.dp)
                .border(2.dp, Color.Black, RoundedCornerShape(8.dp))
        ) {
            Text(
                text = rangeText,
                color = Color.Blue,
                modifier = Modifier
                    .padding(8.dp),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .padding(top = 40.dp, start = 8.dp, end = 8.dp, bottom = 16.dp)
            ) {
                items(currentYears) { year ->
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(32.dp)
                            .clickable { onYearSelected(year) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            year.toString(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (year == selectedYear) Color.Blue else Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
