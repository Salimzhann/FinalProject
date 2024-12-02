package com.example.finalproject.ui.screens.profilepage

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.finalproject.R
import com.example.finalproject.domain.model.FilmDetail
import com.example.finalproject.domain.model.ImageItem
import com.example.finalproject.domain.model.MovieItem
import com.example.finalproject.ui.screens.homepage.MovieItemView
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
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(watchedMovies.size) { index ->
                MovieItemView (movie = watchedMovies[index]) {}
            }

            item {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(111.dp)
                        .height(176.dp)
                        .background(Color.Transparent)
                        .clickable { viewModel.clearWatchedMovies() },
                ) {

                    Icon(
                        Icons.Rounded.DeleteOutline,
                        contentDescription = "Back",
                        tint = Color(0xFF3D3BFF),
                        modifier = Modifier
                            .size(35.dp)
                            .clip(shape = CircleShape)
                            .background(Color.White)
                            .padding(6.dp)
                    )
                    Text(
                        text = "Очистить\nисторию",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black,
                    )
                }
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Коллекции",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier
                    .clickable {

                    }
            )

            Spacer(Modifier.width(16.dp))

            Text(
                text = "Создать свою коллекцию",
                style = MaterialTheme.typography.titleMedium,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 120.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                CollectionCard(icon = R.drawable.heartoutlined, name = "Любимые")
            }

            item {
                CollectionCard(icon = R.drawable.bookmarkoutlined, name = "Хочу посмотреть")
            }
        }
    }
}


//LazyVerticalGrid(
//columns = GridCells.Adaptive(minSize = 120.dp),
//contentPadding = PaddingValues(16.dp),
//verticalArrangement = Arrangement.spacedBy(16.dp),
//horizontalArrangement = Arrangement.spacedBy(16.dp),
//modifier = Modifier.padding(paddingValues)
//) {
//    items(images) { image ->
//        ImageCard(image)
//    }
//}

@Composable
fun CollectionCard(icon: Int = R.drawable.useroutlined, name: String) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .border(1.dp, color = Color.Black, shape = RoundedCornerShape(30.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
//            .clickable(onClick = onClick)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .aspectRatio(1f)
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = null,
                alignment = Alignment.Center,
                modifier = Modifier
                    .size(22.dp)
            )
            Text(
                text = name,
                Modifier.padding(7.dp)
            )
            Box(
                modifier = Modifier
                    .background(Color(0xFF3D3BFF), shape = RoundedCornerShape(50))
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "105",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Composable
fun MovieItemView(movie: FilmDetail, onClick: () -> Unit) {
    Log.d("MOVIE_ITEM_VIEW", "Rendering movie: ${movie.nameOriginal}")
    Column(
        modifier = Modifier
            .padding(8.dp)
            .width(111.dp)
            .height(230.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.Start
    ) {
        Box {
            Image(
                painter = rememberAsyncImagePainter(model = movie.posterUrl),
                contentDescription = "Movie Poster",
                modifier = Modifier.size(111.dp, 156.dp)
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-8).dp, y = 8.dp)
                    .background(
                        color = Color(0xFF3D3BFF),
                        shape = RoundedCornerShape(50)
                    )
                    .width(20.dp)
                    .height(12.dp)
            ) {
                Text(
                    text = movie.ratingKinopoisk?.toString() ?: "N/A",
                    color = Color.White,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = movie.nameRu ?: movie.nameOriginal,
            fontSize = 14.sp,
            fontWeight = FontWeight.W400
        )
        Text(
            color = Color.Gray,
            text = movie.genres.joinToString { it.genre },
            fontSize = 12.sp,
            fontWeight = FontWeight.W400
        )
    }
}
