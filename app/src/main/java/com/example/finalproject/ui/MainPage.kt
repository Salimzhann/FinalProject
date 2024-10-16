package com.example.finalproject.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finalproject.Model.MainPageViewModel

class MainPage : ComponentActivity() {

    private val viewModel = MainPageViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            setupUI(viewModel)
        }
    }
}

@Composable
fun setupUI(viewModel: MainPageViewModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        MovieSection("Премьеры", viewModel.premieres)
        Spacer(modifier = Modifier.height(16.dp))
        MovieSection("Популярное", viewModel.popularCinema)
        Spacer(modifier = Modifier.height(16.dp))
        MovieSection("Боевики США", viewModel.usaActionMovies)
    }
}

@Composable
fun MovieSection(title: String, movies: List<MainPageViewModel.MovieItem>) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Все",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable { /* Some action */ }
            )
        }
        LazyRow {
            items(movies.size) { index ->
                MovieItemView(movie = movies[index])
            }
        }
    }
}

@Composable
fun MovieItemView(movie: MainPageViewModel.MovieItem) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .width(120.dp)
            .clickable {
                // some action
            },
        horizontalAlignment = Alignment.Start
    ) {
        Box {
            Image(
                painter = painterResource(movie.imagePath),
                contentDescription = "Movie Poster",
                modifier = Modifier.size(111.dp, 156.dp)
            )

            // Rating Badge with Perfect Centering
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd) // Positioning at the top-right corner
                    .offset(x = (-8).dp, y = 8.dp) // Adjust position slightly
                    .background(
                        color = Color(0xFF3D5AFE), // Blue background
                        shape = RoundedCornerShape(50) // Fully rounded shape for a pill effect
                    )
                    .width(20.dp) // Increase width to fit the text
                    .height(12.dp) // Adjust height for a better fit
            ) {
                Text(
                    text = movie.rating.toString(),
                    color = Color.White,
                    fontSize = 8.sp, // Adjusted font size to fit within small dimensions
                    fontWeight = FontWeight(500),
                    modifier = Modifier.align(Alignment.Center) // Center text within the box
                )
            }


        }

        Spacer(modifier = Modifier.height(4.dp))
        Text(text = movie.title, style = MaterialTheme.typography.bodyLarge)
        Text(text = movie.genre, style = MaterialTheme.typography.bodySmall)
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val viewModel = MainPageViewModel()
    setupUI(viewModel)
}