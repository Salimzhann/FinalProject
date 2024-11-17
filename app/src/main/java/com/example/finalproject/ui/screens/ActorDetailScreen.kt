package com.example.finalproject.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.finalproject.domain.model.FilmBrief
import com.example.finalproject.ui.viewmodel.MainPageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActorDetailScreen(
    staffId: Int,
    viewModel: MainPageViewModel,
    onNavigateBack: () -> Unit,
    navController: NavController
) {
    val actorDetail by viewModel.actorDetails.observeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            LaunchedEffect(staffId) {
                viewModel.loadActorDetails(staffId)
            }

            actorDetail?.let { actor ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Image(
                            painter = rememberAsyncImagePainter(actor.posterUrl),
                            contentDescription = null,
                            modifier = Modifier
                                .size(150.dp, 240.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Gray)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = actor.nameRu ?: actor.nameEn ?: "Актёр",
                                style = MaterialTheme.typography.h5,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = actor.profession,
                                style = MaterialTheme.typography.body2,
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Лучшее",
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold
                        )
                        TextButton(onClick = {
                        }) {
                            Text(
                                text = "Все >",
                                color = Color.Blue,
                                style = MaterialTheme.typography.body2
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        val topFilms = actor.films.sortedByDescending { it.rating?.toDoubleOrNull() ?: 0.0 }.take(8)
                        items(topFilms.size) { index ->
                            FilmBriefCard(topFilms[index])
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Фильмография",
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold
                        )
                        TextButton(onClick = {
                            navController.navigate("filmography/$staffId")
                        }) {
                            Text(
                                text = "К списку",
                                color = Color.Blue,
                                style = MaterialTheme.typography.body2
                            )
                        }
                    }
                    Text(
                        text = "${actor.films.size} фильма",
                        style = MaterialTheme.typography.body2,
                        color = Color.Gray
                    )
                }
            } ?: run {
                Text(text = "Loading...")
            }
        }
    )
}

@Composable
fun FilmBriefCard(film: FilmBrief) {
    Column(
        modifier = Modifier
            .width(111.dp)
            .height(230.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .size(105.dp, 132.dp)
                .background(Color.LightGray),
            contentAlignment = Alignment.TopEnd
        ) {
            Text(
                text = film.rating ?: "N/A",
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .background(color = Color(0xFF3D3BFF), shape = RoundedCornerShape(20)),
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
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