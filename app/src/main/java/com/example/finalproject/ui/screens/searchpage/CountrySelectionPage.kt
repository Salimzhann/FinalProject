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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.finalproject.domain.model.filter.Country
import com.example.finalproject.ui.viewmodel.SearchViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce


@OptIn(FlowPreview::class)
@Composable
fun CountrySelectionPage(navController: NavController, viewModel: SearchViewModel) {
    val countries by viewModel.countries.observeAsState(emptyList())
    val selectedCountryId = viewModel.selectedCountryId.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }
    var filteredCountries by remember { mutableStateOf(countries) }

    LaunchedEffect(searchQuery, countries) {
        snapshotFlow { searchQuery }
            .debounce(300)
            .collect { query ->
                filteredCountries = countries.filter {
                    it.country.contains(query, ignoreCase = true)
                }
            }
    }

    LaunchedEffect(Unit) {
        if (countries.isEmpty()) {
            viewModel.loadFilters()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
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
                    viewModel.updateSelectedCountry(country.id, country.country)
                    navController.popBackStack()
                }
            }
        }
    }
}

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
