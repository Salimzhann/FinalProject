package com.example.finalproject.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.TextButton
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.finalproject.R
import com.example.finalproject.domain.model.FilmDetail
import com.example.finalproject.domain.model.ScreenState
import com.example.finalproject.domain.model.StaffMember
import com.example.finalproject.ui.viewmodel.MainPageViewModel

@Composable
fun MovieDetailScreen(movieId: Long, viewModel: MainPageViewModel, navController: NavController) {

    LaunchedEffect(movieId) {
        viewModel.loadFilmDetailAndStaffById(movieId)
    }
    val filmDetailState by viewModel.screenStateFilmDetail.observeAsState(ScreenState.Initial)
    val staffMember by viewModel.staffMembers.observeAsState(emptyList())

    Box(modifier = Modifier.fillMaxSize()) {
        when (filmDetailState) {
            is ScreenState.Success -> {
                val filmDetail = (filmDetailState as ScreenState.Success<FilmDetail>).data
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(filmDetail.posterUrl),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                contentScale = ContentScale.Crop
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(400.dp)
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(Color.Transparent, Color.Black),
                                            startY = 0f,
                                            endY = Float.POSITIVE_INFINITY
                                        )
                                    )
                            )

                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "${filmDetail.ratingImdb} ${filmDetail.nameRu}",
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 20.sp,
                                    color = Color.White.copy(alpha = 0.6f)
                                )
                                Text(
                                    text = "${filmDetail.year}, ${filmDetail.genres.joinToString(", ") { it.genre }}",
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 20.sp,
                                    color = Color.White.copy(alpha = 0.6f)
                                )
                                Text(
                                    text = "${filmDetail.countries.joinToString(", ") { it.country }}, ${
                                        calculateTime(
                                            filmDetail.filmLength ?: 0
                                        )
                                    }, ${
                                        filmDetail.ratingAgeLimits?.replace(
                                            Regex("[^0-9]"),
                                            ""
                                        )
                                    } +",
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 20.sp,
                                    color = Color.White.copy(alpha = 0.6f)
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 32.dp),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Favorite,
                                        contentDescription = "Like",
                                        tint = Color.White
                                    )
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = "Star",
                                        tint = Color.White
                                    )
                                    Icon(
                                        imageVector = Icons.Filled.Share,
                                        contentDescription = "Share",
                                        tint = Color.White
                                    )
                                    Icon(
                                        imageVector = Icons.Filled.MoreVert,
                                        contentDescription = "More options",
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(30.dp))
                    }

                    item {
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            Text(
                                text = filmDetail.shortDescription ?: "No description available.",
                                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = filmDetail.description ?: "No description available.",
                                style = MaterialTheme.typography.bodyMedium,
                                lineHeight = 20.sp
                            )
                        }
                    }
                    item {
                        Row(
                            modifier = Modifier
                                .padding(top = 15.dp)
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(
                                text = "В фильме снимались",
                                fontWeight = FontWeight.Medium,
                                fontSize = 25.sp
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            TextButton(onClick = {

                            }) {
                                Text(
                                    text = "${staffMember.size} >",
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 25.sp,
                                    color = Color.Blue,
                                )
                            }
                        }
                        LazyRow {
                            items(staffMember) { staff ->
                                StaffCard(staff, onClick = {
                                    navController.navigate("actorDetail/${staff.staffId}")
                                })
                            }
                        }
                    }
                }
            }
            is ScreenState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            is ScreenState.Error -> Text("Failed to load details", color = Color.Red, modifier = Modifier.align(Alignment.Center))
            else -> Unit
        }
    }
}

private fun calculateTime(minute: Int): String {
    val hours = minute / 60
    val minutes = minute % 60
    return "$hours ч $minutes мин"
}
