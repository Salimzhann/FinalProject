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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
            .width(111.dp)
            .padding(4.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = movie.posterUrl),
            contentDescription = "Movie Poster",
            modifier = Modifier
                .height(156.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .padding(top = 0.dp, start = 4.dp, end = 4.dp)
        ) {
            Text(
                text = movie.nameRu ?: movie.nameOriginal,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 12.sp,
                maxLines = 2,
                modifier = Modifier.padding(vertical = 2.dp)
            )
            Text(
                text = movie.genres.joinToString { it.genre },
                fontSize = 10.sp,
                color = Color.Gray,
                maxLines = 1
            )
        }
    }
}
