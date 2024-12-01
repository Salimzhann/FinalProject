package com.example.finalproject.ui.screens.profilepage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.finalproject.domain.model.MovieItem
import com.example.finalproject.ui.viewmodel.MainPageViewModel

@Composable
fun ProfileScreen(viewModel: MainPageViewModel) {
    val watchedMovies by viewModel.watchedMovies.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Title Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Просмотрено",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = {}) {
                Text(
                    text = "${watchedMovies.size} >",
                    color = Color.Blue,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(watchedMovies.size) { index ->
                MovieProfileCard(movie = watchedMovies[index])
            }

            item {
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(180.dp)
                        .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                        .clickable { viewModel.clearWatchedMovies() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Очистить\nвсё",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun MovieProfileCard(movie: MovieItem) {
    Column(
        modifier = Modifier
            .width(120.dp)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .height(150.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = movie.posterUrl),
                contentDescription = "Movie Poster",
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(Color(0xFF3D3BFF), shape = RoundedCornerShape(50))
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            ) {
                Text(
                    text = movie.ratingKinopoisk?.toString() ?: "N/A",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = movie.nameRu ?: movie.nameOriginal ?: "Untitled",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        Text(
            text = movie.genres.joinToString { it.genre },
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}




