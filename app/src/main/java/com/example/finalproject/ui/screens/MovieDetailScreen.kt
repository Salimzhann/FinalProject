package com.example.finalproject.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import com.example.finalproject.R
import com.example.finalproject.domain.model.FilmDetail
import com.example.finalproject.domain.model.ScreenState
import com.example.finalproject.ui.viewmodel.MainPageViewModel

@Composable
fun MovieDetailScreen(movieId: Long, viewModel: MainPageViewModel) {
    // Request movie details
    LaunchedEffect(movieId) {
        viewModel.loadFilmDetailById(movieId)
    }

    // Observe movie detail state
    val filmDetailState by viewModel.screenStateFilmDetail.observeAsState(ScreenState.Initial)

    Box(modifier = Modifier.fillMaxSize()) {
        when (filmDetailState) {
            is ScreenState.Success -> {
                val filmDetail = (filmDetailState as ScreenState.Success<FilmDetail>).data
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Top image background
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(filmDetail.posterUrl), // Replace with movie poster image if available
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = filmDetail.nameRu.toString(),
                                style = MaterialTheme.typography.headlineLarge,
                                color = Color.White
                            )
                            Text(
                                text = "Rating: ${filmDetail.ratingImdb}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White
                            )
                            Text(
                                text = "${filmDetail.year}, ${filmDetail.genres.joinToString(", ") { it.genre }}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Icons row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Like"
                        )
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Bookmark"
                        )
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = "Share"
                        )
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "More options"
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Description
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text(
                            text = "Plot Summary",
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = filmDetail.description ?: "No description available.",
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
            is ScreenState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            is ScreenState.Error -> Text("Failed to load details", color = Color.Red, modifier = Modifier.align(Alignment.Center))
            else -> Unit
        }
    }
}
