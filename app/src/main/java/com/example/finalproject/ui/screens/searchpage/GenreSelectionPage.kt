package com.example.finalproject.ui.screens.searchpage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.finalproject.domain.model.filter.Genre
import com.example.finalproject.ui.viewmodel.SearchViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce

@OptIn(FlowPreview::class)
@Composable
fun GenreSelectionPage(navController: NavController, viewModel: SearchViewModel) {
    val genres by viewModel.genres.observeAsState(emptyList())
    val selectedGenreId = viewModel.selectedGenreId.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }
    var filteredGenres by remember { mutableStateOf(genres) }

    LaunchedEffect(searchQuery, genres) {
        snapshotFlow { searchQuery }
            .debounce(300)
            .collect { query ->
                filteredGenres = genres.filter {
                    it.genre.contains(query, ignoreCase = true)
                }
            }
    }

    LaunchedEffect(Unit) {
        if (genres.isEmpty()) {
            viewModel.loadFilters()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(120.dp))
            Text(
                text = "Жанр",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }

        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Введите жанр", color = Color.Gray) },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Gray)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(36.dp),
            colors = TextFieldDefaults.colors(unfocusedContainerColor = Color(0x66B5B5C9), focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent)
        )

        Spacer(modifier = Modifier.height(50.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(filteredGenres) { genre ->
                GenreListItem(genre, selectedGenreId.value) {
                    viewModel.updateSelectedGenre(genre.id, genre.genre)
                    navController.popBackStack()
                }
            }
        }
    }
}

@Composable
fun GenreListItem(genre: Genre, selectedGenreId: Int?, onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(16.dp)
                .height(40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = genre.genre,
                modifier = Modifier.weight(1f),
                style = if (selectedGenreId == genre.id) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyLarge
            )
        }
        HorizontalDivider(color = Color.Gray)
    }
}
