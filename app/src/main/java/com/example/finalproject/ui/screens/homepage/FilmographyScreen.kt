package com.example.finalproject.ui.screens.homepage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finalproject.domain.model.FilmBrief
import com.example.finalproject.ui.viewmodel.MainPageViewModel

@Composable
fun FilmographyItem(film: FilmBrief) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(97.dp, 132.dp)
                .background(Color.LightGray)
                .padding(4.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Text(
                text = film.rating ?: "N/A",
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .background(Color.Gray, shape = RoundedCornerShape(20))
                    .padding(4.dp),
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = film.nameRu ?: film.nameEn ?: "",
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = film.description,
                style = MaterialTheme.typography.body2,
                color = Color.Gray
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmographyScreen(
    actorId: Int,
    onNavigateBack: ()-> Unit
) {
    val viewModel: MainPageViewModel = viewModel()
    val actorDetail by viewModel.actorDetails.observeAsState()
    LaunchedEffect(actorId) {
        viewModel.loadActorDetails(actorId)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 48.dp)
                    ) {
                        Text(
                            text = "Фильмография",
                            fontSize = 14.sp,
                            fontWeight = W600,
                            modifier = Modifier.align(Alignment.Center),
                            textAlign = TextAlign.Center
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            actorDetail?.let { actor ->
                val professionKeys = actor.films.map { it.professionKey }.distinct()
                var selectedProfession by remember { mutableStateOf(professionKeys.firstOrNull() ?: "") }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                ) {
                    Text(
                        text = actor.nameRu ?: actor.nameEn ?: "Актёр",
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyRow(
                        modifier = Modifier.padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(professionKeys) { professionKey ->
                            val count = actor.films.count { it.professionKey == professionKey }
                            Button(
                                onClick = { selectedProfession = professionKey },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (professionKey == selectedProfession) Color.Blue else Color.White,
                                    contentColor = if (professionKey == selectedProfession) Color.White else Color.Black
                                ),
                                modifier = Modifier.padding(horizontal = 4.dp)
                            ) {
                                Row {
                                    Text("$professionKey ")
                                    Text(
                                        text = "$count",
                                        color = Color.Gray)
                                }
                            }
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        items(actor.films.filter { it.professionKey == selectedProfession }) { film ->
                            FilmographyItem(film)
                        }
                    }
                }
            }
        }
    )
}
