package com.example.finalproject.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.finalproject.domain.model.MovieItem

@Composable
fun MovieItemView(movie: MovieItem, onClick: () -> Unit) {
    Log.d("MOVIE_ITEM_VIEW", "Rendering movie: ${movie.nameOriginal}")
    Column(
        modifier = Modifier
            .padding(8.dp)
            .width(111.dp)
            .height(230.dp)
            .clickable(onClick = onClick),  // Add click handling here
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
